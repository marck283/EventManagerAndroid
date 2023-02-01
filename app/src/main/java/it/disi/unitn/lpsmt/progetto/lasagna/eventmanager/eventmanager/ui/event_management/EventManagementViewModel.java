package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_management;

import android.content.Intent;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.organizedEvents.OrganizedEvents;

public class EventManagementViewModel extends ViewModel {
    public void getOrgEvents(@NonNull Fragment f, @NonNull View v, @NonNull String userJwt,
                             @Nullable ActivityResultLauncher<Intent> launcher) {
        OrganizedEvents orgEvs = new OrganizedEvents(R.id.eventRecyclerView, f, v, launcher);
        orgEvs.getOrgEvents(userJwt, null);
    }
}