package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class EventViewModel extends ViewModel {
    private final MutableLiveData<String> nomeAtt = new MutableLiveData<>();
    private final MutableLiveData<Boolean> privEvent = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> date = new MutableLiveData<>();
    private final MutableLiveData<Integer> durata = new MutableLiveData<>();
    private final MutableLiveData<LuogoEv> luogoEv = new MutableLiveData<>();
    private final MutableLiveData<String> ora = new MutableLiveData<>();
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

    public void setDate(ArrayList<String> date) {
        this.date.setValue(date);
    }

    public LiveData<ArrayList<String>> getDate() {
        return date;
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
}
