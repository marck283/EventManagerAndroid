package it.disi.unitn.lpsmt.lasagna.checkqrcode;

import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InvalidObjectException;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class QRCodeCallback implements Callback {

    private final Fragment f;

    public QRCodeCallback(@NotNull Fragment f) throws InvalidObjectException {
        this.f = f;
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
            case 200 -> //OK
                    setAlertDialog(R.string.valid_qr_code, R.string.valid_qr_code_message);
            case 400 -> //Richiesta malformata
                    setAlertDialog(R.string.malformed_request, R.string.malformed_request_message);
            case 401 -> //Utente non autenticato
                    setAlertDialog(R.string.user_not_logged_in, R.string.user_not_logged_in_message);
            case 404 -> //QR Code non valido
                    setAlertDialog(R.string.qr_code_invalid, R.string.invalid_qr_code_message);
        }
    }
}
