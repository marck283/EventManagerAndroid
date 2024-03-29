package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_calendar.event_dialog;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.Contract;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.organizedEvents.OrganizedEvents;
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

    public void getEvents(@NonNull Fragment f, String authToken, int d, int m, int y, ConstraintLayout l) {
        String data = padStart(String.valueOf(m)) + "-" + padStart(String.valueOf(y)) + "-" + d;

        //Parallelizzo le chiamate per ottenere gli eventi organizzati dall'utente che sta visualizzando il calendario
        // e quelli a cui partecipa.
        OrganizedEvents orgEv = new OrganizedEvents(R.id.organizer_recycler_view, f, l, null, authToken, data);
        //orgEv.getOrgEvents(authToken, data);
        orgEv.start();

        PrivateEvents privEv = new PrivateEvents(l, authToken, data);
        privEv.start();
    }
}
