package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.publicEvent;

import android.location.Address;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.Locale;

public class LuogoEvento extends Address {
    private String indirizzo, civNum, citta, provincia, data, ora;
    private int cap, maxPers, postiRimanenti;

    /**
     * Constructs a new Address object set to the given Locale and with all
     * other fields initialized to null or false.
     *
     * @param locale
     */
    public LuogoEvento(Locale locale) {
        super(locale);
    }

    public String getData() {
        return data;
    }

    public String getOra() {
        return ora;
    }

    public int getPostiRimanenti() {
        return postiRimanenti;
    }

    public int getMaxPers() {
        return maxPers;
    }

    public LuogoEvento(@NonNull String indirizzo, @NonNull String civNum, int cap, @NonNull String citta,
                       @NonNull String provincia, int maxPers, @NonNull String data, @NonNull String ora, int numPosti) {
        super(Locale.getDefault());
        this.indirizzo = indirizzo;
        this.civNum = civNum;
        this.cap = cap;
        this.citta = citta;
        this.provincia = provincia;
        this.maxPers = maxPers;
        this.data = data;
        this.ora = ora;

        if(maxPers > 0) {
            postiRimanenti = maxPers - numPosti;
        } else {
            postiRimanenti = 0;
        }
    }

    public static String fromJsonString(@NonNull Gson gs1, @NonNull JsonObject eo, @NonNull String name) {
        return gs1.fromJson(eo.get(name), String.class);
    }

    @NonNull
    public static LuogoEvento parseJSON(@NonNull JsonObject json) {
        Gson gs1 = new GsonBuilder().create();

        int maxPers = 0;
        if(json.get("maxPers") != null) {
            maxPers = Integer.parseInt(fromJsonString(gs1, json, "maxPers"));
        }

        return new LuogoEvento(fromJsonString(gs1, json, "indirizzo"),
                fromJsonString(gs1, json, "civNum"),
                Integer.parseInt(fromJsonString(gs1, json, "cap")),
                fromJsonString(gs1, json, "citta"),
                fromJsonString(gs1, json, "provincia"),
                maxPers,
                fromJsonString(gs1, json, "data"), fromJsonString(gs1, json, "ora"),
                json.getAsJsonArray("partecipantiID").size());
    }

    @NonNull
    public String toString() {
        return indirizzo + ", " + civNum + ", " + cap + " " + citta + " " + provincia;
    }
}
