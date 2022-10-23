package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_list;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.Event;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents.PublicEvents;

public class EventListViewModel extends ViewModel {

    private MutableLiveData<List<Event>> peList;
    private PublicEvents pubEv;

    public EventListViewModel() {
        peList = new MutableLiveData<>();
        peList.setValue(new ArrayList<>());
    }

    public void getEvents(@NonNull View layout, String accessToken) {
        pubEv = new PublicEvents(layout);
        pubEv.getEvents((ConstraintLayout) layout, accessToken, null, null, null, null, null);
    }
}