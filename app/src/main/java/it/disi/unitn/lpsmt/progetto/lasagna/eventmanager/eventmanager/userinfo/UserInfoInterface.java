package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.userinfo;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserInfoInterface {
    @GET("/api/v2/Utenti/me")
    Call<JsonObject> getUserInfo(@Header("x-access-token") String accessToken);
}
