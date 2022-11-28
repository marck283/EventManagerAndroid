package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.user_event_registration;

import androidx.annotation.NonNull;

public class EventDayHour {
    @NonNull
    private final String data, ora;

    public EventDayHour(@NonNull String day, @NonNull String hour) {
        this.data = day;
        this.ora = hour;
    }

    @NonNull
    public String getDay() {
        return data;
    }

    @NonNull
    public String getHour() {
        return ora;
    }
}
