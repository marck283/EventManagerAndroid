package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServerAuthentication {
    @POST("/api/v2/authentications")
    Call<JsonObject> authentication(@Body AuthObject o);

    @POST("/api/v2/authentications/facebookLogin")
    Call<JsonObject> fbAuth(@Body AuthObject o);
}
