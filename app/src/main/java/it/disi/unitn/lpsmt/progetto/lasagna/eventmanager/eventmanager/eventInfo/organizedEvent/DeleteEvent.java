package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.organizedEvent;

import android.app.AlertDialog;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.callbacks.OrganizerCallback;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DeleteEvent extends Thread {
    private final OkHttpClient client;
    private final String accessToken, eventId;

    private final Fragment f;

    private final View v;

    public DeleteEvent(@NonNull String accessToken, @NonNull String eventId, @NonNull Fragment f, @NonNull View v) {
        client = new OkHttpClient();
        this.accessToken = accessToken;
        this.eventId = eventId;
        this.f = f;
        this.v = v;
    }

    private void setAlertDialog(@StringRes int title, @StringRes int message) {
        f.requireActivity().runOnUiThread(() -> {
            AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
            dialog.setTitle(title);
            dialog.setMessage(f.getString(message));
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
            dialog.show();
        });
    }

    public void run() {
        Request request = new Request.Builder()
                .addHeader("x-access-token", accessToken)
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

                        //Questa riga sarebbe da rimuovere per risolvere il problema della navigazione
                        //nella direzione sbagliata quando si annulla un evento passando per la
                        //tabella degli eventi organizzati
                        f.requireActivity().runOnUiThread(() ->
                                Navigation.findNavController(v).navigate(
                                        R.id.action_eventDetailsFragment_to_user_calendar_dialog));
                        break;
                    }
                    case 404: {
                        setAlertDialog(R.string.no_event, R.string.no_event_message);
                        break;
                    }
                    default: {
                        setAlertDialog(R.string.unknown_error, R.string.unknown_error_message);
                        break;
                    }
                }
            }
        });
    }
}
