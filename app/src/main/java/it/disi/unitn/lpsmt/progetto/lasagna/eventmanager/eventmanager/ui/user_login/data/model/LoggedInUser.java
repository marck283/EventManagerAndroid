package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.data.model;

import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication.UserAccount;

/**
 * Data class that captures user information for logged in users
 */
public class LoggedInUser {

    private String token, email, id, self;

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getSelf() {
        return self;
    }

    public LoggedInUser parseJSON(@NonNull JsonObject json) {
        GsonBuilder gson = new GsonBuilder();
        Gson gs1 = gson.create();
        gs1.fromJson(json.toString(), UserAccount.class);
        Log.i("OK", json.toString());
        return this;
    }
}