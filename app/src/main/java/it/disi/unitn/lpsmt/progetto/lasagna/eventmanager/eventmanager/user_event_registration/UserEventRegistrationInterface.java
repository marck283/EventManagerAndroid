package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.user_event_registration;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserEventRegistrationInterface {
    @POST("/api/v2/EventiPubblici/{id}/Iscrizioni")
    Call<JsonObject> registerUser(@Path("id") String eventId, @Header("x-access-token") String userAccessToken,
                                  @Body EventDayHour dayHour);
}
