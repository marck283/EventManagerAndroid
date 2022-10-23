package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.privateEvents;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface PrivateEventsInterface {
    @GET("/api/v2/eventiCalendarioPersonale/{data}")
    Call<JsonObject> privEv(@Header("token") String token, @Path("data") String data);
}
