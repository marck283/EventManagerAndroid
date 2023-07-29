package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.event_restrictions;

import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import it.disi.unitn.lasagna.eventcreation.EventCreation;
import it.disi.unitn.lasagna.eventcreation.viewmodel.EventViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;

public class EventRestrictionsViewModel extends ViewModel {
    public void createPublicEvent(@NonNull Fragment f, @NonNull String userJwt, @NonNull EventViewModel evm,
                                  @Nullable ActivityResultLauncher<Intent> i) {
        if(!userJwt.equals("")) {
            Log.i("jwt", userJwt);
            EventCreation creation;
            if(i == null) {
                creation = new EventCreation(f, userJwt, evm);
            } else {
                Intent loginIntent = new Intent(f.requireContext(), LoginActivity.class);
                creation = new EventCreation(f, userJwt, evm, i, loginIntent);
            }

            creation.start();
        }
    }
}