package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.registeredEvent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.Contract;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.publicEvent.LuogoEvento;

public class RegisteredEvent {

    private final String id, idevent, self, name, category, eventPic, orgName, durata;
    private final LuogoEvento luogoEv;

    public RegisteredEvent(@NonNull String id, @NonNull String idevent, @NonNull String self,
                           @NonNull String name, @NonNull String category, @NonNull String eventPic,
                           @NonNull String orgName, @NonNull LuogoEvento luogoEv, @NonNull String durata) {
        this.id = id;
        this.idevent = idevent;
        this.self = self;
        this.name = name;
        this.category = category;
        this.eventPic = eventPic;
        this.orgName = orgName;
        this.luogoEv = luogoEv;
        this.durata = durata;
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
    public String getIdEvent() {
        return idevent;
    }

    public String getSelf() {
        return self;
    }

    public String getEventName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getEventPic() {
        return eventPic;
    }

    public String getOrgName() {
        return orgName;
    }

    public LuogoEvento getLuogoEv() {
        return luogoEv;
    }

    public String getDurata() {
        return durata;
    }

    public Bitmap decodeBase64() {
        byte[] decodedImg = Base64.decode(eventPic
                .replace("data:image/png;base64,", "")
                .replace("data:image/jpeg;base64,",""), Base64.DEFAULT); //Ritorna una stringa in formato Base64
        return BitmapFactory.decodeByteArray(decodedImg, 0, decodedImg.length); //Decodifico la stringa ottenuta
    }

    private static String fromJsonString(@NonNull Gson gs1, @NonNull JsonObject e, @NonNull String name) {
        return gs1.fromJson(e.get(name), String.class);
    }

    @NonNull
    @Contract("_ -> new")
    public static RegisteredEvent parseJSON(@NonNull JsonObject json) {
        Gson gs1 = new GsonBuilder().create();

        JsonObject json1 = json.get("event").getAsJsonObject();
        return new RegisteredEvent(fromJsonString(gs1, json1, "id"),
                fromJsonString(gs1, json1, "idevent"),
                fromJsonString(gs1, json1, "self"), fromJsonString(gs1, json1, "name"),
                fromJsonString(gs1, json1, "category"), fromJsonString(gs1, json1, "eventPic"),
                fromJsonString(gs1, json1, "orgName"),
                LuogoEvento.parseJSON(json1.getAsJsonArray("luogoEv").get(0).getAsJsonObject()),
                fromJsonString(gs1, json1, "durata"));
    }
}
