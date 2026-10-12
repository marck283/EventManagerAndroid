package it.disi.unitn.lpsmt.lasagna.eventinfo.registeredEvent;

import android.app.Activity;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import it.disi.unitn.lpsmt.lasagna.eventinfo.R;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import it.disi.unitn.lpsmt.lasagna.eventinfo.EventDetailsViewModel;

public class RegisteredEventCallback {

    private final Fragment f;

    private final ActivityResultLauncher<Intent> loginLauncher;

    private final EventDetailsViewModel eventVM;

    private final Class<? extends Activity> c;

    public RegisteredEventCallback(@NotNull Fragment f,
                                   @NotNull ActivityResultLauncher<Intent> loginLauncher,
                                   @NotNull EventDetailsViewModel eventVM,
                                   @NotNull Class<? extends Activity> c) {
        this.f = f;
        this.loginLauncher = loginLauncher;
        this.eventVM = eventVM;
        this.c = c;
    }

    public void handleInfoSuccess(@NonNull JsonObject data) {
        RegisteredEvent event = RegisteredEvent.parseJSON(data);

        Activity activity = f.getActivity();
        if (activity != null && f.isAdded()) {
            eventVM.setEventInfo(event);
        }
    }

    public void handleInfoError(int statusCode) {
        switch(statusCode) {
            case 400 -> eventVM.showDialog(R.string.malformed_request, R.string.malformed_request_message);
            case 401 -> {
                Activity activity = f.getActivity();
                if (activity != null && !activity.isFinishing() && !activity.isDestroyed() && f.isAdded()) {
                    activity.runOnUiThread(() -> {
                        if (!activity.isFinishing() && !activity.isDestroyed()) {
                            Intent loginIntent = new Intent(activity, c);
                            loginLauncher.launch(loginIntent);
                        }
                    });
                }
            }
            case 404 -> eventVM.showDialog(R.string.no_event, R.string.no_event_message);
        }
    }
}
