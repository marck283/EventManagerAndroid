package it.disi.lasagna.navigationsvm;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import it.disi.unitn.lpsmt.lasagna.sharedprefs.sharedpreferences.SharedPrefs;

public class NavigationSharedViewModel extends ViewModel {
    private final MutableLiveData<String> idToken = new MutableLiveData<>();

    public void init(@NonNull Activity a) {
        SharedPrefs prefs = new SharedPrefs("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.AccTok", a);
        idToken.setValue(prefs.getString("accessToken"));
    }

    public void setToken(@NonNull String token) {
        idToken.setValue(token);
    }

    public void postToken(@NotNull String token) {
        idToken.postValue(token);
    }

    public LiveData<String> getToken() {
        return idToken;
    }
}
