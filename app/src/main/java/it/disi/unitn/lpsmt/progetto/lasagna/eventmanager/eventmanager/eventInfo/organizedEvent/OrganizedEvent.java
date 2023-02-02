package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.organizedEvent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.publicEvent.LuogoEvento;

public class OrganizedEvent {
    private final String id, idevent, self, name, category, eventPic, orgName, durata;
    private final ArrayList<LuogoEvento> luogoEv;

    public OrganizedEvent(@NonNull String id, @NonNull String idevent, @NonNull String self,
                          @NonNull String name, @NonNull String category, @NonNull String eventPic,
                          @NonNull String orgName, @NonNull ArrayList<LuogoEvento> luogoEv,
                          @NonNull String durata) {
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

    public String getId() {
        return id;
    }

    public String getEventType() {
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

    public ArrayList<LuogoEvento> getLuogoEv() {
        return luogoEv;
    }

    public String getDurata() {
        return durata;
    }

    private static String fromJsonString(@NonNull Gson gs1, @NonNull JsonObject e, @NonNull String name) {
        return gs1.fromJson(e.get(name), String.class);
    }

    @NonNull
    private static ArrayList<LuogoEvento> fromJsonArray(@NonNull JsonObject json) {
        ArrayList<LuogoEvento> arr = new ArrayList<>();
        JsonArray jsonArr = json.getAsJsonArray("luogoEv");

        for(JsonElement e: jsonArr) {
            arr.add(LuogoEvento.parseJSON(e.getAsJsonObject()));
        }

        return arr;
    }

    public Bitmap decodeBase64() {
        if(eventPic != null) {
            byte[] decodedImg = Base64.decode(eventPic
                    .replace("data:image/png;base64,", "")
                    .replace("data:image/jpeg;base64,",""), Base64.DEFAULT); //Ritorna una stringa in formato Base64
            return BitmapFactory.decodeByteArray(decodedImg, 0, decodedImg.length); //Decodifico la stringa ottenuta
        }
        return null;
    }

    @NonNull
    @Contract("_ -> new")
    public static OrganizedEvent parseJSON(@NonNull JsonObject json) {
        Gson gs1 = new GsonBuilder().create();
        JsonObject json1 = json;
        json = json.getAsJsonObject("event");
        return new OrganizedEvent(fromJsonString(gs1, json, "id"),
                fromJsonString(gs1, json, "idevent"),
                fromJsonString(gs1, json, "self"), fromJsonString(gs1, json, "name"),
                fromJsonString(gs1, json, "category"), fromJsonString(gs1, json, "eventPic"),
                fromJsonString(gs1, json, "orgName"), fromJsonArray(json),
                fromJsonString(gs1, json, "durata"));
    }
}
