package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Retrofit;

public class PublicEvents {
    private Retrofit retro;
    private PublicEventsInterface pubEv;

    public PublicEvents() {
        retro = new Retrofit.Builder()
                .baseUrl("https://eventmanagerzlf.herokuapp.com")
                .build();
        pubEv = retro.create(PublicEventsInterface.class);
    }

    public List<JSONObject> getEvents(@Nullable String token, @Nullable String nomeAtt,
                                      @Nullable String categoria, @Nullable String durata,
                                      @Nullable String indirizzo, @Nullable String citta) {
        try {
            return pubEv.pubEv(token, nomeAtt, categoria, durata, indirizzo, citta)
                    .execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
