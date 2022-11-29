package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.newDate;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewDateViewModel extends ViewModel {
    private String data, ora;
    private Integer posti;
    private final MutableLiveData<Boolean> ok;

    public NewDateViewModel() {
        data = "";
        ora = "";
        posti = 0;
        ok = new MutableLiveData<>(false);
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        Log.i("data", data);
        return data;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public String getOra() {
        return ora;
    }

    public void setPosti(int posti) {
        this.posti = posti;
    }

    public Integer getPosti() {
        return posti;
    }

    public void setOk(boolean ok) {
        this.ok.setValue(ok);
    }

    public LiveData<Boolean> getOk() {
        return ok;
    }
}