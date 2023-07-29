package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.registeredEvent;

import android.content.Intent;
import android.util.Pair;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.lasagna.network.NetworkRequest;
import it.disi.unitn.lpsmt.lasagna.network.networkOps.ServerOperation;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.EventDetailsFragment;
import okhttp3.Request;

public class RegisteredEventInfo extends ServerOperation {
    private final NetworkRequest request;

    private final String userJwt, eventId, data;

    private final EventDetailsFragment f;

    private final View v;

    private final ActivityResultLauncher<Intent> loginLauncher;

    public RegisteredEventInfo(@NonNull String userJwt, @NonNull String eventId, @NonNull EventDetailsFragment f,
                               @NonNull View v, @NonNull String data,
                               @NonNull ActivityResultLauncher<Intent> loginLauncher) {
        request = getNetworkRequest();
        this.userJwt = userJwt;
        this.eventId = eventId;
        this.f = f;
        this.v = v;
        this.data = data;
        this.loginLauncher = loginLauncher;
    }

    public void run() {
        List<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<>("x-access-token", userJwt));
        headers.add(new Pair<>("data", data));
        Request req = request.getRequest(headers, getBaseUrl() + "/api/v2/InfoEventoIscr/" + eventId);
        request.enqueue(req, new RegisteredEventCallback(f, v, userJwt, eventId, loginLauncher));
    }
}
