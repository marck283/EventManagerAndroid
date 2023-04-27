package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.publicEvent;

import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.NetworkRequest;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.networkOps.ServerOperation;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.EventDetailsFragment;
import okhttp3.Request;

public class EventInfoCall extends ServerOperation {

    private final NetworkRequest request;

    private final String eventId;

    private final View v;

    private final EventDetailsFragment f;

    public EventInfoCall(@NonNull String eventId, @NonNull View v, @NonNull EventDetailsFragment f) {
        request = getNetworkRequest();
        this.eventId = eventId;
        this.v = v;
        this.f = f;
    }

    public void run() {
        Request req = request.getRequest(new ArrayList<>(), getBaseUrl() + "/api/v2/EventiPubblici/" + eventId);
        request.enqueue(req, new EventInfoCallback(f, v));
    }
}
