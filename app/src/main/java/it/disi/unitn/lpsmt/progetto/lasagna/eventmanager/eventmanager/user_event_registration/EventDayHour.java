package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.user_event_registration;

import androidx.annotation.NonNull;

public class EventDayHour {
    private String day, hour;

    public EventDayHour(@NonNull String day, @NonNull String hour) {
        this.day = day;
        this.hour = hour;
    }
}
