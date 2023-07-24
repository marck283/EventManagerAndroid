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

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication.Authentication;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CsrfTokenCallback implements Callback {

    private final Authentication o;

    private final Activity a;

    private final Intent i;

    private final String which;

    private final AccessToken fbJwt;

    private final String gJwt;

    public CsrfTokenCallback(@NotNull Authentication o, @NotNull Activity a, @Nullable Intent i,
                             @NotNull String which, @Nullable AccessToken fbJwt, @Nullable String gJwt) {
        this.o = o;
        this.a = a;
        this.i = i;
        this.which = which;
        this.fbJwt = fbJwt;
        this.gJwt = gJwt;
    }

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
            if (response.isSuccessful() && response.body() != null) {
                Gson gson = new GsonBuilder().create();
                ApiCSRFClass token1 = ApiCSRFClass.parseJSON(gson.fromJson(response.body().string(), JsonObject.class));
                o.setActivity(a);
                o.setCsrfToken(token1.getToken());
                if(i != null) {
                    o.setIntent(i);
                }
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
}
