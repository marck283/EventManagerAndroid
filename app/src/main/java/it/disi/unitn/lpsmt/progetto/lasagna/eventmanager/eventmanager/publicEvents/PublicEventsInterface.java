package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents;

import org.json.JSONObject;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents.publicEvent.PubEvList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface PublicEventsInterface {
    @GET("/api/v2/eventiCalendarioPubblico")
    Call<PubEvList> pubEv(@Header("token") String token,
                          @Header("nomeAtt") String nomeAtt,
                          @Header("categoria") String categoria,
                          @Header("durata") String durata,
                          @Header("indirizzo") String indirizzo,
                          @Header("citta") String citta);
}
