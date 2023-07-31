package it.disi.unitn.lpsmt.lasagna.user_event_registration;

import android.content.Intent;
import android.util.Log;
import android.util.Pair;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.lasagna.network.NetworkRequest;
import it.disi.unitn.lpsmt.lasagna.network.networkOps.ServerOperation;
import it.disi.unitn.lpsmt.lasagna.eventinfo.EventDetailsFragment;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class UserEventRegistration extends ServerOperation {

    private final NetworkRequest request;

    private final EventDetailsFragment f;

    private final String accessToken, eventId, day, time;

    private final ActivityResultLauncher<Intent> launcher;

    public UserEventRegistration(@NonNull String accessToken, @NonNull String eventId, @NonNull String day,
                                 @NonNull String time, @NonNull EventDetailsFragment f,
                                 @Nullable ActivityResultLauncher<Intent> launcher) {
        request = getNetworkRequest();
        this.f = f;
        this.accessToken = accessToken;
        this.eventId = eventId;
        this.day = day;
        this.time = time;
        this.launcher = launcher;
    }

    public void run() {
        Log.i("day", day);
        Log.i("time", time);

        List<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<>("x-access-token", accessToken));
        RequestBody body = new FormBody.Builder()
                .add("data", day)
                .add("ora", time)
                .build();
        Request req = request.getPostRequest(body, headers,
                getBaseUrl() + "/api/v2/EventiPubblici/" + eventId + "/Iscrizioni");
        request.enqueue(req, new UserEventRegistrationCallback(f, launcher));
    }
}
