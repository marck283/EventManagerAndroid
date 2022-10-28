package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NavigationSharedViewModel extends ViewModel {
    private MutableLiveData<String> idToken = new MutableLiveData<>();

    public void setToken(@NonNull String token) {
        idToken.setValue(token);
    }

    public LiveData<String> getToken() {
        return idToken;
    }
}
