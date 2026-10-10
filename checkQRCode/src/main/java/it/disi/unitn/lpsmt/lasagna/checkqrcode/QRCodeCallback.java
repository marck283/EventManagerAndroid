package it.disi.unitn.lpsmt.lasagna.checkqrcode;

import android.app.AlertDialog;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.io.InvalidObjectException;

public class QRCodeCallback {

    private final Fragment f;

    private final int validQRCT, validQRCMsg, invalid_qr_code, invalid_qr_code_message,
            malformed_request, malformed_request_message, no_session_title, no_session_message;

    public QRCodeCallback(@NotNull Fragment f, @StringRes int validQRCodeTitle,
                          @StringRes int validQRCodeMsg, @StringRes int invalid_qr_code,
                          @StringRes int invalid_qr_code_message, @StringRes int malformed_request,
                          @StringRes int malformed_request_message, @StringRes int no_session_title,
                          @StringRes int no_session_message) throws InvalidObjectException {
        this.f = f;
        validQRCT = validQRCodeTitle;
        validQRCMsg = validQRCodeMsg;
        this.invalid_qr_code = invalid_qr_code;
        this.invalid_qr_code_message = invalid_qr_code_message;
        this.malformed_request = malformed_request;
        this.malformed_request_message = malformed_request_message;
        this.no_session_title = no_session_title;
        this.no_session_message = no_session_message;
    }

    private void setAlertDialog(@StringRes int title, @StringRes int message) {
        android.app.Activity activity = f.getActivity();
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed() && f.isAdded()) {
            activity.runOnUiThread(() -> {
                if (!activity.isFinishing() && !activity.isDestroyed() && f.isAdded()) {
                    AlertDialog dialog = new AlertDialog.Builder(activity).create();
                    dialog.setTitle(title);
                    dialog.setMessage(f.getString(message));
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                    dialog.show();
                }
            });
        }
    }

    public void handleResponseCode(int statusCode) {
        switch (statusCode) {
            case 200 -> // OK
                    setAlertDialog(validQRCT, validQRCMsg);
            case 400 -> // Richiesta malformata
                    setAlertDialog(malformed_request, malformed_request_message);
            case 401 -> // Utente non autenticato
                    setAlertDialog(no_session_title, no_session_message);
            case 404 -> // QR Code non valido
                    setAlertDialog(invalid_qr_code, invalid_qr_code_message);
        }
    }
}
