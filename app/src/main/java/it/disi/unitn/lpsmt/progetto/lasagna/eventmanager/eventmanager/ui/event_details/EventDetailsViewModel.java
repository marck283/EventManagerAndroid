package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.EventInfoCall;

public class EventDetailsViewModel extends ViewModel {
    public void getEventInfo(@NonNull String which, @NonNull String eventId, @NonNull View view, @NonNull Fragment f) {
        EventInfoCall c = new EventInfoCall();
        switch(which) {
            case "pub": {
                c.getEventInfo("pub", eventId, view, f);
                break;
            }
            case "iscr": {
                //Qualcosa
            }
            case "org": {
                //Qualcosa
            }
        }
    }
}