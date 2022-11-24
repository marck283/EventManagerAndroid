package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.newDate;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewDateViewModel extends ViewModel {
    private final MutableLiveData<String> data, ora;
    private final MutableLiveData<Integer> posti;
    private final MutableLiveData<Boolean> ok;

    public NewDateViewModel() {
        data = new MutableLiveData<>();
        ora = new MutableLiveData<>();
        posti = new MutableLiveData<>();
        ok = new MutableLiveData<>(false);
    }

    public void setData(String data) {
        this.data.setValue(data);
    }

    public LiveData<String> getData() {
        return data;
    }

    public void setOra(String ora) {
        this.ora.setValue(ora);
    }

    public LiveData<String> getOra() {
        return ora;
    }

    public void setPosti(int posti) {
        this.posti.setValue(posti);
    }

    public LiveData<Integer> getPosti() {
        return posti;
    }

    public void setOk(boolean ok) {
        this.ok.setValue(ok);
    }

    public LiveData<Boolean> getOk() {
        return ok;
    }
}