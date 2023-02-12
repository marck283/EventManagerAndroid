package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Objects;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.NavigationDrawerActivity;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses.DBOrgEvents;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.NetworkCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.NavigationSharedViewModel;

public class EventDetailsFragment extends Fragment {

    private EventDetailsViewModel mViewModel;
    private NavigationSharedViewModel nvm;
    private String day, time;

    //Indica il tipo della schermata (ad esempio "iscr" per utente iscritto, od "org" per "organizzatore")
    private String screenType;
    private String eventId;

    private ActivityResultLauncher<ScanOptions> launcher;

    private ActivityResultLauncher<Intent> loginLauncher, loginLauncher1;

    private SharedPreferences prefs;

    private String token = "";

    private View view;

    private NetworkCallback callback;

    private TextInputLayout spinner, spinner2;

    public void setEventId(@NonNull String val) {
        eventId = val;
    }

    public void setDay(@NonNull String day) {
        this.day = day;
    }

    public void setTime(@NonNull String time) {
        this.time = time;
    }

    @NonNull
    public static EventDetailsFragment newInstance() {
        return new EventDetailsFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = requireActivity().getSharedPreferences("AccTok", Context.MODE_PRIVATE);
        token = prefs.getString("accessToken", "");

        loginLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    switch (result.getResultCode()) {
                        case Activity.RESULT_OK: {
                            token = prefs.getString("accessToken", "");
                            mViewModel.registerUser(token, eventId, this, day, time, null);
                            break;
                        }
                        case Activity.RESULT_CANCELED: {
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("accessToken", "");
                            editor.apply();
                            Navigation.findNavController(view).navigate(R.id.action_eventDetailsFragment_to_nav_event_list);
                            ((NavigationDrawerActivity) requireActivity())
                                    .updateUI("logout", "", "", "", false);
                            break;
                        }
                    }
                });

        Bundle b = getArguments();
        if (b != null) {
            screenType = b.getString("eventType");
            eventId = b.getString("eventId");
            day = b.getString("day");
        }
        if(screenType.equals("iscr")) {
            loginLauncher1 = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        switch(result.getResultCode()) {
                            case Activity.RESULT_OK: {
                                mViewModel.getEventInfo("iscr", eventId, view, this, nvm.getToken().getValue(),
                                        day, null, null);
                                break;
                            }
                            case Activity.RESULT_CANCELED: {
                                //Ritorna alla schermata principale, reimpostando il token alla stringa vuota
                                //e chiedendo all'Activity NavigationDrawerActivity di reimpostare il suo menù
                                //a quello riservato agli utenti non autenticati.
                            }
                        }
                    });
        } else {
            if(screenType.equals("org")) {
                launcher = registerForActivityResult(new ScanContract(),
                        result -> {
                            if (result.getContents() != null && spinner.getEditText() != null &&
                                    spinner.getEditText().getText() != null &&
                                    !spinner.getEditText().getText().toString().equals("---") &&
                                    spinner2.getEditText() != null && spinner2.getEditText().getText() != null &&
                                    !spinner2.getEditText().getText().toString().equals("---")) {
                                SharedPreferences prefs = requireActivity().getSharedPreferences("AccTok", Context.MODE_PRIVATE);
                                String token = prefs.getString("accessToken", "");
                                if (!token.equals("")) {
                                    if (callback.isOnline(requireActivity())) {
                                        mViewModel.checkQR(token, result.getContents(),
                                                eventId, spinner2.getEditText().getText().toString(),
                                                spinner.getEditText().getText().toString(), this);
                                    } else {
                                        setNoConnectionDialog();
                                        callback.registerNetworkCallback();
                                        callback.addDefaultNetworkActiveListener(() ->
                                                mViewModel.checkQR(token, result.getContents(),
                                                        eventId, spinner2.getEditText().getText().toString(),
                                                        spinner.getEditText().getText().toString(), this));
                                        callback.unregisterNetworkCallback();
                                    }
                                }
                            }
                        });
                loginLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            switch (result.getResultCode()) {
                                case Activity.RESULT_OK: {
                                    if (callback.isOnline(requireActivity())) {
                                        mViewModel.getEventInfo("org", eventId, view, this, nvm.getToken().getValue(),
                                                day, launcher, null);
                                    } else {
                                        setNoConnectionDialog();
                                        callback.registerNetworkCallback();
                                        callback.addDefaultNetworkActiveListener(() ->
                                                mViewModel.getEventInfo("org", eventId, view, this, nvm.getToken().getValue(),
                                                        day, launcher, null));
                                        callback.unregisterNetworkCallback();
                                    }
                                    break;
                                }
                                case Activity.RESULT_CANCELED: {
                                    SharedPreferences prefs =
                                            requireActivity().getSharedPreferences("AccTok", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("accessToken", "");
                                    editor.apply();
                                    Navigation.findNavController(view).navigate(R.id.action_eventDetailsFragment_to_nav_event_list);
                                    ((NavigationDrawerActivity) requireActivity())
                                            .updateUI("logout", "", "", "", false);
                                    break;
                                }
                            }
                        });
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        switch (screenType) {
            case "pub": {
                view = inflater.inflate(R.layout.public_event_info, container, false);
                break;
            }
            case "iscr": {
                view = inflater.inflate(R.layout.dettagli_evento_iscritto, container, false);
                break;
            }
            case "org": {
                view = inflater.inflate(R.layout.dettagli_evento_organizzatore, container, false);
                break;
            }
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EventDetailsViewModel.class);
        nvm = new ViewModelProvider(requireActivity()).get(NavigationSharedViewModel.class);
    }

    public void onStart() {
        super.onStart();

        switch (screenType) {
            case "pub": {
                mViewModel.getEventInfo("pub", eventId, view, this, null, null,
                        null, null);

                Button b = view.findViewById(R.id.cLayout).findViewById(R.id.sign_up_button);
                b.setEnabled(false);
                b.setOnClickListener(c -> {
                    TextInputLayout spinner = view.findViewById(R.id.spinner), spinner2 = view.findViewById(R.id.dateArray);
                    EditText spinnerText = spinner.getEditText(), spinner2Text = spinner2.getEditText();
                    String token = prefs.getString("accessToken", "");
                    if (eventId != null && spinnerText != null && !spinnerText.getText().toString().equals("")
                            && !spinnerText.getText().toString().equals("---") &&
                            spinner2Text != null && !spinner2Text.getText().toString().equals("") &&
                            !spinner2Text.getText().toString().equals("---")) {
                        String[] dayArr = spinnerText.getText().toString().split("/");
                        day = dayArr[1] + "-" + dayArr[0] + "-" + dayArr[2];
                        time = spinner2Text.getText().toString();
                        mViewModel.registerUser(token, eventId, this, day, time, loginLauncher);
                    }
                });

                ((TextView) view.findViewById(R.id.event_address)).setText(getString(R.string.event_address, ""));
                ((TextView) view.findViewById(R.id.duration)).setText(getString(R.string.duration, "", "", ""));
                ((TextView) view.findViewById(R.id.organizerName)).setText(getString(R.string.organizer, ""));

                Button ratings = view.findViewById(R.id.show_ratings);
                ratings.setOnClickListener(c -> {
                    Bundle b1 = new Bundle();
                    b1.putString("screenType", screenType);
                    b1.putString("eventId", eventId);
                    Navigation.findNavController(view).navigate(R.id.action_eventDetailsFragment_to_reviewsFragment, b1);
                });

                break;
            }
            case "iscr": {
                TextView organizer = view.findViewById(R.id.textView16), dayTextView = view.findViewById(R.id.textView11),
                        time = view.findViewById(R.id.textView20), duration = view.findViewById(R.id.textView39),
                        address = view.findViewById(R.id.textView42);
                organizer.setText(getString(R.string.organizer, ""));
                dayTextView.setText(getString(R.string.day_not_selectable, ""));
                time.setText(getString(R.string.time_not_selectable, ""));
                duration.setText(getString(R.string.duration, "", "", ""));
                address.setText(getString(R.string.event_address, ""));

                mViewModel.getEventInfo("iscr", eventId, view, this, nvm.getToken().getValue(),
                        day, null, loginLauncher1);
                break;
            }
            case "org": {
                TextView duration = view.findViewById(R.id.textView12);
                duration.setText(getString(R.string.duration, "", "", ""));

                TextView address = view.findViewById(R.id.textView15);
                address.setText(getString(R.string.event_address, ""));

                spinner = view.findViewById(R.id.spinner);
                spinner2 = view.findViewById(R.id.spinner2);
                callback = new NetworkCallback(requireActivity());

                Button qrCodeScan = view.findViewById(R.id.button8),
                        terminaEvento = view.findViewById(R.id.button12),
                        annullaEvento = view.findViewById(R.id.button13);

                if (callback.isOnline(requireActivity())) {
                    mViewModel.getEventInfo("org", eventId, view, this, nvm.getToken().getValue(),
                            day, launcher, loginLauncher);
                } else {
                    //Nessuna connessione ad Internet. Acquisire i dati dal database e visualizzarli a schermo
                    AlertDialog dialog = new AlertDialog.Builder(requireActivity()).create();
                    dialog.setTitle(R.string.no_connection);
                    dialog.setMessage(getString(R.string.buttons_disabled));
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                    dialog.show();

                    qrCodeScan.setEnabled(false);
                    terminaEvento.setEnabled(false);
                    annullaEvento.setEnabled(false);

                    DBOrgEvents events = new DBOrgEvents(this, eventId, view);
                    events.start();

                    callback.registerNetworkCallback();
                    callback.addDefaultNetworkActiveListener(() -> {
                        qrCodeScan.setEnabled(true);
                        terminaEvento.setEnabled(true);
                        annullaEvento.setEnabled(true);
                    });
                    callback.unregisterNetworkCallback();
                }

                qrCodeScan.setOnClickListener(c -> {
                    EditText editText = spinner.getEditText(), editText1 = spinner2.getEditText();
                    if (editText != null &&
                            !editText.getText().toString().equals("") &&
                            !editText.getText().toString().equals("---") &&
                            editText1 != null && !editText.getText().toString().equals("") &&
                            !editText.getText().toString().equals("---")) {
                        launcher.launch(new ScanOptions());
                    }
                });

                terminaEvento.setOnClickListener(c -> {
                    MaterialAutoCompleteTextView hourTextView = spinner.findViewById(R.id.orgHourTextView);
                    if (!callback.isOnline(requireActivity())) {
                        setNoConnectionDialog();
                    } else {
                        try {
                            //Aggiungere ActivityResultLauncher per ottenere un nuovo token dall'Activity di login.
                            //Ricordarsi anche di aggiornare "token" all'interno del launcher!
                            mViewModel.terminateEvent(token,
                                    this, eventId, day, hourTextView.getText().toString(), view);
                        } catch (NullPointerException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                annullaEvento.setOnClickListener(c -> {
                    if (!callback.isOnline(requireActivity())) {
                        setNoConnectionDialog();
                        return;
                    }
                    //Aggiungere ActivityResultLauncher per ottenere un nuovo token dall'Activity di login.
                    //Ricordarsi anche di aggiornare "token" all'interno del launcher!
                    mViewModel.deleteEvent(Objects.requireNonNull(nvm.getToken().getValue()), eventId, this, view);
                });
            }
        }
    }

    private void setNoConnectionDialog() {
        AlertDialog dialog = new AlertDialog.Builder(requireActivity()).create();
        dialog.setTitle(R.string.no_connection);
        dialog.setMessage(getString(R.string.no_connection_message));
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
        dialog.show();
    }

    public EventDetailsViewModel getViewModel() {
        return mViewModel;
    }

}