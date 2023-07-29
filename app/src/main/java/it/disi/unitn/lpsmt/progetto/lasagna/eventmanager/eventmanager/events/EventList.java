package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lasagna.eventcreation.helpers.LuogoEv;

public class EventList {
    final List<Event> pubEvList;

    public EventList() {
        pubEvList = new ArrayList<>();
    }

    private String fromJson(@NonNull Gson gs1, @NonNull JsonObject eo, String name) {
        return gs1.fromJson(eo.get(name), String.class);
    }

    @NonNull
    private ArrayList<LuogoEv> fromJsonArr(@NonNull JsonArray json) {
        ArrayList<LuogoEv> larr = new ArrayList<>();

        for(int i = 0; i < json.size(); i++) {
            larr.add(LuogoEv.parseJSON(json.get(i).getAsJsonObject()));
        }

        return larr;
    }

    public EventList parseJSON(@NonNull JsonObject response) {
        GsonBuilder gson = new GsonBuilder();
        Gson gs1 = gson.create();

        JsonArray arr = response.get("eventi").getAsJsonArray();
        for(JsonElement e: arr) {
            JsonObject eo = e.getAsJsonObject();

            Event pe = new Event(fromJson(gs1, eo, "id"),
                    fromJson(gs1, eo, "idevent"),
                    fromJson(gs1, eo, "self"),
                    fromJson(gs1, eo, "name"),
                    fromJson(gs1, eo, "category"),
                    fromJson(gs1, eo, "eventPic"),
                    fromJson(gs1, eo, "orgName"),
                    fromJsonArr(eo.getAsJsonArray("luogoEv")), fromJson(gs1, eo, "durata"));
            pubEvList.add(pe);
        }
        return this;
    }

    public List<Event> getList() {
        return pubEvList;
    }
}
