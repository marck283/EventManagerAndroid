package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class EventViewModel extends ViewModel {
    private MutableLiveData<String> nomeAtt = new MutableLiveData<>();
    private MutableLiveData<Boolean> privEvent = new MutableLiveData<>();
    private MutableLiveData<ArrayList<String>> date = new MutableLiveData<>();
    private MutableLiveData<Integer> durata = new MutableLiveData<>();
    private MutableLiveData<LuogoEv> luogoEv = new MutableLiveData<>();
    private MutableLiveData<String> ora = new MutableLiveData<>();
    private MutableLiveData<Integer> maxPers = new MutableLiveData<>();
    private MutableLiveData<String> categoria = new MutableLiveData<>();
    private MutableLiveData<Integer> etaMin = new MutableLiveData<>(), etaMax = new MutableLiveData<>();
}
