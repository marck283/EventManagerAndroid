package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents.publicEvent;

import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import java.util.List;

public class PubEvList {
    List<PublicEvent> pubEvList;

    public PubEvList() {
        pubEvList = new ArrayList<>();
    }

    private String fromJson(Gson gs1, JsonObject eo, String name) {
        return gs1.fromJson(eo.get(name), String.class);
    }

    public PubEvList parseJSON(@NonNull JsonObject response) {
        GsonBuilder gson = new GsonBuilder();
        Gson gs1 = gson.create();

        JsonArray arr = response.get("eventi").getAsJsonArray();
        for(JsonElement e: arr) {
            JsonObject eo = e.getAsJsonObject();
            PublicEvent pe = new PublicEvent(fromJson(gs1, eo, "id"),
                    fromJson(gs1, eo, "idevent"),
                    fromJson(gs1, eo, "self"), fromJson(gs1, eo, "name"),
                    fromJson(gs1, eo, "category"), fromJson(gs1, eo, "eventPic"));
            pubEvList.add(pe);
        }
        return this;
    }

    public List<PublicEvent> getList() {
        return pubEvList;
    }
}
