package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class EventInfo {
    private String id, nomeAtt, categoria, organizzatore, eventPic;
    private int durata;
    private boolean terminato;
    private ArrayList<LuogoEvento> luogoEvento;

    public EventInfo() {
        durata = 0;
        terminato = false;
    }

    public String getOrgName() {
        return organizzatore;
    }

    public int getDurata() {
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
    int durata, boolean terminato, @NonNull ArrayList<LuogoEvento> luogoEvento, @NonNull String organizzatore) {
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

        for(LuogoEvento le: luogoEvento) {
            if(!res.contains(le.getData())) {
                res.add(le.getData());
            }
        }

        try {
            res.forEach(c -> c = String.join("/", c.split("-")));
        } catch(NullPointerException ex) {
            Log.i("voidDateSpinner", "Nessuna data per questo evento");
        }

        return res;
    }

    public ArrayList<String> getOrari(@NonNull String data) {
        ArrayList<String> orari = new ArrayList<>();

        for(LuogoEvento le: luogoEvento) {
            if(!orari.contains(le.getOra()) && le.getData().equals(data)) {
                orari.add(le.getOra());
            }
        }

        return orari;
    }

    public LuogoEvento getLuogo(@NonNull String data, @NonNull String ora) {
        LuogoEvento res = null;

        for(LuogoEvento le: luogoEvento) {
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
    private ArrayList<LuogoEvento> fromJsonArr(@NonNull JsonArray json) {
        ArrayList<LuogoEvento> larr = new ArrayList<>();

        for(int i = 0; i < json.size(); i++) {
            larr.add(LuogoEvento.parseJSON(json.get(i).getAsJsonObject()));
        }

        return larr;
    }

    public EventInfo parseJSON(@NonNull JsonObject response) {
        Gson gs1 = new GsonBuilder().create();
        return new EventInfo(fromJsonString(gs1, response, "id"),
                fromJsonString(gs1, response, "nomeAtt"),
                fromJsonString(gs1, response, "categoria"),
                fromJsonString(gs1, response, "eventPic"),
                Integer.parseInt(fromJsonString(gs1, response, "durata")),
                Boolean.parseBoolean(fromJsonString(gs1, response, "terminato")),
                fromJsonArr(response.getAsJsonArray("luogoEv")),
                fromJsonString(gs1, response, "organizzatore"));
    }
}
