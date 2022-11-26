package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.menu_settings;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MenuSettingsViewModel extends ViewModel {
    @NonNull
    private final MutableLiveData<Boolean> phoneChecked = new MutableLiveData<>();

    public void setChecked(boolean val) {
        phoneChecked.setValue(val);
    }

    @NonNull
    public LiveData<Boolean> getChecked() {
        return phoneChecked;
    }
}