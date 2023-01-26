package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.event_restrictions;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.event_creation.EventCreation;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.EventViewModel;

public class EventRestrictionsViewModel extends ViewModel {
    public void createPublicEvent(@NonNull Fragment f, @NonNull String userJwt, @NonNull EventViewModel evm) {
        if(!userJwt.equals("")) {
            Log.i("jwt", userJwt);
            EventCreation creation = new EventCreation(f, userJwt, evm);
            creation.start();
        }
    }
}