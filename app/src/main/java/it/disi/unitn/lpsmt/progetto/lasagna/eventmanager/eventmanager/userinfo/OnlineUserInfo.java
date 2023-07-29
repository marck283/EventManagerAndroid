package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.userinfo;

import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.lasagna.network.NetworkRequest;
import it.disi.unitn.lpsmt.lasagna.network.networkOps.ServerOperation;
import okhttp3.Request;

public class OnlineUserInfo extends ServerOperation {

    private final String accessToken;

    private final View v;

    private final Fragment f;

    public OnlineUserInfo(@NonNull String accessToken, @NonNull View v, @NonNull Fragment f) {
        this.accessToken = accessToken;
        this.v = v;
        this.f = f;
    }

    public void run() {
        List<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<>("x-access-token", accessToken));
        NetworkRequest request = getNetworkRequest();
        Request req = request.getRequest(headers, getBaseUrl() + "/api/v2/Utenti/me");
        request.enqueue(req, new UserProfileCallback(f, v));
    }
}
