package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.checkQRCode;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.lasagna.network.NetworkRequest;
import it.disi.unitn.lpsmt.lasagna.network.networkOps.ServerOperation;
import okhttp3.Request;

public class CheckQRCode extends ServerOperation {

    private final NetworkRequest request;
    private final String userJwt, qrCode, eventid, day, hour;

    private final Fragment f;

    public CheckQRCode(@NonNull String userJwt, @NonNull String qrCode, @NonNull String eventid,
                       @NonNull String day, @NonNull String hour, @NonNull Fragment f) {
        this.userJwt = userJwt;
        this.qrCode = qrCode;
        request = getNetworkRequest();
        this.f = f;
        this.eventid = eventid;
        this.day = day;
        this.hour = hour;
    }

    public void run() {
        List<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<>("x-access-token", userJwt));
        headers.add(new Pair<>("eventoid", eventid));
        headers.add(new Pair<>("day", day));
        headers.add(new Pair<>("hour", hour));
        Request req = request.getRequest(headers,
                getBaseUrl() + "/api/v2/QRCodeCheck/" + qrCode);
        Log.i("code", qrCode);
        try {
            request.enqueue(req, new QRCodeCallback(f));
        } catch (InvalidObjectException e) {
            e.printStackTrace();
        }
    }
}
