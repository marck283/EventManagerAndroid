package it.disi.unitn.lpsmt.lasagna.eventinfo.interfaces;

import androidx.annotation.NonNull;

public interface EventDetailsInterface {
    void setEventId(@NonNull String val);
    void setDay(@NonNull String day);
    void setTime(@NonNull String time);
}
