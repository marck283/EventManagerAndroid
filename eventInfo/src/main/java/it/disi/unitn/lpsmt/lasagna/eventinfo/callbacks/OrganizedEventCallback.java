package it.disi.unitn.lpsmt.lasagna.eventinfo.callbacks;

import android.app.Activity;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import it.disi.unitn.lpsmt.lasagna.eventinfo.EventDetailsViewModel;
import it.disi.unitn.lpsmt.lasagna.eventinfo.R;
import it.disi.unitn.lpsmt.lasagna.eventinfo.fields.Field;
import it.disi.unitn.lpsmt.lasagna.eventinfo.organizedEvent.OrganizedEvent;

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
        OrganizedEvent event = OrganizedEvent.parseJSON(body);

        Activity activity = f.getActivity();
        if (activity != null && f.isAdded()) {
            Field eventPic = new Field();
            eventPic.setFieldId("eventPicture");
            eventPic.setFieldValue(event.getEventPic());
            eventVM.setField(eventPic);

            Field title = new Field();
            title.setFieldId("title");
            title.setFieldValue(event.getEventName());
            eventVM.setField(title);

            Field duration = new Field();
            if (event.getDurata() == null || event.getDurata().isEmpty()) {
                duration.setFieldValue(f.getString(R.string.duration, "0", "0", "0"));
            } else {
                String[] durataArr = event.getDurata().split(":");
                duration.setFieldValue(f.getString(R.string.duration, durataArr[0], durataArr[1], durataArr[2]));
            }
            duration.setFieldId("duration");
            eventVM.setField(duration);

            eventVM.setOrganizedEvent(event);
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
            }
        }
    }
}