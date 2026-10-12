package it.disi.unitn.lpsmt.lasagna.eventinfo.callbacks;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import it.disi.unitn.lpsmt.lasagna.eventinfo.EventDetailsViewModel;
import it.disi.unitn.lpsmt.lasagna.eventinfo.EventInfo;
import it.disi.unitn.lpsmt.lasagna.eventinfo.R;

public class OrganizedEventCallback {
    private final Fragment f;
    private final EventDetailsViewModel eventVM;
    private final ActivityResultLauncher<Intent> loginLauncher;
    private final Class<? extends Activity> c;

    public OrganizedEventCallback(@NotNull Fragment f, @NotNull EventDetailsViewModel eventVM,
                                  @NotNull ActivityResultLauncher<Intent> loginLauncher,
                                  @NotNull Class<? extends Activity> c) {
        this.f = f;
        this.eventVM = eventVM;
        this.loginLauncher = loginLauncher;
        this.c = c;
    }

    public void handleInfoSuccess(@NotNull JsonObject body) {
        EventInfo event = EventInfo.parseJSON(body);

        Activity activity = f.getActivity();
        if (activity != null && f.isAdded()) {
            Log.i("OrgEvCallback", "activity not null");
            eventVM.setEventInfo(event);
        } else {
            Log.i("OrgEvCallback", "activity null or Fragment not added");
        }
    }

    public void handleInfoError(int statusCode) {
        Activity activity = f.getActivity();
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed() && f.isAdded()) {
            switch (statusCode) {
                case 401 -> activity.runOnUiThread(() -> {
                    if (!activity.isFinishing() && !activity.isDestroyed()) {
                        Intent loginIntent = new Intent(activity, c);
                        loginLauncher.launch(loginIntent);
                    }
                });
                case 404 -> activity.runOnUiThread(() -> {
                    if (!activity.isFinishing() && !activity.isDestroyed()) {
                        eventVM.showDialog(R.string.event_not_found, R.string.event_not_found_msg);
                    }
                });
                default -> activity.runOnUiThread(() -> {
                    if (!activity.isFinishing() && !activity.isDestroyed()) {
                        eventVM.showDialog(R.string.unknown_error, R.string.unknown_error_message);
                    }
                });
            }
        }
    }
}