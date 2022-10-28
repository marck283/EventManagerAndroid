package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_list;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModel;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents.PublicEvents;

public class EventListViewModel extends ViewModel {

    private PublicEvents pubEv;

    public void getEvents(@NonNull View layout, String accessToken) {
        pubEv = new PublicEvents(layout);
        pubEv.getEvents((ConstraintLayout) layout, accessToken, null, null, null, null, null);
    }
}