package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.menu_settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MenuSettingsViewModel extends ViewModel {

    private final MutableLiveData<Boolean> phoneChecked;

    public MenuSettingsViewModel() {
        phoneChecked = new MutableLiveData<>();
    }

    public void setChecked(boolean val) {
        phoneChecked.setValue(val);
    }

    public LiveData<Boolean> getChecked() {
        return phoneChecked;
    }
}