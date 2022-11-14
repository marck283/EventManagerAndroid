package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.newDate;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewDateViewModel extends ViewModel {
    private MutableLiveData<String> data, ora, luogo;
    private MutableLiveData<Integer> posti;

    public NewDateViewModel() {
        data = new MutableLiveData<>();
        ora = new MutableLiveData<>();
        luogo = new MutableLiveData<>();
        posti = new MutableLiveData<>();
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

    public void setLuogo(String indirizzo) {
        luogo.setValue(indirizzo);
    }

    public LiveData<String> getLuogo() {
        return luogo;
    }

    public void setPosti(int posti) {
        this.posti.setValue(posti);
    }

    public LiveData<Integer> getPosti() {
        return posti;
    }
}