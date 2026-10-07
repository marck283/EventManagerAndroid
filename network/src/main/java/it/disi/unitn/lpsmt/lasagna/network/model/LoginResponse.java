package it.disi.unitn.lpsmt.lasagna.network.model;

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

    @SerializedName("profilePic")
    private String profilePic;

    public LoginResponse() {
        // Required for Gson
    }

    public LoginResponse(String token, String email, String name, String id, String self, String profilePic) {
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

    public String getProfilePic() {
        return profilePic;
    }
}
