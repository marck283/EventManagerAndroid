package it.disi.unitn.lasagna.eventcreation.helpers;

import android.location.Address;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class LuogoEv extends Address {
    private String indirizzo, civNum, citta, provincia, data, ora;
    private int cap, maxPers, postiRimanenti;

    private Boolean terminato = false;

    /**
     * Constructs a new Address object set to the given Locale and with all
     * other fields initialized to null or false.
     *
     * @param locale The user's Locale
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

    public Boolean getTerminato() {
        return terminato;
    }

    public LuogoEv(@NonNull String indirizzo, @NonNull String civNum, int cap, @NonNull String citta,
                   @NonNull String provincia, int maxPers, @NonNull String data, @NonNull String ora,
                   int numPosti, @NonNull Boolean terminato) {
        super(Locale.getDefault());
        this.indirizzo = indirizzo;
        this.civNum = civNum;
        this.cap = cap;
        this.citta = citta;
        this.provincia = provincia;
        this.maxPers = maxPers;
        this.data = data;
        this.ora = ora;
        postiRimanenti = numPosti;
        this.terminato = terminato;
    }

    public JSONObject toJSON(boolean priv) throws JSONException {
        JSONObject json = new JSONObject();

        json.put("indirizzo", indirizzo);
        json.put("civNum", civNum);
        json.put("cap", cap);
        json.put("citta", citta);
        json.put("provincia", provincia);
        if(!priv) {
            json.put("maxPers", maxPers);
        }
        json.put("data", data);
        json.put("ora", ora);

        return json;
    }

    public static String fromJsonString(@NonNull Gson gs1, @NonNull JsonObject eo, @NonNull String name) {
        return gs1.fromJson(eo.get(name), String.class);
    }

    public static Boolean fromJsonBoolean(@NonNull Gson gs1, @NonNull JsonObject json) {
        return gs1.fromJson(json.get("terminato"), Boolean.class);
    }

    @NonNull
    public static LuogoEv parseJSON(@NonNull JsonObject json) {
        Gson gs1 = new GsonBuilder().create();
        int maxPers = 0;
        if(json.get("maxPers") != null) {
            maxPers = Integer.parseInt(fromJsonString(gs1, json, "maxPers"));
        }

        return new LuogoEv(fromJsonString(gs1, json, "indirizzo"),
                fromJsonString(gs1, json, "civNum"),
                Integer.parseInt(fromJsonString(gs1, json, "cap")),
                fromJsonString(gs1, json, "citta"), fromJsonString(gs1, json, "provincia"),
                maxPers,
                fromJsonString(gs1, json, "data"), fromJsonString(gs1, json, "ora"),
                maxPers - json.getAsJsonArray("partecipantiID").size(),
                fromJsonBoolean(gs1, json));
    }

    @NonNull
    public String toString() {
        return indirizzo + ", " + civNum + ", " + cap + " " + citta + " " + provincia + ", " + maxPers
                + ", " + data + ", " + ora + ", " + postiRimanenti + ", " + terminato.toString();
    }

    public String getAddress() {
        return indirizzo + ", " + civNum + ", " + cap + " " + citta + " " + provincia;
    }

    public String getAddressWOCap() {
        return indirizzo + ", " + civNum + ", " + citta + ", " + provincia;
    }
}
