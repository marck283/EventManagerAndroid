package it.disi.unitn.lpsmt.lasagna.eventinfo.publicEvent;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import it.disi.unitn.lpsmt.lasagna.eventinfo.EventDetailsViewModel;
import it.disi.unitn.lpsmt.lasagna.eventinfo.R;
import it.disi.unitn.lpsmt.lasagna.eventinfo.fields.Field;

public class EventInfoCallback {
    private final Fragment f;
    private final EventDetailsViewModel eventVM;

    public EventInfoCallback(@NotNull Fragment f, @NotNull EventDetailsViewModel eventVM) {
        this.f = f;
        this.eventVM = eventVM;
    }

    public void handleInfoSuccess(@NotNull JsonObject data) {
        EventInfo ei = new EventInfo();
        final EventInfo ei1 = ei.parseJSON(data);

        Activity activity = f.getActivity();
        if (activity != null && f.isAdded()) {
            Field eventPic = new Field();
            eventPic.setFieldId("eventPicture");
            eventPic.setFieldValue(ei1.getEventPic());
            eventVM.setField(eventPic);

            Field title = new Field();
            title.setFieldId("title");
            title.setFieldValue(ei1.getNomeAtt());
            eventVM.setField(title);

            Field organizer = new Field();
            organizer.setFieldId("organizer");
            organizer.setFieldValue(f.getString(R.string.organizer, ei1.getOrgName()));
            eventVM.setField(organizer);

            Field duration = new Field();
            String[] durataArr = ei1.getDurata().split(":");
            duration.setFieldId("duration");
            duration.setFieldValue(f.getString(R.string.duration, durataArr[0], durataArr[1], durataArr[2]));
            eventVM.setField(duration);

            eventVM.setPublicEventInfo(ei1);
        }
    }
}