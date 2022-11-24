package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class EventInfo {
    private String nomeAtt, categoria, organizzatore, eventPic;
    private int durata;
    private boolean terminato;
    private LuogoEv luogoEv;

    public EventInfo() {
        durata = 0;
        terminato = false;
    }

    private EventInfo(@NonNull String nomeAtt, @NonNull String categoria, @NonNull String eventPic,
    int durata, boolean terminato, @NonNull LuogoEv luogoEv) {
        this.nomeAtt = nomeAtt;
        this.categoria = categoria;
        this.eventPic = eventPic;
        this.durata = durata;
        this.terminato = terminato;
        this.luogoEv = luogoEv;
    }

    private String fromJsonString(@NonNull Gson gs1, @NonNull JsonElement json) {
        return gs1.fromJson(json.getAsString(), String.class);
    }

    public EventInfo parseJSON(@NonNull JsonObject response) {
        Gson gs1 = new GsonBuilder().create();
        return new EventInfo(fromJsonString(gs1, response.get("nomeAtt")), fromJsonString(gs1, response.get("categoria")),
                fromJsonString(gs1, response.get("eventPic")), Integer.parseInt(fromJsonString(gs1, response.get("durata"))),
                Boolean.parseBoolean(fromJsonString(gs1, response.get("terminato"))),
                LuogoEv.parseJSON(response.getAsJsonObject("luogoEv")));
    }
}
