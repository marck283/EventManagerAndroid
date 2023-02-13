package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_management;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.organizedEvents.OrganizedEvents;

public class EventManagementViewModel extends ViewModel {
    private final MutableLiveData<String> evName = new MutableLiveData<>();

    public void setEvName(@NonNull String val) {
        evName.setValue(val);
    }

    public LiveData<String> getEvName() {
        return evName;
    }

    public void getOrgEvents(@NonNull Fragment f, @NonNull View v, @NonNull String userJwt,
                             @Nullable ActivityResultLauncher<Intent> launcher) {
        Activity activity = f.getActivity();
        if(activity != null && f.isAdded()) {
            OrganizedEvents orgEvs = new OrganizedEvents(R.id.eventRecyclerView, f, v, launcher);
            orgEvs.getOrgEvents(userJwt, null);
        }
    }

    public void getOrgEvents(@NonNull Fragment f, @NonNull View v, @NonNull String userJwt,
                             @NonNull String evName, @Nullable ActivityResultLauncher<Intent> launcher) {
        Activity activity = f.getActivity();
        if(activity != null && f.isAdded()) {
            OrganizedEvents orgEvs = new OrganizedEvents(R.id.eventRecyclerView, f, v, evName, launcher);
            orgEvs.getOrgEventsWithName(userJwt);
        }
    }
}