package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.menu_settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

public class MenuSettingsViewModel extends ViewModel {
    @NotNull
    private final MutableLiveData<Boolean> phoneChecked = new MutableLiveData<>(false);

    public void setChecked(boolean val) {
        phoneChecked.setValue(val);
    }

    public LiveData<Boolean> getChecked() {
        return phoneChecked;
    }
}