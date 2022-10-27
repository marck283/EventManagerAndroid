package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface PublicEventsInterface {
    @GET("/api/v2/eventiCalendarioPubblico")
    Call<JsonObject> pubEv(@Header("x-access-token") String token,
                           @Header("nomeAtt") String nomeAtt,
                           @Header("categoria") String categoria,
                           @Header("durata") String durata,
                           @Header("indirizzo") String indirizzo,
                           @Header("citta") String citta);
}
