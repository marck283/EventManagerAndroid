package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.publicEvent;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface EventInfoInterface {
    @GET("/api/v2/EventiPubblici/{id}")
    Call<JsonObject> getPubEventInfo(@Path("id") String eventId);

    @GET("/api/v2/InfoEventoOrg/{id}")
    Call<JsonObject> getOrgEventInfo(@Path("id") String eventId, @Header("x-access-token") String userJwt);
}
