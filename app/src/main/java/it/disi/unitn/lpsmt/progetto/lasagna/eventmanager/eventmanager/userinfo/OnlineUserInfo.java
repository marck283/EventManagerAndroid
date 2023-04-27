package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.userinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses.DBUser;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.NetworkRequest;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.networkOps.ServerOperation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

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
