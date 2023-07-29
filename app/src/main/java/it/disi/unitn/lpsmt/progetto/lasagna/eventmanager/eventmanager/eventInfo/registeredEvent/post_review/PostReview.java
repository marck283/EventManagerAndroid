package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.registeredEvent.post_review;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Pair;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.lasagna.network.NetworkRequest;
import it.disi.unitn.lpsmt.lasagna.network.networkOps.ServerOperation;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostReview extends ServerOperation {
    private final String userId, eventId, titolo, motivazione;
    private final float valutazione;

    private final NetworkRequest request;

    private final Fragment f;

    private final View v;

    private final ActivityResultLauncher<Intent> loginLauncher;

    public PostReview(@NonNull String userId, @NonNull String eventId, @NonNull String titolo,
                      @NonNull String motivazione, float valutazione, @NonNull Fragment f,
                      @NonNull View v, @NotNull ActivityResultLauncher<Intent> loginLauncher) {
        request = getNetworkRequest();
        this.userId = userId;
        this.eventId = eventId;
        this.titolo = titolo;
        this.motivazione = motivazione;
        this.valutazione = valutazione;
        this.f = f;
        this.v = v;
        this.loginLauncher = loginLauncher;
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
                getBaseUrl() + "/api/v2/Recensioni/" + eventId);
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
                    case 201 -> setAlertDialog(R.string.review_creation_successful, R.string.review_creation_successful_message,
                            (dialog1, which) -> {
                                dialog1.dismiss();
                                Navigation.findNavController(v).navigate(R.id.action_reviewWriting_to_nav_user_calendar);
                            });
                    case 401 -> {
                        Intent loginIntent = new Intent(f.requireContext(), LoginActivity.class);
                        loginLauncher.launch(loginIntent);
                    }
                    case 400 -> // Codice di ritorno utilizzato solo per il debug.
                        // Non dovrebbero esserci problemi di questo tipo durante l'utilizzo normale
                        // dell'applicazione.
                            setAlertDialog(R.string.malformed_request, R.string.malformed_request_message,
                                    (dialog1, which) -> dialog1.dismiss());
                    case 500 -> setAlertDialog(R.string.internal_server_error, R.string.retry_later,
                            (dialog1, which) -> dialog1.dismiss());
                    default -> //Tutti i codici di errore non gestiti sopra andranno a finire qui...
                            setAlertDialog(R.string.unknown_error, R.string.unknown_error_message,
                                    (dialog1, which) -> dialog1.dismiss());
                }
            }
        });
    }
}
