package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.publicEvent.eventReviews;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ReviewList {
    private final List<Review> revList;

    public ReviewList() {
        revList = new ArrayList<>();
    }

    public void parseJSON(@NonNull JsonArray json) {
        for(JsonElement e: json) {
            JsonObject mainObject = e.getAsJsonObject(), recensione = mainObject.getAsJsonObject("recensione");
            revList.add(new Review(mainObject.get("name").getAsString(), mainObject.get("picture").getAsString(),
                    recensione.get("descrizione").getAsString(), recensione.get("valutazione").getAsFloat()));
        }
    }

    public List<Review> getList() {
        return revList;
    }
}
