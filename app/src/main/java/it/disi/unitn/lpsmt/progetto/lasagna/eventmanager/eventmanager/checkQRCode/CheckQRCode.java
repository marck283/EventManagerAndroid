package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.checkQRCode;

import android.app.AlertDialog;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import java.io.IOException;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CheckQRCode extends Thread {
    private final OkHttpClient client;
    private final String userJwt, qrCode, eventid, day, hour;

    private final Fragment f;

    public CheckQRCode(@NonNull String userJwt, @NonNull String qrCode, @NonNull String eventid,
                       @NonNull String day, @NonNull String hour, @NonNull Fragment f) {
        this.userJwt = userJwt;
        this.qrCode = qrCode;
        client = new OkHttpClient();
        this.f = f;
        this.eventid = eventid;
        this.day = day;
        this.hour = hour;
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
                .addHeader("x-access-token", userJwt)
                .addHeader("eventoid", eventid)
                .addHeader("day", day)
                .addHeader("hour", hour)
                .url("https://eventmanagerzlf.herokuapp.com/api/v2/QRCodeCheck/" + qrCode)
                .build();
        Log.i("code", qrCode);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //Nulla qui...
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                switch (response.code()) {
                    case 200: {
                        //OK
                        setAlertDialog(R.string.valid_qr_code, R.string.valid_qr_code_message);
                        break;
                    }
                    case 400: {
                        //Richiesta malformata
                        setAlertDialog(R.string.malformed_request, R.string.malformed_request_message);
                        break;
                    }
                    case 401: {
                        //Utente non autenticato
                        setAlertDialog(R.string.user_not_logged_in, R.string.user_not_logged_in_message);
                        break;
                    }
                    case 404: {
                        //QR Code non valido
                        setAlertDialog(R.string.qr_code_invalid, R.string.invalid_qr_code_message);
                        break;
                    }
                }
            }
        });
    }
}
