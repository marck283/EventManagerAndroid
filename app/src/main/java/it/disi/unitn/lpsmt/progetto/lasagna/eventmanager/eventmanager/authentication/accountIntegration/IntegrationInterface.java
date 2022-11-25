package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication.accountIntegration;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

public interface IntegrationInterface {
    @GET("/api/v2/AccountIntegration")
    Call<JsonObject> googleIntegrate(@Body GoogleIntegration gInt);

    @GET("/api/v2/AccountIntegration")
    Call<JsonObject> fbIntegrate(@Body FBIntegration fbInt);
}
