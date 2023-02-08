package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.organizedEvent;

import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.callbacks.OrganizerCallback;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TerminateEvent extends Thread {
    private final OkHttpClient client;

    private final String eventId, accessToken, data, ora;

    private final View v;

    private final Fragment f;

    public TerminateEvent(@NonNull String accessToken, @NonNull String eventId, @NonNull String data,
                          @NonNull String ora, @NonNull View v, @NonNull Fragment f) {
        client = new OkHttpClient();
        this.eventId = eventId;
        this.data = data;
        this.ora = ora;
        this.v = v;
        this.f = f;
        this.accessToken = accessToken;
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
        RequestBody body = new FormBody.Builder()
                .add("data", data)
                .add("ora", ora)
                .build();
        Request request = new Request.Builder()
                .addHeader("x-access-token", accessToken)
                .url("https://eventmanagerzlf.herokuapp.com/api/v2/terminaEvento/" + eventId)
                .patch(body)
                .build();
        client.newCall(request).enqueue(new OrganizerCallback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                switch (response.code()) {
                    case 400: {
                        setAlertDialog(R.string.malformed_request, R.string.malformed_request_message);
                        break;
                    }
                    case 200: {
                        setAlertDialog(R.string.attempt_ok, R.string.attempt_ok_message);

                        Button qrCodeScan = v.findViewById(R.id.button8), terminaEvento = v.findViewById(R.id.button12);
                        qrCodeScan.setEnabled(false);
                        terminaEvento.setEnabled(false);
                        break;
                    }
                    case 500: {
                        setAlertDialog(R.string.internal_server_error, R.string.internal_server_error);
                        break;
                    }
                }
            }
        });
    }
}
