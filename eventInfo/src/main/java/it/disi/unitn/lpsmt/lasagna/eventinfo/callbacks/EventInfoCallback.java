package it.disi.unitn.lpsmt.lasagna.eventinfo.callbacks;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import it.disi.unitn.lpsmt.lasagna.eventinfo.EventDetailsViewModel;
import it.disi.unitn.lpsmt.lasagna.eventinfo.EventInfo;

public class EventInfoCallback {
    private final Fragment f;
    private final EventDetailsViewModel eventVM;

    public EventInfoCallback(@NotNull Fragment f, @NotNull EventDetailsViewModel eventVM) {
        this.f = f;
        this.eventVM = eventVM;
    }

    public void handleInfoSuccess(@NotNull JsonObject data) {
        Activity activity = f.getActivity();
        if (activity != null && f.isAdded()) {
            eventVM.setEventInfo(EventInfo.parseJSON(data));
        }
    }
}