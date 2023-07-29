package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.disi.unitn.lasagna.eventcreation.helpers.LuogoEv;

public class EventViewModel extends ViewModel {
    private String nomeAtt = "";
    private boolean privEvent = false;
    private int giorni = -1, ore = -1, minuti = -1;
    private final ArrayList<LuogoEv> luogoEv = new ArrayList<>();
    private String categoria, base64Image, description;

    private int etaMin = 0, etaMax = 0; //Non obbligatori per la creazione di un evento

    public void setNomeAtt(String nome) {
        nomeAtt = nome;
    }

    public String getNomeAtt() {
        return nomeAtt;
    }

    public void setPrivEvent(boolean priv) {
        privEvent = priv;
    }

    public boolean getPrivEvent() {
        return privEvent;
    }

    public void setGiorni(Integer d) {
        giorni = d;
    }

    public int getGiorni() {
        return giorni;
    }

    public void setOre(Integer h) {
        ore = h;
    }

    public int getOre() {
        return ore;
    }

    public void setMinuti(Integer mins) {
        minuti = mins;
    }

    public int getMinuti() {
        return minuti;
    }

    public void setLuogoEv(@NonNull LuogoEv luogo) {
        luogoEv.add(luogo);
    }

    public ArrayList<LuogoEv> getLuogoEv() {
        return luogoEv;
    }

    public void setCategoria(String c) {
        categoria = c;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setBase64Image(@NonNull String base64Image) {
        this.base64Image = base64Image;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setDescription(String desc) {
        description = desc;
    }

    public String getDescription() {
        return description;
    }

    public void setEtaMin(int etaMin) {
        this.etaMin = etaMin;
    }

    public int getEtaMin() {
        return etaMin;
    }

    public void setEtaMax(int etaMax) {
        this.etaMax = etaMax;
    }

    public int getEtaMax() {
        return etaMax;
    }

    @NonNull
    private JSONArray toJSONArray() throws JSONException {
        JSONArray arr = new JSONArray();

        for(LuogoEv l: luogoEv) {
            arr.put(l.toJSON(privEvent));
        }

        return arr;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("descrizione", description);
            jsonObject.put("categoria", categoria);
            jsonObject.put("nomeAtt", nomeAtt);
            jsonObject.put("luogoEv", toJSONArray());

            Log.i("base64", base64Image);
            jsonObject.put("eventPic", base64Image);

            if(!privEvent) {
                JSONArray arr = new JSONArray();
                arr.put(giorni);
                arr.put(ore);
                arr.put(minuti);
                jsonObject.put("durata", arr);
                jsonObject.put("etaMin", etaMin);
                jsonObject.put("etaMax", etaMax);
            }
        } catch(JSONException ex) {
            ex.printStackTrace();
        }

        return jsonObject;
    }
}
