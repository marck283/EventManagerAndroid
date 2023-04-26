package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.csrfToken;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication.Authentication;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.NetworkRequest;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.networkOps.ServerOperation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class CsrfToken extends ServerOperation {

    private final Activity a;

    private final String gJwt, which;

    private final AccessToken fbJwt;

    private final Intent i;

    public CsrfToken(@NonNull Activity a, @Nullable String gJwt,
                     @Nullable AccessToken fbJwt, @NonNull String which, @Nullable Intent i) {
        this.a = a;
        this.gJwt = gJwt;
        this.fbJwt = fbJwt;
        this.which = which;
        this.i = i;
    }

    //Come associo il token CSRF alla classe di autenticazione senza dimenticare che potrebbe servirmi anche per altre classi in futuro?
    public void run() {
        NetworkRequest req = new NetworkRequest();
        Authentication o = new Authentication();
        Request request = req.getRequest(new ArrayList<>(), getBaseUrl() + "/api/v2/csrfToken");
        req.enqueue(request, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                try {
                    throw e;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                synchronized (this) {
                    ApiCSRFClass token = new ApiCSRFClass();
                    if (response.isSuccessful() && response.body() != null) {
                        Gson gson = new GsonBuilder().create();
                        ApiCSRFClass token1 = token.parseJSON(gson.fromJson(response.body().string(), JsonObject.class));
                        Log.i("token1", String.valueOf(token1.getToken()));
                        o.setActivity(a);
                        o.setCsrfToken(token1.getToken());
                        o.setIntent(i);
                        o.setWhich(which);
                        if(which.equals("google")) {
                            o.setUserToken(gJwt, null);
                        } else {
                            if(which.equals("facebook")) {
                                o.setUserToken(null, fbJwt);
                            }
                        }
                        o.start();
                    } else {
                        Log.i("null", "Unsuccessful or null response");
                    }
                    notify();
                }
            }
        });
    }
}
