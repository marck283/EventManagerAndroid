package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_list;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents.PublicEvents;

public class EventListViewModel extends ViewModel {

    public void getEvents(@NonNull View layout, String accessToken, @Nullable String nomeAtt, @Nullable String orgName) {
        PublicEvents pubEv = new PublicEvents(layout);
        pubEv.getEvents(accessToken, nomeAtt, null, null, null, null, orgName);
    }
}