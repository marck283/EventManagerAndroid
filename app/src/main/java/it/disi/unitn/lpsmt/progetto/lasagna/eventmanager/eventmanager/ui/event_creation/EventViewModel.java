package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class EventViewModel extends ViewModel {
    private String nomeAtt = "";
    private boolean privEvent = false;
    private int durata;
    private ArrayList<LuogoEv> luogoEv = new ArrayList<>();
    private String categoria;
    private String base64Image;

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

    public void setDurata(Integer d) {
        durata = d;
    }

    public int getDurata() {
        return durata;
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
}
