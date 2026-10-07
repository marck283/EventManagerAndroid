package it.disi.unitn.lpsmt.lasagna.network.api;

import com.google.gson.JsonObject;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EventApi {

    @GET("/api/v2/eventiCalendarioPubblico")
    Call<JsonObject> getPublicCalendarEvents();

    @GET("/api/v2/EventiPubblici/{eventId}")
    Call<JsonObject> getPublicEventInfo(@Path("eventId") String eventId);

    @GET("/api/v2/eventiCalendarioPersonale/{data}")
    Call<JsonObject> getPersonalCalendarEvents(@Path("data") String data);

    @POST("/api/v2/EventiPubblici")
    Call<ResponseBody> createPublicEvent(@Body RequestBody body);

    @POST("/api/v2/EventiPrivati")
    Call<ResponseBody> createPrivateEvent(@Body RequestBody body);
}