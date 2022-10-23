package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_calendar.event_dialog;

import android.app.AlertDialog;

import androidx.annotation.NonNull;

import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.Event;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.privateEvents.PrivateEvents;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_calendar.UserCalendarFragment;

public class EventDialog {
    private AlertDialog d;
    private int d1, m, y;
    private List<Event> pubEvList;

    public EventDialog(@NonNull UserCalendarFragment f, int d, int m, int y) {
        d1 = d;
        this.m = m;
        this.y = y;
        this.d = new AlertDialog.Builder(f.getContext()).create();
    }

    private String padStart(String s) {
        if(s.length() < 2) {
            return "0" + s;
        }
        return s;
    }

    public void getEvents(String authToken) {
        PrivateEvents privEv = new PrivateEvents();
        privEv.getPersonalEvents(authToken, padStart(String.valueOf(m)) + "-" + padStart(String.valueOf(d)) + "-" + y);
    }

    public void showDialog() {
        d.show();
    }
}
