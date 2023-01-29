package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.eventAdditionalInfoFragment;

import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.event_creation.EventCreation;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.EventViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;

public class EventAdditionalInfoViewModel extends ViewModel {
    public void createPrivateEvent(@NonNull Fragment f, @NonNull String jwt, @NonNull EventViewModel evm,
                                   @Nullable ActivityResultLauncher<Intent> launcher) {
        Intent loginIntent = new Intent(f.requireContext(), LoginActivity.class);
        EventCreation creation = new EventCreation(f, jwt, evm, launcher, loginIntent);
        creation.start();
    }
}
