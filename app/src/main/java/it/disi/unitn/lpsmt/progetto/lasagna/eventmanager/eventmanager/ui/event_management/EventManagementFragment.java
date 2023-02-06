package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_management;

import static android.app.Activity.RESULT_OK;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.MutableLiveData;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.Contract;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.NavigationDrawerActivity;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.NetworkCallback;

public class EventManagementFragment extends Fragment {

    private EventManagementViewModel mViewModel;

    private final MutableLiveData<String> evName = new MutableLiveData<>();

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
        if (b != null) {
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
                    switch (result.getResultCode()) {
                        case RESULT_OK: {
                            SharedPreferences prefs =
                                    requireActivity().getSharedPreferences("AccTok", Context.MODE_PRIVATE);
                            userJwt = prefs.getString("accessToken", "");
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
                            ((NavigationDrawerActivity) requireActivity()).updateUI("logout",
                                    "", "", false);
                        }
                    }
                });

        NetworkCallback callback = new NetworkCallback(requireActivity());
        //searchEvents(callback, null, view, launcher);

        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(c -> {
            Bundle b = new Bundle();
            b.putString("parent", "EventManagementFragment");
            Navigation.findNavController(view).navigate(R.id.action_eventManagement_to_eventSearchFragment, b);
        });

        SharedPreferences prefs = requireActivity().getSharedPreferences("EventManagementFragment",
                Context.MODE_PRIVATE);
        evName.setValue(prefs.getString("evName", null));
        evName.observe(getViewLifecycleOwner(), o -> searchEvents(callback, evName.getValue(), view, launcher));

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("evName", null);
        editor.apply();
    }

    private void searchEvents(@NonNull NetworkCallback callback, @Nullable String evName,
                              @NonNull View view, @Nullable ActivityResultLauncher<Intent> launcher) {
        if (callback.isOnline(requireActivity())) {
            if(evName != null && !evName.equals("")) {
                mViewModel.getOrgEvents(this, view, userJwt, evName, launcher);
            } else {
                mViewModel.getOrgEvents(this, view, userJwt, launcher);
            }
        } else {
            //Implementare qui l'ottenimento dei dati sugli eventi che l'utente ha organizzato
            //tramite l'interazione con il database
        }
    }

}