package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.registeredEvent.post_review;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.NetworkRequest;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostReview extends Thread {
    private final String userId, eventId, titolo, motivazione;
    private final float valutazione;

    private final NetworkRequest request;

    private final Fragment f;

    private final View v;

    public PostReview(@NonNull String userId, @NonNull String eventId, @NonNull String titolo,
                      @NonNull String motivazione, float valutazione, @NonNull Fragment f, @NonNull View v) {
        request = new NetworkRequest();
        this.userId = userId;
        this.eventId = eventId;
        this.titolo = titolo;
        this.motivazione = motivazione;
        this.valutazione = valutazione;
        this.f = f;
        this.v = v;
    }

    private void setAlertDialog(@StringRes int title, @StringRes int message,
                                @NonNull DialogInterface.OnClickListener click) {
        f.requireActivity().runOnUiThread(() -> {
            AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
            dialog.setTitle(title);
            dialog.setMessage(f.getString(message));
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", click);
            dialog.show();
        });
    }

    public void run() {
        //La valutazione deve essere espressa in decimi.
        RequestBody body = new FormBody.Builder().add("title", titolo)
                .add("evaluation", String.valueOf(2 * valutazione)).add("description", motivazione).build();
        List<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<>("x-access-token", userId));
        Request req = request.getPostRequest(body, headers,
                "https://eventmanagerzlf.herokuapp.com/api/v2/Recensioni/" + eventId);
        request.enqueue(req, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                try {
                    throw e;
                } catch(Throwable e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                switch (response.code()) {
                    case 201: {
                        setAlertDialog(R.string.review_creation_successful, R.string.review_creation_successful_message,
                                (dialog1, which) -> {
                                    dialog1.dismiss();
                                    Navigation.findNavController(v).navigate(R.id.action_reviewWriting_to_nav_user_calendar);
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
                        setAlertDialog(R.string.malformed_request, R.string.malformed_request_message,
                                (dialog1, which) -> dialog1.dismiss());
                        break;
                    }
                    case 500: {
                        //Secondo dei due codici usati solo per il debug.
                        setAlertDialog(R.string.internal_server_error, R.string.retry_later,
                                (dialog1, which) -> dialog1.dismiss());
                        break;
                    }
                    default: {
                        //Tutti i codici di errore non gestiti sopra andranno a finire qui...
                        setAlertDialog(R.string.unknown_error, R.string.unknown_error_message,
                                (dialog1, which) -> dialog1.dismiss());
                    }
                }
            }
        });
    }
}
