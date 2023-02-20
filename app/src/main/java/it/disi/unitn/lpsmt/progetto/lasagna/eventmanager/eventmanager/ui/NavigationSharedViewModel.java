package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.NavigationDrawerActivity;

public class NavigationSharedViewModel extends ViewModel {
    private final MutableLiveData<String> idToken = new MutableLiveData<>();

    public void init(@NonNull NavigationDrawerActivity a) {
        SharedPreferences prefs = a.getSharedPreferences(
                "it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.AccTok", Context.MODE_PRIVATE);
        idToken.setValue(prefs.getString("accessToken", ""));
    }

    public void setToken(@NonNull String token) {
        idToken.setValue(token);
    }

    public LiveData<String> getToken() {
        return idToken;
    }
}
