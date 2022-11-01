package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_calendar.event_dialog;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.Contract;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.privateEvents.PrivateEvents;

public class EventDialogViewModel extends ViewModel {
    @NonNull
    @Contract(pure = true)
    private String padStart(@NonNull String s) {
        if(s.length() < 2) {
            return "0" + s;
        }
        return s;
    }

    public void getEvents(String authToken, int d, int m, int y, ConstraintLayout l) {
        PrivateEvents privEv = new PrivateEvents(l);
        privEv.getPersonalEvents(authToken, padStart(String.valueOf(m)) + "-" + padStart(String.valueOf(y)) + "-" + d, l);
    }
}
