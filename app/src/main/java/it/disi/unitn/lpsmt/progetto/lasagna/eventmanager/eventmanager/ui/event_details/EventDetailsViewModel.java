package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.organizedEvent.OrganizedEventInfo;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.publicEvent.EventInfoCall;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.user_event_registration.UserEventRegistration;

public class EventDetailsViewModel extends ViewModel {
    public void getEventInfo(@NonNull String which, @NonNull String eventId, @NonNull View view,
                             @NonNull EventDetailsFragment f, @Nullable String userJwt) {
        EventInfoCall c = new EventInfoCall();
        switch(which) {
            case "pub": {
                c.getEventInfo("pub", eventId, view, f, null);
                break;
            }
            case "iscr": {
                //Qualcosa
            }
            case "org": {
                if(userJwt != null) {
                    OrganizedEventInfo orgEvInfo = new OrganizedEventInfo(view, f, userJwt, eventId);
                    orgEvInfo.start();
                }
                break;
            }
        }
    }

    public void registerUser(@NonNull String accessToken, @NonNull String eventId, @NonNull EventDetailsFragment f,
                             @NonNull String day, @NonNull String time) {
        UserEventRegistration uer = new UserEventRegistration();
        uer.registerUser(accessToken, eventId, day, time, f);
    }
}