package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_calendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserCalendarViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public UserCalendarViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is user_calendar fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}