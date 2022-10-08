package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.csrfToken;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CsrfTokenRequest {
    @GET("/api/v2/csrfToken")
    Call<JsonObject> getToken();
}
