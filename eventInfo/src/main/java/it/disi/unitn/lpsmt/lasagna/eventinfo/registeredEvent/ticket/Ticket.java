package it.disi.unitn.lpsmt.lasagna.eventinfo.registeredEvent.ticket;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.Contract;

public class Ticket {
    private final String id, eventoid, utenteid, qr, tipoevento, giorno, ora;

    private Ticket(@NonNull String id, @NonNull String eventoId, @NonNull String userId, @NonNull String qr,
                   @NonNull String tipoEvento, @NonNull String giorno, @NonNull String ora) {
        this.id = id;
        eventoid = eventoId;
        utenteid = userId;
        this.qr = qr;
        tipoevento = tipoEvento;
        this.giorno = giorno;
        this.ora = ora;
    }

    public String getQR() {
        return qr;
    }

    private static String fromJsonString(@NonNull Gson gson, @NonNull JsonObject json, @NonNull String name) {
        return gson.fromJson(json.get(name), String.class);
    }

    @NonNull
    @Contract("_ -> new")
    public static Ticket parseJSON(@NonNull JsonObject json) {
        Gson gson = new GsonBuilder().create();
        json = json.get("biglietto").getAsJsonObject();
        return new Ticket(fromJsonString(gson, json, "id"),
                fromJsonString(gson, json, "eventoid"),
                fromJsonString(gson, json, "utenteid"),
                fromJsonString(gson, json, "qr"),
                fromJsonString(gson, json, "tipoevento"),
                fromJsonString(gson, json, "data"),
                fromJsonString(gson, json, "ora"));
    }
}
