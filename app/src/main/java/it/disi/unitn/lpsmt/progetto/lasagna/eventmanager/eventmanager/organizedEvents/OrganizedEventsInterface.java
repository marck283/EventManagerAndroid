package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.organizedEvents;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface OrganizedEventsInterface {
    @GET("/api/v2/EventOrgList/{data}")
    Call<JsonObject> orgEv(@Header("x-access-token") String token, @Path("data") String data);
}
