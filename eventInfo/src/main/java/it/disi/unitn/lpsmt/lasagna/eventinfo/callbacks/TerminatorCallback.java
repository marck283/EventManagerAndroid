package it.disi.unitn.lpsmt.lasagna.eventinfo.callbacks;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import okhttp3.Call;
import okhttp3.Response;

public class TerminatorCallback extends OrganizerCallback {

    private final Fragment f;

    private final ActivityResultLauncher<Intent> launcher;

    private final Intent loginIntent;

    private final View v;

    private final int malformed_request, malformed_request_message, no_session_title, no_session_content,
    attempt_ok, attempt_ok_message, button8, button12, internal_server_error;

    public TerminatorCallback(@NotNull Fragment f, @NotNull ActivityResultLauncher<Intent> l,
                              @NotNull Intent lintent, @NotNull View v, @StringRes int malformed_req,
                              @StringRes int malf_req_msg, @StringRes int no_session_title,
                              @StringRes int no_session_content, @StringRes int attempt_ok,
                              @StringRes int attempt_ok_msg, @IdRes int but8, @IdRes int but12,
                              @StringRes int internal_server_error) {
        this.f = f;
        launcher = l;
        loginIntent = lintent;
        this.v = v;
        malformed_request = malformed_req;
        malformed_request_message = malf_req_msg;
        this.no_session_title = no_session_title;
        this.no_session_content = no_session_content;
        this.attempt_ok = attempt_ok;
        attempt_ok_message = attempt_ok_msg;
        button8 = but8;
        button12 = but12;
        this.internal_server_error = internal_server_error;
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
    public void onResponse(@NonNull Call call, @NonNull Response response) {
        switch (response.code()) {
            case 400 -> setAlertDialog(malformed_request, malformed_request_message);
            case 401 -> {
                setAlertDialog(no_session_title, no_session_content);
                launcher.launch(loginIntent);
            }
            case 200 -> {
                setAlertDialog(attempt_ok, attempt_ok_message);

                Button qrCodeScan = v.findViewById(button8), terminaEvento = v.findViewById(button12);
                qrCodeScan.setEnabled(false);
                terminaEvento.setEnabled(false);
            }
            case 500 -> setAlertDialog(internal_server_error, internal_server_error);
        }
    }
}
