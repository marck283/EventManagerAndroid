package it.disi.unitn.lpsmt.lasagna.network.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserApi {
    @GET("/api/v2/Utenti/me")
    Call<JsonObject> getUserProfile();
}
