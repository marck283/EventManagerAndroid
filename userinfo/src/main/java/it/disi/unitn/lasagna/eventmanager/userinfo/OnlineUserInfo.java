package it.disi.unitn.lasagna.eventmanager.userinfo;

import android.util.Pair;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.lasagna.network.NetworkRequest;
import it.disi.unitn.lpsmt.lasagna.network.networkOps.ServerOperation;
import okhttp3.Callback;
import okhttp3.Request;

public class OnlineUserInfo extends ServerOperation {

    private final String accessToken;

    private final Callback callback;

    public OnlineUserInfo(@NonNull String accessToken, @NonNull Callback callback) {
        this.accessToken = accessToken;
        this.callback = callback;
    }

    public void run() {
        List<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<>("x-access-token", accessToken));
        NetworkRequest request = getNetworkRequest();
        Request req = request.getRequest(headers, getBaseUrl() + "/api/v2/Utenti/me");
        request.enqueue(req, callback);
    }
}
