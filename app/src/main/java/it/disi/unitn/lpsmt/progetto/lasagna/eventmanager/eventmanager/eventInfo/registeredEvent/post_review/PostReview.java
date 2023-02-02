package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.registeredEvent.post_review;

import android.app.AlertDialog;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.io.IOException;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostReview extends Thread {
    private final String userId, eventId, titolo, motivazione;
    private final float valutazione;

    private final OkHttpClient client;

    private final Fragment f;

    private final View v;

    public PostReview(@NonNull String userId, @NonNull String eventId, @NonNull String titolo,
                      @NonNull String motivazione, float valutazione, @NonNull Fragment f, @NonNull View v) {
        client = new OkHttpClient();
        this.userId = userId;
        this.eventId = eventId;
        this.titolo = titolo;
        this.motivazione = motivazione;
        this.valutazione = valutazione;
        this.f = f;
        this.v = v;
    }

    public void run() {
        //La valutazione deve essere espressa in decimi.
        RequestBody body = new FormBody.Builder().add("title", titolo)
                .add("evaluation", String.valueOf(2*valutazione)).add("description", motivazione).build();
        Request request = new Request.Builder()
                .addHeader("x-access-token", userId)
                .url("https://eventmanagerzlf.herokuapp.com/api/v2/Recensioni/" + eventId)
                .post(body)
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
                switch(response.code()) {
                    case 201: {
                        dialog.setTitle(R.string.review_creation_successful);
                        dialog.setMessage(f.getString(R.string.review_creation_successful_message));
                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> {
                            dialog1.dismiss();

                            f.requireActivity().runOnUiThread(() ->
                            Navigation.findNavController(v).navigate(R.id.action_reviewWriting_to_nav_user_calendar));
                        });
                        break;
                    }
                    case 401: {
                        //Qui dovrei implementare un metodo per eseguire il login dell'utente e
                        //far partire un'altra richiesta verso il server...
                        break;
                    }
                    case 400: {
                        // Primo dei due codici usati solo per il debug.
                        // Non dovrebbero esserci problemi durante l'utilizzo normale dell'applicazione.
                        dialog.setTitle(R.string.malformed_request);
                        dialog.setMessage(f.getString(R.string.malformed_request_message));
                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                        break;
                    }
                    case 500: {
                        //Secondo dei due codici usati solo per il debug.
                        dialog.setTitle(R.string.internal_server_error);
                        dialog.setMessage(f.getString(R.string.retry_later));
                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                        break;
                    }
                    default: {
                        //Tutti i codici di errore non gestiti sopra andranno a finire qui...
                        dialog.setTitle(R.string.unknown_error);
                        dialog.setMessage(f.getString(R.string.unknown_error_message));
                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                    }
                }
                dialog.show();
                Looper.loop();
                Looper.myLooper().quitSafely();
            }
        });
    }
}
