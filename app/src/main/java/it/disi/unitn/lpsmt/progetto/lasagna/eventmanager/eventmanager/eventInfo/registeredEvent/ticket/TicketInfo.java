package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.registeredEvent.ticket;

import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.lasagna.network.NetworkRequest;
import it.disi.unitn.lpsmt.lasagna.network.networkOps.ServerOperation;
import okhttp3.Request;

public class TicketInfo extends ServerOperation {
    private final String eventId, userId, data, ora;
    private final View v;

    private final Fragment f;

    private final NetworkRequest request;

    public TicketInfo(@NonNull Fragment f, @NonNull View v, @NonNull String eventId,
                      @NonNull String userId, @NonNull String data, @NonNull String ora) {
        request = getNetworkRequest();
        this.eventId = eventId;
        this.userId = userId;
        this.data = data;
        this.ora = ora;
        this.v = v;
        this.f = f;
    }

    public void run() {
        List<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<>("x-access-token", userId));
        headers.add(new Pair<>("giorno", data));
        headers.add(new Pair<>("ora", ora));
        Request req = request.getRequest(headers, getBaseUrl() + "/api/v2/ticket/" + eventId);
        request.enqueue(req, new TicketInfoCallback(f, v));
    }
}
