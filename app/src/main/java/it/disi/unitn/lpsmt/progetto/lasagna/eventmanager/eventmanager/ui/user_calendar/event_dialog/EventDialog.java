package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_calendar.event_dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;

import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.privateEvents.PrivateEvents;

public class EventDialog extends Dialog {
    private int d, m;
    private List<PrivateEvents> privEv;

    public EventDialog(Context c, int d, int m/*, List<PrivateEvents> privEv*/) {
        super(c);
        this.d = d;
        this.m = m;
        this.setContentView(R.layout.fragment_calendar_dialog);
        setTitle(c.getString(R.string.dialog_day, d, m)); //Perché non riesco a visualizzare il giorno a schermo?
        //this.privEv.addAll(privEv);
    }

    public void showDialog() {
        show();
    }

    public void onDismiss(@NonNull DialogInterface dialog) {

    }
}
