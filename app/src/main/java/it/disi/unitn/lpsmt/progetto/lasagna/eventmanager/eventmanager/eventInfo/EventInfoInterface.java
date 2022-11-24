package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EventInfoInterface {
    @GET("/api/v2/EventiPubblici/{id}")
    Call<JsonObject> getEventInfo(@Path("id") String eventId);
}
