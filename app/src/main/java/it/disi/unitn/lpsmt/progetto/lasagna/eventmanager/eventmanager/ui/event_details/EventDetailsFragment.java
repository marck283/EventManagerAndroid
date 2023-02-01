package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.StringRes;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Objects;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
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
        if(b != null) {
            screenType = b.getString("eventType");
            eventId = b.getString("eventId");
            day = b.getString("day");
        }
        switch(screenType) {
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
        Looper.prepare();
        AlertDialog dialog = new AlertDialog.Builder(requireActivity()).create();
        dialog.setTitle(title);
        dialog.setMessage(getString(message));
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
        dialog.show();
        Looper.loop();
        Looper.getMainLooper().quitSafely();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EventDetailsViewModel.class);
        nvm = new ViewModelProvider(requireActivity()).get(NavigationSharedViewModel.class);

        switch(screenType) {
            case "pub": {
                mViewModel.getEventInfo("pub", eventId, view, this, null, null, null);

                Button b = view.findViewById(R.id.sign_up_button);
                b.setEnabled(false);
                b.setOnClickListener(c -> {
                    if(eventId != null && day != null && !day.equals("") && !day.equals("---") &&
                            time != null && !time.equals("") && !time.equals("---") && nvm.getToken() != null &&
                    nvm.getToken().getValue() != null) {
                        mViewModel.registerUser(nvm.getToken().getValue(), eventId, this, day, time);
                    }
                });

                ((TextView)view.findViewById(R.id.event_address)).setText(getString(R.string.event_address, ""));

                Button ratings = view.findViewById(R.id.show_ratings);
                ratings.setOnClickListener(c -> {
                    Bundle b1 = new Bundle();
                    b1.putString("eventId", eventId);
                    Navigation.findNavController(view).navigate(R.id.action_eventDetailsFragment_to_reviewsFragment, b1);
                });

                break;
            }
            case "iscr": {
                mViewModel.getEventInfo("iscr", eventId, view, this, nvm.getToken().getValue(), day, null);
                break;
            }
            case "org": {
                Spinner spinner = view.findViewById(R.id.spinner);
                launcher = registerForActivityResult(new ScanContract(),
                        result -> {
                            if (result.getContents() != null && spinner.getSelectedItem() != null) {
                                mViewModel.checkQR(nvm.getToken().getValue(), result.getContents(),
                                        eventId, day, spinner.getSelectedItem().toString(), this);
                            }
                        });
                mViewModel.getEventInfo("org", eventId, view, this, nvm.getToken().getValue(), day, launcher);

                Button qrCodeScan = view.findViewById(R.id.button8), terminaEvento = view.findViewById(R.id.button12);
                terminaEvento.setOnClickListener(c -> {
                    try {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .addHeader("x-access-token", Objects.requireNonNull(nvm.getToken().getValue()))
                                .url("https://eventmanagerzlf.herokuapp.com/api/v2/EventiPubblici/" + eventId)
                                .build();
                        client.newCall(request).enqueue(new OrganizerCallback() {
                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) {
                                switch(response.code()) {
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
                    } catch(NullPointerException ex) {
                        ex.printStackTrace();
                    }
                });

                Button annullaEvento = view.findViewById(R.id.button13);
                annullaEvento.setOnClickListener(c -> {
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
                                switch(response.code()) {
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
                                        Navigation.findNavController(view).navigate(R.id.action_eventDetailsFragment_to_user_calendar_dialog);
                                        break;
                                    }
                                    case 404: {
                                        setAlertDialog(R.string.no_event, R.string.no_event_message);
                                        break;
                                    }
                                }
                            }
                        });
                    } catch(NullPointerException ex) {
                        ex.printStackTrace();
                    }
                });
            }
        }
    }

    public EventDetailsViewModel getViewModel() {
        return mViewModel;
    }

}