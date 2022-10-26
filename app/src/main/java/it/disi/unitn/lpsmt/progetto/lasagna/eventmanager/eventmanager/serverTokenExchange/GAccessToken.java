package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.serverTokenExchange;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class GAccessToken {
    private String token;

    public GAccessToken parseJSON(@NonNull JsonObject json) {
        GsonBuilder gson = new GsonBuilder();
        Gson gs1 = gson.create();

        token = gs1.fromJson(json.get("authToken"), String.class);

        return this;
    }

    public String getToken() {
        return token;
    }
}
