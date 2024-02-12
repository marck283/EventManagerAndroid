package it.disi.unitn.lpsmt.lasagna.eventinfo.publicEvent;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import it.disi.unitn.lpsmt.lasagna.network.NetworkRequest;
import it.disi.unitn.lpsmt.lasagna.network.networkOps.ServerOperation;
import okhttp3.Request;

public class EventInfoCall extends ServerOperation {

    private final NetworkRequest request;

    private final String eventId;

    private final View v;

    private final Fragment f;

    private final int regClosed;

    public EventInfoCall(@NonNull String eventId, @NonNull View v, @NonNull Fragment f, @StringRes int regClosed) {
        request = getNetworkRequest();
        this.eventId = eventId;
        this.v = v;
        this.f = f;
        this.regClosed = regClosed;
    }

    public void run() {
        Request req = request.getRequest(new ArrayList<>(), getBaseUrl() + "/api/v2/EventiPubblici/" + eventId);
        request.enqueue(req, new EventInfoCallback(f, v, regClosed));
    }
}
