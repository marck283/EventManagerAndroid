package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.registeredEvent.ticket.delete_ticket;

import android.app.AlertDialog;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.IOException;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DeleteTicket extends Thread {
    private final String eventId, ticketId, userJwt;
    private final OkHttpClient client;

    private final Fragment f;

    public DeleteTicket(@NonNull String eventId, @NonNull String ticketId, @NonNull String userJwt, @NonNull Fragment f) {
        this.eventId = eventId;
        this.ticketId = ticketId;
        this.userJwt = userJwt;
        client = new OkHttpClient();
        this.f = f;
    }

    public void run() {
        Request request = new Request.Builder()
                .addHeader("x-access-token", userJwt)
                .url("https://eventmanagerzlf.herokuapp.com/api/v2/EventiPubblici/" + eventId + "/Iscrizioni/" + ticketId)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //Nulla qui...
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                Looper.prepare();
                AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();

                /*
                * Di tutti i codici di errore gestiti qui sotto solo "204", "401" e "500" sono realmente utilizzati.
                * Tutti gli altri servono per ragioni di debugging e non dovrebbero essere mai restituiti dal server.
                * */
                switch(response.code()) {
                    case 204: {
                        dialog.setTitle(R.string.deletion_successful);
                        dialog.setMessage(f.getString(R.string.deletion_successful_message));
                        break;
                    }
                    case 401: {
                        //Utente non autenticato
                        dialog.setTitle(R.string.user_not_logged_in);
                        dialog.setMessage(f.getString(R.string.user_not_logged_in_message));
                        break;
                    }
                    case 403: {
                        dialog.setTitle(R.string.user_not_registered);
                        dialog.setMessage(f.getString(R.string.user_not_registered_message));
                        break;
                    }
                    case 404: {
                        dialog.setTitle(R.string.deletion_404);
                        dialog.setMessage(f.getString(R.string.deletion_404_message));
                        break;
                    }
                    case 500: {
                        //Errore interno al server
                        dialog.setTitle(R.string.internal_server_error);
                        dialog.setMessage(f.getString(R.string.internal_server_error));
                        break;
                    }
                }
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                dialog.show();
                Looper.loop();
                Looper.getMainLooper().quitSafely();
            }
        });
    }
}
