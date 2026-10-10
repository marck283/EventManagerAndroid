package it.disi.unitn.lpsmt.lasagna.network.model;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("token")
    private String token;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id;

    @SerializedName("self")
    private String self;

    @SerializedName(value = "profilePic", alternate = {"picture", "profile_pic"})
    private Uri profilePic;

    public LoginResponse() {
        // Required for Gson
    }

    public LoginResponse(String token, String email, String name, String id, String self, Uri profilePic) {
        this.token = token;
        this.email = email;
        this.name = name;
        this.id = id;
        this.self = self;
        this.profilePic = profilePic;
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getSelf() {
        return self;
    }

    public Uri getProfilePic() {
        return profilePic;
    }
}
