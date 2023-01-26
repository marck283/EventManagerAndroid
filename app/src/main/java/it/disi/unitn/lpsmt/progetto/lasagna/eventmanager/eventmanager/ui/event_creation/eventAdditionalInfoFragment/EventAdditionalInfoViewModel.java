package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.eventAdditionalInfoFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.event_creation.EventCreation;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.EventViewModel;

public class EventAdditionalInfoViewModel extends ViewModel {
    public void createPrivateEvent(@NonNull Fragment f, @NonNull String jwt, @NonNull EventViewModel evm) {
        EventCreation creation = new EventCreation(f, jwt, evm);
        creation.start();
    }
}
