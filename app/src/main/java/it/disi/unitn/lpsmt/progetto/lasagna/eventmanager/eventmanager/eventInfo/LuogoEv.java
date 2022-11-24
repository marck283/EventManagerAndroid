package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo;

import android.location.Address;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Locale;

public class LuogoEv extends Address {
    private String indirizzo, civNum, citta, provincia, data, ora;
    private int cap, maxPers, numPartecipanti, postiRimanenti;

    /**
     * Constructs a new Address object set to the given Locale and with all
     * other fields initialized to null or false.
     *
     * @param locale
     */
    public LuogoEv(Locale locale) {
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

    public LuogoEv(@NonNull String indirizzo, @NonNull String civNum, int cap, @NonNull String citta,
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
    }

    public static String fromJsonString(@NonNull Gson gs1, @NonNull JsonElement eo) {
        return gs1.fromJson(eo.getAsString(), String.class);
    }

    @NonNull
    public static LuogoEv parseJSON(@NonNull JsonObject json) {
        Gson gs1 = new GsonBuilder().create();

        //Da correggere aggiungendo il numero di posti rimanenti come ultimo argomento
        return new LuogoEv(fromJsonString(gs1, json.get("indirizzo")),
                fromJsonString(gs1, json.get("civNum")),
                Integer.parseInt(fromJsonString(gs1, json.get("cap"))),
                fromJsonString(gs1, json.get("citta")), fromJsonString(gs1, json.get("provincia")),
                Integer.parseInt(fromJsonString(gs1, json.get("maxPers"))),
                fromJsonString(gs1, json.get("data")), fromJsonString(gs1, json.get("ora")));
    }

    @NonNull
    public String toString() {
        return indirizzo + ", " + civNum + ", " + cap + " " + citta + " " + provincia;
    }
}
