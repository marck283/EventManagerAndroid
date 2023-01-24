package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class EventViewModel extends ViewModel {
    private String nomeAtt = "";
    private boolean privEvent = false;
    private int giorni = -1, ore = -1, minuti = -1;
    private ArrayList<LuogoEv> luogoEv = new ArrayList<>();
    private String categoria, base64Image, description;

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

    public void setLuogoEv(String address, String city, String civNum, String province, int cap, String data, String ora, int maxPers) {
        luogoEv.add(new LuogoEv(address, city, civNum, province, cap, data, ora, maxPers));
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
}
