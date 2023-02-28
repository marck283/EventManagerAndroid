package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.registeredEvent.ticket.delete_ticket;

import android.app.AlertDialog;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.networkRequests.NetworkRequest;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class DeleteTicket extends Thread {
    private final String eventId, ticketId, userJwt, data, ora;

    private final NetworkRequest request;

    private final Fragment f;

    public DeleteTicket(@NonNull String eventId, @NonNull String ticketId, @NonNull String userJwt,
                        @NonNull Fragment f, @NonNull String data, @NonNull String ora) {
        this.eventId = eventId;
        this.ticketId = ticketId;
        this.userJwt = userJwt;
        request = new NetworkRequest();
        this.f = f;
        this.data = data;
        this.ora = ora;
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
        List<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<>("x-access-token", userJwt));
        headers.add(new Pair<>("data", data));
        headers.add(new Pair<>("ora", ora));
        Request req = request.getDeleteRequest(headers,
                "https://eventmanagerzlf.herokuapp.com/api/v2/EventiPubblici/" + eventId + "/Iscrizioni/" + ticketId);
        request.enqueue(req, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                try {
                    throw e;
                } catch(Throwable ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                /*
                * Tra tutti i codici di errore gestiti qui sotto solo "204", "401" e "500" sono realmente utilizzati.
                * Tutti gli altri servono per ragioni di debugging e non dovrebbero essere mai restituiti dal server.
                * */
                switch(response.code()) {
                    case 204: {
                        setAlertDialog(R.string.deletion_successful, R.string.deletion_successful_message);
                        break;
                    }
                    case 401: {
                        //Utente non autenticato
                        setAlertDialog(R.string.user_not_logged_in, R.string.user_not_logged_in_message);
                        break;
                    }
                    case 403: {
                        setAlertDialog(R.string.user_not_registered, R.string.user_not_registered_message);
                        break;
                    }
                    case 404: {
                        setAlertDialog(R.string.deletion_404, R.string.deletion_404_message);
                        break;
                    }
                    case 500: {
                        //Errore interno al server
                        setAlertDialog(R.string.internal_server_error, R.string.internal_server_error);
                        break;
                    }
                }
            }
        });
    }
}
