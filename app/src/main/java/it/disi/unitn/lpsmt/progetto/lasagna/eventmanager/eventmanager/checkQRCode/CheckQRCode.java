package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.checkQRCode;

import android.app.AlertDialog;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.NetworkRequest;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.networkOps.ServerOperation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class CheckQRCode extends ServerOperation {

    private final NetworkRequest request;
    private final String userJwt, qrCode, eventid, day, hour;

    private final Fragment f;

    public CheckQRCode(@NonNull String userJwt, @NonNull String qrCode, @NonNull String eventid,
                       @NonNull String day, @NonNull String hour, @NonNull Fragment f) {
        this.userJwt = userJwt;
        this.qrCode = qrCode;
        request = getNetworkRequest();
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
        List<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<>("x-access-token", userJwt));
        headers.add(new Pair<>("eventoid", eventid));
        headers.add(new Pair<>("day", day));
        headers.add(new Pair<>("hour", hour));
        Request req = request.getRequest(headers,
                getBaseUrl() + "/api/v2/QRCodeCheck/" + qrCode);
        Log.i("code", qrCode);
        request.enqueue(req, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                try {
                    throw e;
                } catch(IOException ex) {
                    ex.printStackTrace();
                }
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
