package it.disi.unitn.lpsmt.lasagna.eventinfo.registeredEvent;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;

import it.disi.unitn.lasagna.eventcreation.helpers.LuogoEv;
import it.disi.unitn.lpsmt.lasagna.eventinfo.EventInfo;

public class RegisteredEvent extends EventInfo {

    private final String idIscr;

    public RegisteredEvent(@NonNull String id, @NonNull String idevent, @NonNull String self,
                           @NonNull String name, @NonNull String category, @NonNull String eventPic,
                           @NonNull String orgName, @NonNull ArrayList<LuogoEv> luogoEv, @NonNull String durata,
                           @NonNull String idIscr) {
        super(id, idevent, self, name, category, eventPic, durata, false, luogoEv, orgName);
        this.idIscr = idIscr;
    }

    /**
     * Ritorna il tipo dell'evento (pubblico o privato).
     * @return il tipo dell'evento
     */
    public String getId() {
        return id;
    }

    /**
     * Ritorna l'id dell'evento così come identificato dal server.
     * @return l'id dell'evento così come identificato dal server
     */
    public String getEventType() {
        return eventType;
    }

    public String getSelf() {
        return self;
    }

    public String getEventName() {
        return nomeAtt;
    }

    public String getCategory() {
        return categoria;
    }

    public ArrayList<LuogoEv> getLuogoEv() {
        return luogoEvento;
    }

    /**
     * Ritorna l'identificatore del biglietto così come identificato dal server.
     * @return l'identificatore del biglietto così come identificato dal server
     */
    public String getTicketId() {
        return idIscr;
    }

    private static String fromJsonString(@NonNull Gson gs1, @NonNull JsonObject e, @NonNull String name) {
        return gs1.fromJson(e.get(name), String.class);
    }

    @NonNull
    @Contract("_ -> new")
    public static RegisteredEvent parseJSON(@NonNull JsonObject json) {
        Gson gs1 = new GsonBuilder().create();

        JsonObject event = json.get("event").getAsJsonObject().get("event").getAsJsonObject();
        String ticketId = json.get("biglietto").getAsString();
        ArrayList<LuogoEv> luogoEv = new ArrayList<>();
        luogoEv.add(LuogoEv.parseJSON(json.getAsJsonArray("luogoEv").get(0).getAsJsonObject()));
        return new RegisteredEvent(fromJsonString(gs1, event, "id"),
                fromJsonString(gs1, event, "idevent"),
                fromJsonString(gs1, event, "self"), fromJsonString(gs1, event, "name"),
                fromJsonString(gs1, event, "category"), fromJsonString(gs1, event, "eventPic"),
                fromJsonString(gs1, event, "orgName"),
                luogoEv,
                fromJsonString(gs1, event, "durata"), ticketId);
    }
}
