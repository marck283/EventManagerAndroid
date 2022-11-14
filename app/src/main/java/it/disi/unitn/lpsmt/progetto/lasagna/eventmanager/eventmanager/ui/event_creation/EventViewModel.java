package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

class Pair<A, B> {
    private final A first;
    private final B second;

    Pair(A f, B s) {
        first = f;
        second = s;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
}

public class EventViewModel extends ViewModel {
    private final MutableLiveData<String> nomeAtt = new MutableLiveData<>();
    private final MutableLiveData<Boolean> privEvent = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Pair<String, String>>> dataOra = new MutableLiveData<>();
    private final MutableLiveData<Integer> durata = new MutableLiveData<>();
    private final MutableLiveData<LuogoEv> luogoEv = new MutableLiveData<>();
    private final MutableLiveData<Integer> maxPers = new MutableLiveData<>();
    private final MutableLiveData<String> categoria = new MutableLiveData<>();
    private final MutableLiveData<Integer> etaMin = new MutableLiveData<>(), etaMax = new MutableLiveData<>();

    public void setNomeAtt(String nome) {
        nomeAtt.setValue(nome);
    }

    public LiveData<String> getNomeAtt() {
        return nomeAtt;
    }

    public void setPrivEvent(boolean priv) {
        privEvent.setValue(priv);
    }

    public LiveData<Boolean> getPrivEvent() {
        return privEvent;
    }

    public void setDate(ArrayList<Pair<String, String>> dataOra) {
        this.dataOra.setValue(dataOra);
    }

    public void addDate(Pair<String, String> dataOra) {
        this.dataOra.getValue().add(dataOra);
    }

    /**
     * Ritorna una coppia di elementi (data, ora).
     * @return
     */
    public LiveData<ArrayList<Pair<String, String>>> getDate() {
        return dataOra;
    }

    public void setDurata(Integer d) {
        durata.setValue(d);
    }

    public LiveData<Integer> getDurata() {
        return durata;
    }

    public void setLuogoEv(String address, String city, String civNum, String province, int cap) {
        luogoEv.setValue(new LuogoEv(address, city, civNum, province, cap));
    }

    public LiveData<LuogoEv> getLuogoEv() {
        return luogoEv;
    }

    public void setMaxPers(Integer maxPers) {
        this.maxPers.setValue(maxPers);
    }

    public LiveData<Integer> getMaxPers() {
        return maxPers;
    }

    public void setCategoria(String c) {
        categoria.setValue(c);
    }

    public LiveData<String> getCategoria() {
        return categoria;
    }

    public void setEtaMin(Integer etaMin) {
        this.etaMin.setValue(etaMin);
    }

    public LiveData<Integer> getEtaMin() {
        return etaMin;
    }

    public void setEtaMax(Integer etaMax) {
        this.etaMax.setValue(etaMax);
    }

    public LiveData<Integer> getEtaMax() {
        return etaMax;
    }
}
