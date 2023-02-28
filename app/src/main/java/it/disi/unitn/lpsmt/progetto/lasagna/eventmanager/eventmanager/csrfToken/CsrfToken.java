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

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication.Authentication;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CsrfToken {

    //Come associo il token CSRF alla classe di autenticazione senza dimenticare che potrebbe servirmi anche per altre classi in futuro?
    public void getCsrfToken(@NonNull Activity a, @NonNull Authentication o, @Nullable String gJwt, @Nullable AccessToken fbJwt,
                             @NonNull String which, @Nullable Intent i) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://eventmanagerzlf.herokuapp.com/api/v2/csrfToken")
                .build();
        client.newCall(request).enqueue(new Callback() {
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
                        o.login(a, token1.getToken(), gJwt, fbJwt, which, i);
                    } else {
                        Log.i("null", "Unsuccessful or null response");
                    }
                    notify();
                }
            }
        });
    }
}
