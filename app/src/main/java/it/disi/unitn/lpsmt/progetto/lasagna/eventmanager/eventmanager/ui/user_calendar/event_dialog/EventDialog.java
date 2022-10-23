package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_calendar.event_dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.jetbrains.annotations.Contract;

import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.Event;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.privateEvents.PrivateEvents;

public class EventDialog extends Dialog {
    private int d, m, y;
    private List<Event> pubEvList;

    public EventDialog(Context c, int d, int m, int y) {
        super(c);
        this.d = d;
        this.m = m;
        this.y = y;
        this.setContentView(R.layout.user_calendar_dialog);
        setTitle(c.getString(R.string.dialog_day, d, m));
    }

    @NonNull
    @Contract(pure = true)
    private String padStart(@NonNull String s) {
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
        show();
    }
}
