package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_list;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents.PublicEvents;

public class EventListViewModel extends ViewModel {
    private final MutableLiveData<String> evName = new MutableLiveData<>(), orgName = new MutableLiveData<>(),
    category = new MutableLiveData<>(), duration = new MutableLiveData<>(), address = new MutableLiveData<>(),
    city = new MutableLiveData<>();

    public void setEvName(@NonNull String val) {
        evName.setValue(val);
    }

    public LiveData<String> getEvName() {
        return evName;
    }

    public void setCategory(@NotNull String val) {
        category.setValue(val);
    }

    public LiveData<String> getCategory() {
        return category;
    }

    public void setDuration(@NotNull String val) {
        duration.setValue(val);
    }

    public LiveData<String> getDuration() {
        return duration;
    }

    public void setAddress(@NotNull String val) {
        address.setValue(val);
    }

    public LiveData<String> getAddress() {
        return address;
    }

    public void setCity(@NotNull String val) {
        city.setValue(val);
    }

    public LiveData<String> getCity() {
        return city;
    }

    public void setOrgName(@NonNull String val) {
        orgName.setValue(val);
    }

    public LiveData<String> getOrgName() {
        return orgName;
    }

    public void getEvents(@NonNull Fragment f, @NonNull View layout, String accessToken) {
        PublicEvents pubEv = new PublicEvents(f, layout, accessToken, evName.getValue(), category.getValue(),
                duration.getValue(), address.getValue(), city.getValue(), orgName.getValue());
        pubEv.start();
    }
}