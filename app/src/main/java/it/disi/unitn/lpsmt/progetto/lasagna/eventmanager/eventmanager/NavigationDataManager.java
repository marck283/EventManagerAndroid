package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import it.disi.unitn.lpsmt.NavigationUiUpdateEvent;

public class NavigationDataManager {
    private static NavigationDataManager instance;

    private final MutableLiveData<NavigationUiUpdateEvent> fieldUpdates = new MutableLiveData<>();

    private NavigationDataManager() {
        // Purposefully left blank here
    }

    public static synchronized NavigationDataManager getInstance() {
        if (instance == null) {
            instance = new NavigationDataManager();
        }
        return instance;
    }

    public LiveData<NavigationUiUpdateEvent> getFieldUpdates() {
        return fieldUpdates;
    }

    public void updateField(@NonNull String fieldName, @NonNull String fieldValue) {
        NavigationUiUpdateEvent event = new NavigationUiUpdateEvent(fieldName, fieldValue);
        fieldUpdates.postValue(event);
    }
}
