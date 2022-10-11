package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ServerAuthentication {
    @POST("/api/v2/authentications")
    Call<JsonObject> authentication(@Header("csrfToken") String token, @Body String googleJwt);
}
