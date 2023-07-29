package it.disi.unitn.lasagna.eventcreation;

import android.content.Intent;
import android.util.Pair;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.lasagna.network.NetworkRequest;
import it.disi.unitn.lpsmt.lasagna.network.networkOps.ServerOperation;
import it.disi.unitn.lasagna.eventcreation.viewmodel.EventViewModel;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class EventCreation extends ServerOperation {
    private final String userJwt;

    private final EventViewModel evm;

    private final NetworkRequest request;

    private final Fragment f;

    private final ActivityResultLauncher<Intent> i;
    private final Intent loginIntent;

    public EventCreation(@NonNull Fragment f, @NonNull String jwt, @NonNull EventViewModel evm) {
        userJwt = jwt;
        this.evm = evm;
        request = getNetworkRequest();
        this.f = f;
        i = null;
        loginIntent = null;
    }

    public EventCreation(@NonNull Fragment f, @NonNull String jwt, @NonNull EventViewModel evm,
                         @NonNull ActivityResultLauncher<Intent> i, @NonNull Intent loginIntent) {
        userJwt = jwt;
        this.evm = evm;
        request = getNetworkRequest();
        this.f = f;
        this.i = i;
        this.loginIntent = loginIntent;
    }

    public void run() {
        JSONObject jsonObject = evm.toJson();
        String url;

        if (!evm.getPrivEvent()) {
            url = getBaseUrl() + "/api/v2/EventiPubblici";
        } else {
            url = getBaseUrl() + "/api/v2/EventiPrivati";
        }

        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json; charset=utf-8"));

        List<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<>("x-access-token", userJwt));
        Request req = request.getPostRequest(body, headers, url);
        request.enqueue(req, new EventCreationCallback(f, i, loginIntent));
    }
}
