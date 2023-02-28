package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.organizedEvents;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface OrganizedEventsInterface {
    @GET("/api/v2/EventOrgList")
    Call<JsonObject> orgEv(@Header("x-access-token") String token, @Nullable @Header("name") String evName);
}
