package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.menu_settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MenuSettingsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MenuSettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is menu_settings fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}