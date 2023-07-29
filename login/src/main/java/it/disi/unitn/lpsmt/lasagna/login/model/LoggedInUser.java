package it.disi.unitn.lpsmt.lasagna.login.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Data class that captures user information for logged in users
 */
public class LoggedInUser {

    private String token, email, name, id, self, profilePic;

    public LoggedInUser() {
        //Costruttore vuoto
    }

    public LoggedInUser(String token, String email, String name, String id, String self, String profilePic) {
        this.token = token;
        this.email = email;
        this.id = id;
        this.self = self;
        this.profilePic = profilePic;
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
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

    public String getProfilePic() {
        return profilePic;
    }

    private String fromJson(@NonNull Gson gs1, String name, @NonNull JsonObject json) {
        return gs1.fromJson(json.get(name), String.class);
    }

    public LoggedInUser parseJSON(@NonNull JsonObject json) {
        GsonBuilder gson = new GsonBuilder();
        Gson gs1 = gson.create();
        LoggedInUser user = new LoggedInUser(fromJson(gs1, "token", json), fromJson(gs1, "email", json),
                fromJson(gs1, "name", json), fromJson(gs1, "id", json), fromJson(gs1, "self", json),
                fromJson(gs1, "profilePic", json));
        Log.i("OK", json.toString());
        return user;
    }
}