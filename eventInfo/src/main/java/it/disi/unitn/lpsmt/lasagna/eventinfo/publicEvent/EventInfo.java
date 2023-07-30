package it.disi.unitn.lpsmt.lasagna.eventinfo.publicEvent;

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
    private final String id;
    private final String nomeAtt;
    private final String categoria;
    private final String organizzatore;
    private final String eventPic;
    private final String durata;
    private final boolean terminato;
    private ArrayList<LuogoEv> luogoEvento;

    public EventInfo() {
        id = "";
        nomeAtt = "";
        categoria = "";
        organizzatore = "";
        eventPic = "";
        durata = "";
        terminato = false;
    }

    public String getOrgName() {
        return organizzatore;
    }

    public String getDurata() {
        return durata;
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

    private EventInfo(@NonNull String id, @NonNull String nomeAtt, @NonNull String categoria, @NonNull String eventPic,
    String durata, boolean terminato, @NonNull ArrayList<LuogoEv> luogoEvento, @NonNull String organizzatore) {
        this.id = id;
        this.nomeAtt = nomeAtt;
        this.categoria = categoria;
        this.eventPic = eventPic;
        this.durata = durata;
        this.terminato = terminato;
        this.luogoEvento = luogoEvento;
        this.organizzatore = organizzatore;
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

    private String fromJsonString(@NonNull Gson gs1, @NonNull JsonObject json, @NonNull String name) {
        return gs1.fromJson(json.get(name), String.class);
    }

    @NonNull
    private ArrayList<LuogoEv> fromJsonArr(@NonNull JsonArray json) {
        ArrayList<LuogoEv> larr = new ArrayList<>();

        for(int i = 0; i < json.size(); i++) {
            larr.add(LuogoEv.parseJSON(json.get(i).getAsJsonObject()));
        }

        return larr;
    }

    public EventInfo parseJSON(@NonNull JsonObject response) {
        Gson gs1 = new GsonBuilder().create();
        return new EventInfo(fromJsonString(gs1, response, "id"),
                fromJsonString(gs1, response, "nomeAtt"),
                fromJsonString(gs1, response, "categoria"),
                fromJsonString(gs1, response, "eventPic"),
                fromJsonString(gs1, response, "durata"),
                Boolean.parseBoolean(fromJsonString(gs1, response, "terminato")),
                fromJsonArr(response.getAsJsonArray("luogoEv")),
                fromJsonString(gs1, response, "organizzatore"));
    }
}
