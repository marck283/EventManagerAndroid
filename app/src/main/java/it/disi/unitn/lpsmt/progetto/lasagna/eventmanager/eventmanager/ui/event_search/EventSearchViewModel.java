package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_search;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EventSearchViewModel extends ViewModel {
    @NonNull
    private MutableLiveData<String> eventName = new MutableLiveData<>(""), orgName = new MutableLiveData<>("");

    public void setEventName(@NonNull String evName) {
        eventName.setValue(evName);
    }

    public LiveData<String> getEventName() {
        return eventName;
    }

    public void setOrgName(@NonNull String orgName) {
        this.orgName.setValue(orgName);
    }

    public LiveData<String> getOrgName() {
        return orgName;
    }
}