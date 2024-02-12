package it.disi.unitn.lpsmt.lasagna.user_event_registration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserEventRegistrationCallback implements Callback {

    private final Fragment f;

    private final ActivityResultLauncher<Intent> launcher;

    private final Class<? extends Activity> c;

    public UserEventRegistrationCallback(@NotNull Fragment f, @NotNull ActivityResultLauncher<Intent> launcher,
                                         @NotNull Class<? extends Activity> c) {
        this.f = f;
        this.launcher = launcher;
        this.c = c;
    }

    private void setAlertDialog(@StringRes int title, @StringRes int message) {
        Activity activity = f.getActivity();
        if(activity != null && f.isAdded()) {
            f.requireActivity().runOnUiThread(() -> {
                AlertDialog ad = new AlertDialog.Builder(f.requireActivity()).create();
                ad.setTitle(title);
                ad.setMessage(f.getString(message));
                ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                ad.show();
            });
        }
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        try {
            throw e;
        } catch (Throwable e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        switch (response.code()) {
            case 201 -> //Successo
                    setAlertDialog(R.string.event_registration_success_title, R.string.event_registration_success);
            case 400 -> //Richiesta malformata
                    setAlertDialog(R.string.malformed_request, R.string.malformed_request_message);
            case 401 -> {
                Activity activity = f.getActivity();
                if (activity != null && f.isAdded()) {
                    Intent loginIntent = new Intent(f.requireActivity(), c);
                    launcher.launch(loginIntent);
                } else {
                    setAlertDialog(R.string.user_not_logged_in, R.string.user_not_logged_in_message);
                }
            }
            case 403, 404 -> setAlertDialog(R.string.error, R.string.max_pers_reached_or_user_already_registered);
            case 500 -> //Errore interno al server
                    setAlertDialog(R.string.internal_server_error, R.string.service_unavailable);
            case 503 -> setAlertDialog(R.string.request_timeout, R.string.request_timeout_message);
            default -> //Errore sconosciuto
                    setAlertDialog(R.string.unknown_error, R.string.unknown_error_message);
        }
    }
}
