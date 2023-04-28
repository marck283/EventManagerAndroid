package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.user_event_registration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.NetworkRequest;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.networkOps.ServerOperation;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.EventDetailsFragment;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserEventRegistration extends ServerOperation {

    private final NetworkRequest request;

    private final EventDetailsFragment f;

    private final String accessToken, eventId, day, time;

    private final ActivityResultLauncher<Intent> launcher;

    public UserEventRegistration(@NonNull String accessToken, @NonNull String eventId, @NonNull String day,
                                 @NonNull String time, @NonNull EventDetailsFragment f,
                                 @Nullable ActivityResultLauncher<Intent> launcher) {
        request = getNetworkRequest();
        this.f = f;
        this.accessToken = accessToken;
        this.eventId = eventId;
        this.day = day;
        this.time = time;
        this.launcher = launcher;
    }

    private void setAlertDialog(@StringRes int title, @StringRes int message) {
        Activity activity = f.getActivity();
        if(activity != null && f.isAdded()) {
            f.requireActivity().runOnUiThread(() -> {
                AlertDialog ad = new AlertDialog.Builder(f.requireActivity()).create();
                ad.setTitle(title);
                ad.setMessage(f.getString(message));
                ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                ad.show();
            });
        }
    }

    public void run() {
        Log.i("day", day);
        Log.i("time", time);

        List<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<>("x-access-token", accessToken));
        RequestBody body = new FormBody.Builder()
                .add("data", day)
                .add("ora", time)
                .build();
        Request req = request.getPostRequest(body, headers,
                getBaseUrl() + "/api/v2/EventiPubblici/" + eventId + "/Iscrizioni");
        request.enqueue(req, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                try {
                    throw e;
                } catch (Throwable e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                switch(response.code()) {
                    case 201: {
                        //Successo
                        setAlertDialog(R.string.event_registration_success_title, R.string.event_registration_success);
                        break;
                    }
                    case 400: {
                        //Richiesta malformata
                        setAlertDialog(R.string.malformed_request, R.string.malformed_request_message);
                        break;
                    }
                    case 401: {
                        Activity activity = f.getActivity();
                        if(launcher != null && activity != null && f.isAdded()) {
                            Intent loginIntent = new Intent(f.requireActivity(), LoginActivity.class);
                            launcher.launch(loginIntent);
                        } else {
                            setAlertDialog(R.string.user_not_logged_in, R.string.user_not_logged_in_message);
                        }
                        break;
                    }
                    case 403:
                    case 404: {
                        setAlertDialog(R.string.error, R.string.max_pers_reached_or_user_already_registered);
                        break;
                    }
                    case 500: {
                        //Errore interno al server
                        setAlertDialog(R.string.internal_server_error, R.string.service_unavailable);
                        break;
                    }
                    case 503: {
                        setAlertDialog(R.string.request_timeout, R.string.request_timeout_message);
                        break;
                    }
                    default: {
                        //Errore sconosciuto
                        setAlertDialog(R.string.unknown_error, R.string.unknown_error_message);
                        break;
                    }
                }
            }
        });
    }
}
