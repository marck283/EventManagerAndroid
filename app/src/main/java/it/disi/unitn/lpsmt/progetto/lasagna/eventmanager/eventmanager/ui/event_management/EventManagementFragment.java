package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_management;

import static android.app.Activity.RESULT_OK;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.Contract;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.NavigationDrawerActivity;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;

public class EventManagementFragment extends Fragment {

    private EventManagementViewModel mViewModel;

    private String userJwt;

    @NonNull
    @Contract(" -> new")
    public static EventManagementFragment newInstance() {
        return new EventManagementFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Bundle b = getArguments();
        if(b != null) {
            userJwt = b.getString("userJwt");
        }

        return inflater.inflate(R.layout.fragment_event_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EventManagementViewModel.class);
        //Login user, then send him to the next Fragment
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    switch(result.getResultCode()) {
                        case RESULT_OK: {
                            mViewModel.getOrgEvents(this, view, userJwt, null);
                            break;
                        }
                        case Activity.RESULT_CANCELED: {
                            SharedPreferences prefs =
                                    requireActivity().getSharedPreferences("AccTok", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("accessToken", "");
                            editor.apply();
                            Navigation.findNavController(view).navigate(R.id.action_eventManagement_to_nav_event_list);
                            ((NavigationDrawerActivity)requireActivity()).updateUI("logout",
                                    "", "", false);
                        }
                    }
                });
        mViewModel.getOrgEvents(this, view, userJwt, launcher);
    }

}