package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.event_creation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EventCreationCallback implements Callback {

    private final Fragment f;

    private final ActivityResultLauncher<Intent> i;

    private final Intent loginIntent;

    public EventCreationCallback(@NotNull Fragment f, @NotNull ActivityResultLauncher<Intent> i, @NotNull Intent lintent) {
        this.f = f;
        this.i = i;
        loginIntent = lintent;
    }

    private void setAlertDialog(@StringRes int title, @StringRes int message) {
        Activity activity = f.getActivity();
        if(activity != null && f.isAdded()) {
            f.requireActivity().runOnUiThread(() -> {
                AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
                dialog.setTitle(title);
                dialog.setMessage(f.getString(message));
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> {
                    dialog1.dismiss();
                    f.requireActivity().finish();
                });
                dialog.show();
            });
        }
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        try {
            throw e;
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        switch (response.code()) {
            case 201: {
                setAlertDialog(R.string.event_creation_ok_title, R.string.event_creation_ok_message);
                break;
            }

            case 400: {
                setAlertDialog(R.string.event_creation_error, R.string.event_creation_error_message);
                break;
            }

            case 401: {
                i.launch(loginIntent);
                break;
            }

            case 500: {
                setAlertDialog(R.string.internal_server_error, R.string.retry_later);
                break;
            }

            case 503: {
                setAlertDialog(R.string.service_unavailable, R.string.service_unavailable_message);
                break;
            }
        }
    }
}
