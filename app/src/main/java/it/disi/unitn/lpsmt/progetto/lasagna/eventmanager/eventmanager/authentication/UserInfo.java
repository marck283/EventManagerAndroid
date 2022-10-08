package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.csrfToken.ApiCSRFClass;

public class UserInfo {
    private String token, email, id, self;

    public UserInfo parseJSON(@NonNull JsonObject json) {
        GsonBuilder gson = new GsonBuilder();
        Gson gs1 = gson.create();
        gs1.fromJson(json.toString(), UserInfo.class);
        Log.i("OK", "JSON parsed");
        return this;
    }

    public String getString(@NonNull String name) {
        switch(name) {
            case "token": {
                return token;
            }
            case "email": {
                return email;
            }
            case "id": {
                return id;
            }
            case "self": {
                return self;
            }
            default: {
                Log.i("null", "Nessun parametro con tale nome.");
                return null;
            }
        }
    }
}
