package it.disi.unitn.lpsmt.lasagna.eventinfo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import it.disi.unitn.lasagna.eventcreation.helpers.LuogoEv;

public class EventInfo {
    protected final String eventType;
    protected final String id;
    protected final String self;
    protected final String nomeAtt;
    protected final String categoria;
    protected final String organizzatore;
    protected final String eventPic;
    protected final String durata;
    protected boolean terminato;
    protected ArrayList<LuogoEv> luogoEvento;

    public EventInfo() {
        this.eventType = "";
        id = "";
        nomeAtt = "";
        self = "";
        categoria = "";
        organizzatore = "";
        eventPic = "";
        durata = "";
        terminato = false;
    }

    protected EventInfo(@NonNull String eventType, @NonNull String id, @NonNull String self,
                        @NonNull String nomeAtt, @NonNull String categoria, @NonNull String eventPic,
                        String durata, boolean terminato, @NonNull ArrayList<LuogoEv> luogoEvento,
                        @NonNull String organizzatore) {
        this.eventType = eventType;
        this.id = id;
        this.self = self;
        this.nomeAtt = nomeAtt;
        this.categoria = categoria;
        this.eventPic = eventPic;
        this.durata = durata;
        this.terminato = terminato;
        this.luogoEvento = luogoEvento;
        this.organizzatore = organizzatore;
    }

    public String getEventType() {
        return eventType;
    }

    public String getOrgName() {
        return organizzatore;
    }

    public String getDurata() {
        return durata;
    }

    public String getEventPic() {
        return eventPic;
    }

    /**
     * Decodifica il valore della stringa base64 che rappresenta l'immagine dell'evento in Bitmap.
     * @return Il valore decodificato in tipo Bitmap
     */
    public Bitmap decodeBase64() {
        byte[] decodedImg = Base64.decode(eventPic
                .replace("data:image/png;base64,", "")
                .replace("data:image/jpeg;base64,",""), Base64.DEFAULT); //Ritorna una stringa in formato Base64
        return BitmapFactory.decodeByteArray(decodedImg, 0, decodedImg.length); //Decodifico la stringa ottenuta
    }

    public String getId() {
        return id;
    }

    public String getNomeAtt() {
        return nomeAtt;
    }

    public ArrayList<String> getLuoghi() {
        ArrayList<String> res = new ArrayList<>();

        for(LuogoEv le: luogoEvento) {
            String[] dataArr = le.getData().split("-");
            String data = String.join("/", dataArr[1], dataArr[0], dataArr[2]);
            if(!res.contains(data)) {
                res.add(data);
            }
        }

        return res;
    }

    public ArrayList<String> getOrari(@NonNull String data) {
        ArrayList<String> orari = new ArrayList<>();

        for(LuogoEv le: luogoEvento) {
            if(!orari.contains(le.getOra()) && le.getData().equals(data)) {
                orari.add(le.getOra());
            }
        }

        return orari;
    }

    public LuogoEv getLuogo(@NonNull String data, @NonNull String ora) {
        LuogoEv res = null;

        for(LuogoEv le: luogoEvento) {
            if(le.getData().equals(data) && le.getOra().equals(ora)) {
                res = le;
            }
        }

        return res;
    }

    public void setTerminato(boolean terminato) {
        this.terminato = terminato;
    }

    private static String fromJsonString(@NonNull Gson gs1, @NonNull JsonObject json, @NonNull String name) {
        if (json.get(name) == null || json.get(name).isJsonNull()) {
            return "";
        }
        return gs1.fromJson(json.get(name), String.class);
    }

    @NonNull
    private static ArrayList<LuogoEv> fromJsonArr(@NonNull JsonArray json) {
        ArrayList<LuogoEv> larr = new ArrayList<>();

        if (json != null) {
            for (int i = 0; i < json.size(); i++) {
                larr.add(LuogoEv.parseJSON(json.get(i).getAsJsonObject()));
            }
        }

        return larr;
    }

    @NonNull
    public static EventInfo parseJSON(@NonNull JsonObject response) {
        Gson gs1 = new GsonBuilder().create();
        JsonObject json = (response.has("event") && response.get("event").isJsonObject())
                ? response.getAsJsonObject("event")
                : response;

        String eventType = json.has("eventType") ? fromJsonString(gs1, json, "eventType") : fromJsonString(gs1, json, "id");
        String id = json.has("idevent") ? fromJsonString(gs1, json, "idevent") : fromJsonString(gs1, json, "id");
        String self = fromJsonString(gs1, json, "self");
        String name = json.has("nomeAtt") ? fromJsonString(gs1, json, "nomeAtt") : fromJsonString(gs1, json, "name");
        String category = json.has("categoria") ? fromJsonString(gs1, json, "categoria") : fromJsonString(gs1, json, "category");
        String eventPic = fromJsonString(gs1, json, "eventPic");
        String durata = fromJsonString(gs1, json, "durata");
        boolean terminato = json.has("terminato") && !json.get("terminato").isJsonNull() && json.get("terminato").getAsBoolean();

        JsonArray luoghiArray = json.has("luogoEv") && !json.get("luogoEv").isJsonNull()
                ? json.getAsJsonArray("luogoEv")
                : new JsonArray();

        String orgName = json.has("organizzatore") ? fromJsonString(gs1, json, "organizzatore") : fromJsonString(gs1, json, "orgName");

        return new EventInfo(
                eventType,
                id,
                self,
                name,
                category,
                eventPic,
                durata,
                terminato,
                fromJsonArr(luoghiArray),
                orgName
        );
    }
}
