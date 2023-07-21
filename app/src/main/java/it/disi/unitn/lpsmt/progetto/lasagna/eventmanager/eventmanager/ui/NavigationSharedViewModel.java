package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.NavigationDrawerActivity;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.sharedpreferences.SharedPrefs;

public class NavigationSharedViewModel extends ViewModel {
    private final MutableLiveData<String> idToken = new MutableLiveData<>();

    public void init(@NonNull NavigationDrawerActivity a) {
        SharedPrefs prefs = new SharedPrefs("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.AccTok", a);
        idToken.setValue(prefs.getString("accessToken"));
    }

    public void setToken(@NonNull String token) {
        idToken.setValue(token);
    }

    public LiveData<String> getToken() {
        return idToken;
    }
}
