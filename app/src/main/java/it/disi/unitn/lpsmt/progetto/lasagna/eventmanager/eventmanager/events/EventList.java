package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class EventList {
    List<Event> pubEvList;

    public EventList() {
        pubEvList = new ArrayList<>();
    }

    private String fromJson(@NonNull Gson gs1, @NonNull JsonObject eo, String name) {
        return gs1.fromJson(eo.get(name), String.class);
    }

    public EventList parseJSON(@NonNull JsonObject response) {
        GsonBuilder gson = new GsonBuilder();
        Gson gs1 = gson.create();

        JsonArray arr = response.get("eventi").getAsJsonArray();
        for(JsonElement e: arr) {
            JsonObject eo = e.getAsJsonObject();

            Log.i("info", String.valueOf(fromJson(gs1, eo, "orgName") == null));

            Event pe = new Event(fromJson(gs1, eo, "id"),
                    fromJson(gs1, eo, "idevent"),
                    fromJson(gs1, eo, "self"),
                    fromJson(gs1, eo, "name"),
                    fromJson(gs1, eo, "category"),
                    fromJson(gs1, eo, "eventPic"),
                    fromJson(gs1, eo, "orgName"),
                    fromJson(gs1, eo.get("luogoEv").getAsJsonObject().get("data").getAsJsonObject(), "data"),
                    fromJson(gs1, eo.get("luogoEv").getAsJsonObject().get("ora").getAsJsonObject(), "ora"));
            pubEvList.add(pe);
        }
        return this;
    }

    public List<Event> getList() {
        return pubEvList;
    }

    public void print() {
        for(Event pe: pubEvList) {
            pe.print();
        }
    }
}
