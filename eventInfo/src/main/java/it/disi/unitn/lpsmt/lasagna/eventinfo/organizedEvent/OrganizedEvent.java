package it.disi.unitn.lpsmt.lasagna.eventinfo.organizedEvent;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lasagna.eventcreation.helpers.LuogoEv;
import it.disi.unitn.lpsmt.lasagna.localdatabase.Event;

public class OrganizedEvent extends Event {
    private final String id, idevent, self, name, category, eventPic, orgName, durata;
    private final ArrayList<LuogoEv> luogoEv;

    public OrganizedEvent(@NonNull String id, @NonNull String idevent, @NonNull String self,
                          @NonNull String name, @NonNull String category, @NonNull String eventPic,
                          @NonNull String orgName, @NonNull ArrayList<LuogoEv> luogoEv,
                          @NonNull String durata) {
        super(id, idevent, self, name, category, eventPic, orgName, luogoEv, durata);
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

    private static String fromJsonString(@NonNull Gson gs1, @NonNull JsonObject e, @NonNull String name) {
        return gs1.fromJson(e.get(name), String.class);
    }

    @NonNull
    private static ArrayList<LuogoEv> fromJsonArray(@NonNull JsonObject json) {
        ArrayList<LuogoEv> arr = new ArrayList<>();
        JsonArray jsonArr = json.getAsJsonArray("luogoEv");

        for(JsonElement e: jsonArr) {
            arr.add(LuogoEv.parseJSON(e.getAsJsonObject()));
        }

        return arr;
    }

    public LuogoEv getLuogo(@NonNull String data, @NonNull String ora) {
        LuogoEv res = null;
        for(LuogoEv l: luogoEv) {
            if(l.getData().equals(data) && l.getOra().equals(ora)) {
                res = l;
            }
        }
        return res;
    }

    public List<LuogoEv> getOrari(@NonNull String data) {
        ArrayList<LuogoEv> res = new ArrayList<>();
        for(LuogoEv l: luogoEv) {
            if(l.getData().equals(data)) {
                res.add(l);
            }
        }
        return res;
    }

    @NonNull
    @Contract("_ -> new")
    public static OrganizedEvent parseJSON(@NonNull JsonObject json) {
        Gson gs1 = new GsonBuilder().create();
        json = json.getAsJsonObject("event");
        return new OrganizedEvent(fromJsonString(gs1, json, "id"),
                fromJsonString(gs1, json, "idevent"),
                fromJsonString(gs1, json, "self"), fromJsonString(gs1, json, "name"),
                fromJsonString(gs1, json, "category"), fromJsonString(gs1, json, "eventPic"),
                fromJsonString(gs1, json, "orgName"), fromJsonArray(json),
                fromJsonString(gs1, json, "durata"));
    }
}
