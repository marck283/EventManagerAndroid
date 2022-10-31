package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.serverTokenExchange;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServerTokenExchange {
    @GET("/api/v2/GoogleToken")
    Call<JsonObject> getAccessTokenFromServer(@Query("code") String code);
}
