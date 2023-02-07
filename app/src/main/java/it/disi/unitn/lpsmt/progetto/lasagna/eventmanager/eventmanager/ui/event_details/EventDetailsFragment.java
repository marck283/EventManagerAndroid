package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.StringRes;
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
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Objects;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.NavigationDrawerActivity;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses.DBOrgEvents;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.NetworkCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.NavigationSharedViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.callbacks.OrganizerCallback;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EventDetailsFragment extends Fragment {

    private EventDetailsViewModel mViewModel;
    private NavigationSharedViewModel nvm;
    private String day, time;

    //Indica il tipo della schermata (ad esempio "iscr" per utente iscritto, od "org" per "organizzatore")
    private String screenType;
    private String eventId;

    private ActivityResultLauncher<ScanOptions> launcher;

    private ActivityResultLauncher<Intent> loginLauncher;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle b = getArguments();
        if (b != null) {
            screenType = b.getString("eventType");
            eventId = b.getString("eventId");
            day = b.getString("day");
        }
        switch (screenType) {
            case "pub": {
                return inflater.inflate(R.layout.public_event_info, container, false);
            }
            case "iscr": {
                return inflater.inflate(R.layout.dettagli_evento_iscritto, container, false);
            }
            case "org": {
                return inflater.inflate(R.layout.dettagli_evento_organizzatore, container, false);
            }
        }

        return null;
    }

    private void setAlertDialog(@StringRes int title, @StringRes int message) {
        requireActivity().runOnUiThread(() -> {
            AlertDialog dialog = new AlertDialog.Builder(requireActivity()).create();
            dialog.setTitle(title);
            dialog.setMessage(getString(message));
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
            dialog.show();
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EventDetailsViewModel.class);
        nvm = new ViewModelProvider(requireActivity()).get(NavigationSharedViewModel.class);

        switch (screenType) {
            case "pub": {
                //Controllare questo blocco con un utente non registrato... dovrebbe reindirizzare
                //all'Activity di login, ma qualcosa non funziona...
                loginLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            switch (result.getResultCode()) {
                                case Activity.RESULT_OK: {
                                    mViewModel.getEventInfo("pub", eventId, view, this, nvm.getToken().getValue(),
                                            day, launcher, null);
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
                                            .updateUI("logout", "", "", false);
                                    break;
                                }
                            }
                        });
                mViewModel.getEventInfo("pub", eventId, view, this, null, null,
                        null, null);

                Button b = view.findViewById(R.id.sign_up_button);
                b.setEnabled(false);
                b.setOnClickListener(c -> {
                    if (eventId != null && day != null && !day.equals("") && !day.equals("---") &&
                            time != null && !time.equals("") && !time.equals("---") && nvm.getToken() != null &&
                            nvm.getToken().getValue() != null) {
                        String[] dayArr = day.split("/");
                        day = dayArr[1] + "-" + dayArr[0] + "-" + dayArr[2];

                        SharedPreferences prefs = requireActivity().getSharedPreferences("AccTok", Context.MODE_PRIVATE);
                        String token = prefs.getString("accessToken", "");
                        if(!token.equals("")) {
                            mViewModel.registerUser(token, eventId, this, day, time, loginLauncher);
                        }
                    }
                });

                ((TextView) view.findViewById(R.id.event_address)).setText(getString(R.string.event_address, ""));
                ((TextView) view.findViewById(R.id.duration)).setText(getString(R.string.duration, "", "", ""));
                ((TextView) view.findViewById(R.id.organizerName)).setText(getString(R.string.organizer, ""));

                Button ratings = view.findViewById(R.id.show_ratings);
                ratings.setOnClickListener(c -> {
                    Bundle b1 = new Bundle();
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

                ActivityResultLauncher<Intent> loginLauncher = registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                result -> {
                            if(result.getResultCode() == Activity.RESULT_OK) {
                                mViewModel.getEventInfo("iscr", eventId, view, this, nvm.getToken().getValue(),
                                        day, null, null);
                            }
                });

                mViewModel.getEventInfo("iscr", eventId, view, this, nvm.getToken().getValue(),
                        day, null, loginLauncher);
                break;
            }
            case "org": {
                TextView duration = view.findViewById(R.id.textView12);
                duration.setText(getString(R.string.duration, "", "", ""));

                TextView address = view.findViewById(R.id.textView15);
                address.setText(getString(R.string.event_address, ""));

                TextInputLayout spinner = view.findViewById(R.id.spinner), spinner2 = view.findViewById(R.id.spinner2);
                NetworkCallback callback = new NetworkCallback(requireActivity());

                launcher = registerForActivityResult(new ScanContract(),
                        result -> {
                            if (result.getContents() != null && spinner.getEditText() != null &&
                                    spinner.getEditText().getText() != null &&
                                    !spinner.getEditText().getText().toString().equals("---") &&
                                    spinner2.getEditText() != null && spinner2.getEditText().getText() != null &&
                                    !spinner2.getEditText().getText().toString().equals("---")) {
                                SharedPreferences prefs = requireActivity().getSharedPreferences("AccTok", Context.MODE_PRIVATE);
                                String token = prefs.getString("accessToken", "");
                                if(!token.equals("")) {
                                    if(callback.isOnline(requireActivity())) {
                                        mViewModel.checkQR(token, result.getContents(),
                                                eventId, spinner2.getEditText().getText().toString(),
                                                spinner.getEditText().getText().toString(), this);
                                    } else {
                                        setNoConnectionDialog();
                                    }
                                }
                            }
                        });
                loginLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            switch (result.getResultCode()) {
                                case Activity.RESULT_OK: {
                                    if(callback.isOnline(requireActivity())) {
                                        mViewModel.getEventInfo("org", eventId, view, this, nvm.getToken().getValue(),
                                                day, launcher, null);
                                    } else {
                                        DBOrgEvents orgEvents = new DBOrgEvents(this, eventId);
                                        orgEvents.start();
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
                                            .updateUI("logout", "", "", false);
                                    break;
                                }
                            }
                        });
                if(callback.isOnline(requireActivity())) {
                    mViewModel.getEventInfo("org", eventId, view, this, nvm.getToken().getValue(),
                            day, launcher, loginLauncher);
                } else {
                    //Nessuna connessione ad Internet
                    DBOrgEvents orgEvents = new DBOrgEvents(this, eventId);
                    orgEvents.start();
                }

                Button qrCodeScan = view.findViewById(R.id.button8), terminaEvento = view.findViewById(R.id.button12);
                terminaEvento.setOnClickListener(c -> {
                    if(!callback.isOnline(requireActivity())) {
                        setNoConnectionDialog();
                        return;
                    }
                    try {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .addHeader("x-access-token", Objects.requireNonNull(nvm.getToken().getValue()))
                                .url("https://eventmanagerzlf.herokuapp.com/api/v2/EventiPubblici/" + eventId)
                                .build();
                        client.newCall(request).enqueue(new OrganizerCallback() {
                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) {
                                switch (response.code()) {
                                    case 403: {
                                        setAlertDialog(R.string.unauthorized_attempt, R.string.unauthorized_attempt_message);
                                        break;
                                    }
                                    case 200: {
                                        setAlertDialog(R.string.attempt_ok, R.string.attempt_ok_message);

                                        // Ora disabilita tutti i bottoni della schermata e,
                                        // la prossima volta che questo evento viene mostrato,
                                        // mantieni i bottoni bloccati...
                                        qrCodeScan.setEnabled(false);
                                        terminaEvento.setEnabled(false);
                                        break;
                                    }
                                }
                            }
                        });
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                });

                Button annullaEvento = view.findViewById(R.id.button13);
                annullaEvento.setOnClickListener(c -> {
                    if(!callback.isOnline(requireActivity())) {
                        setNoConnectionDialog();
                        return;
                    }
                    try {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .addHeader("x-access-token", Objects.requireNonNull(nvm.getToken().getValue()))
                                .url("https://eventmanagerzlf.herokuapp.com/api/v2/annullaEvento/" + eventId)
                                .delete()
                                .build();
                        client.newCall(request).enqueue(new OrganizerCallback() {
                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) {
                                switch (response.code()) {
                                    case 401: {
                                        setAlertDialog(R.string.user_not_logged_in, R.string.user_not_logged_in_message);
                                        break;
                                    }
                                    case 403: {
                                        setAlertDialog(R.string.unauthorized_attempt, R.string.unauthorized_attempt_message);
                                        break;
                                    }
                                    case 200: {
                                        setAlertDialog(R.string.attempt_ok, R.string.attempt_ok_message);
                                        requireActivity().runOnUiThread(() ->
                                                Navigation.findNavController(view).navigate(
                                                        R.id.action_eventDetailsFragment_to_user_calendar_dialog));
                                        break;
                                    }
                                    case 404: {
                                        setAlertDialog(R.string.no_event, R.string.no_event_message);
                                        break;
                                    }
                                }
                            }
                        });
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
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