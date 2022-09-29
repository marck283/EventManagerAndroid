package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents.publicEvent;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

public class PubEvList {
    ArrayList<PublicEvent> evList;

    public PubEvList() {
        evList = new ArrayList<>();
    }

    public PubEvList parseJSON(@NonNull JsonObject response) {
        Gson gson = new Gson();
        JsonArray arr = response.get("eventi").getAsJsonArray();
        for(JsonElement e: arr) {
            evList.add(gson.fromJson(e, PublicEvent.class));
        }
        return this;
    }

    public List<PublicEvent> getList() {
        return evList;
    }

    public void print() {
        Log.i("printing", "printing... " + evList.size());
        for(PublicEvent pe: evList) {
            pe.print();
        }
    }
}
