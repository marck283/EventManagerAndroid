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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.Contract;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.NavigationDrawerActivity;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses.DBOrgEvents;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.NetworkCallback;

public class EventManagementFragment extends Fragment {

    private EventManagementViewModel mViewModel;

    private String userJwt;

    private SharedPreferences prefs;

    private NetworkCallback callback;

    private View view;

    private ActivityResultLauncher<Intent> launcher;

    @NonNull
    @Contract(" -> new")
    public static EventManagementFragment newInstance() {
        return new EventManagementFragment();
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = requireActivity().getSharedPreferences("AccTok", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Bundle b = getArguments();
        if (b != null) {
            userJwt = b.getString("userJwt");
        }

        callback = new NetworkCallback(requireActivity());

        view = inflater.inflate(R.layout.fragment_event_management, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(EventManagementViewModel.class);

        //Login user, then send him to the next Fragment
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    switch (result.getResultCode()) {
                        case RESULT_OK: {
                            userJwt = prefs.getString("accessToken", "");
                            searchEvents(mViewModel.getEvName().getValue(), view, null);
                            break;
                        }
                        case Activity.RESULT_CANCELED: {
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("accessToken", "");
                            editor.apply();
                            Navigation.findNavController(view).navigate(R.id.action_eventManagement_to_nav_event_list);
                            ((NavigationDrawerActivity) requireActivity()).updateUI("logout",
                                    "", "", false);
                        }
                    }
                });

        searchEvents(null, view, launcher);

        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(c -> {
            Bundle b = new Bundle();
            b.putString("parent", "EventManagementFragment");
            Navigation.findNavController(view).navigate(R.id.action_eventManagement_to_eventSearchFragment, b);
        });

        if(callback.isOnline(requireActivity())) {
            mViewModel.getEvName().observe(getViewLifecycleOwner(), o -> searchEvents(
                    mViewModel.getEvName().getValue(), view, launcher));
        } else {
            DBOrgEvents orgEvs;
            if(mViewModel.getEvName().getValue() == null) {
                orgEvs = new DBOrgEvents(this, "getAll", view.findViewById(R.id.eventRecyclerView));
            } else {
                orgEvs = new DBOrgEvents(this, "getEventsByName", mViewModel.getEvName().getValue(),
                        view.findViewById(R.id.eventRecyclerView));
            }
            orgEvs.start();
        }
    }

    private void searchEvents(@Nullable String evName,
                              @NonNull View view, @Nullable ActivityResultLauncher<Intent> launcher) {
        if(evName != null && !evName.equals("")) {
            mViewModel.getOrgEvents(this, view, userJwt, evName, launcher);
        } else {
            mViewModel.getOrgEvents(this, view, userJwt, launcher);
        }
    }
}