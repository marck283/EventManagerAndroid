package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events;

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

    @NonNull
    private String[] fromJsonArr(@NonNull Gson gs1, @NonNull JsonObject eo, String name) {
        ArrayList<String> arr1 = new ArrayList<>();
        for(JsonElement s: eo.getAsJsonArray(name)) {
            arr1.add(gs1.fromJson(s, String.class));
        }
        return arr1.toArray(new String[0]);
    }

    public EventList parseJSON(@NonNull JsonObject response) {
        GsonBuilder gson = new GsonBuilder();
        Gson gs1 = gson.create();

        JsonArray arr = response.get("eventi").getAsJsonArray();
        for(JsonElement e: arr) {
            JsonObject eo = e.getAsJsonObject();
            Event pe = new Event(fromJson(gs1, eo, "id"),
                    fromJson(gs1, eo, "idevent"),
                    fromJson(gs1, eo, "self"), fromJson(gs1, eo, "name"),
                    fromJson(gs1, eo, "category"), fromJson(gs1, eo, "eventPic"),
                    fromJson(gs1, eo, "orgName"), fromJsonArr(gs1, eo, "days"),
                    fromJson(gs1, eo, "hours"));
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
