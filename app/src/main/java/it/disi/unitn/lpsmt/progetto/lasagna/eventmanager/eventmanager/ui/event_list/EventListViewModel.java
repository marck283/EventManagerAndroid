package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EventListViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public EventListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is event_list fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}