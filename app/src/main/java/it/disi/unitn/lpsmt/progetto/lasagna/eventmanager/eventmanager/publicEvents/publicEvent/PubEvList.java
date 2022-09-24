package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents.publicEvent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class PubEvList {
    List<PublicEvent> evList;

    public PubEvList() {
        evList = new ArrayList<>();
    }

    public PubEvList parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(response, PubEvList.class);
    }

    public List<PublicEvent> getList() {
        return evList;
    }
}
