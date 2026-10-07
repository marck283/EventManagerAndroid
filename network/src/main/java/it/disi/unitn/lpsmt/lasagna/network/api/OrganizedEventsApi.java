package it.disi.unitn.lpsmt.lasagna.network.api;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrganizedEventsApi {
    @GET("/api/v2/EventOrgList")
    Call<JsonObject> getOrganizedEvents();

    @GET("/api/v2/EventOrgList")
    Call<JsonObject> getOrganizedEventsByName(@Query("name") String name);

    @GET("/api/v2/EventOrgList/{data}")
    Call<JsonObject> getOrganizedEventsByDate(@Path("data") String data);

    @GET("/api/v2/InfoEventoOrg/{eventId}")
    Call<JsonObject> getOrganizedEventInfo(@Path("eventId") String eventId);

    @DELETE("/api/v2/annullaEvento/{eventId}")
    Call<ResponseBody> cancelEvent(@Path("eventId") String eventId);

    @FormUrlEncoded
    @POST("/api/v2/terminaEvento/{eventId}")
    Call<ResponseBody> terminateEvent(
            @Path("eventId") String eventId,
            @Field("data") String data,
            @Field("ora") String ora
    );
}
