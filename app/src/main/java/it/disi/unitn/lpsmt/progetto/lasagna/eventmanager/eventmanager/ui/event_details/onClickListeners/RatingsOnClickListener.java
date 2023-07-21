package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.onClickListeners;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.Navigation;

import org.jetbrains.annotations.NotNull;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;

public class RatingsOnClickListener implements View.OnClickListener {

    private final String screenType, eventId;

    public RatingsOnClickListener(@NotNull String screenType, @NotNull String eventId) {
        if(screenType.equals("") || eventId.equals("")) {
            throw new IllegalArgumentException("Nessuno degli argomenti forniti al costruttore puo' essere null.");
        }

        this.screenType = screenType;
        this.eventId = eventId;
    }

    @Override
    public void onClick(View view) {
        Bundle b1 = new Bundle();
        b1.putString("screenType", screenType);
        b1.putString("eventId", eventId);
        Navigation.findNavController(view).navigate(R.id.action_eventDetailsFragment_to_reviewsFragment, b1);
    }
}
