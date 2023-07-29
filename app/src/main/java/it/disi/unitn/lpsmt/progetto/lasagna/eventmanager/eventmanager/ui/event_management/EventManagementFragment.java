package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_management;

import static android.app.Activity.RESULT_OK;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
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
import it.disi.unitn.lpsmt.lasagna.network.NetworkCallback;
import it.disi.unitn.lpsmt.lasagna.sharedprefs.sharedpreferences.SharedPrefs;

public class EventManagementFragment extends Fragment {

    private EventManagementViewModel mViewModel;

    private String userJwt;

    private SharedPrefs prefs;

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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Bundle b = getArguments();
        if (b != null) {
            userJwt = b.getString("userJwt");
        }

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
                        case RESULT_OK -> {
                            userJwt = prefs.getString("accessToken");
                            searchEvents(mViewModel.getEvName().getValue(), view, null);
                        }
                        case Activity.RESULT_CANCELED -> {
                            Activity activity = getActivity();
                            if (activity != null && isAdded()) {
                                prefs.setString("accessToken", "");
                                prefs.apply();
                                Navigation.findNavController(view).navigate(R.id.action_eventManagement_to_nav_event_list);
                                ((NavigationDrawerActivity) requireActivity()).updateUI("logout",
                                        "", "", "", false);
                            }
                        }
                    }
                });
    }

    public void onStart() {
        super.onStart();

        Activity activity = getActivity();
        if(activity != null && isAdded()) {
            callback = new NetworkCallback(requireActivity());
            prefs = new SharedPrefs(
                    "it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.AccTok", requireActivity());
            if (callback.isOnline(requireActivity())) {
                searchEvents(null, view, launcher);
            } else {
                DBOrgEvents orgEvs = new DBOrgEvents(this, "getAll", view.findViewById(R.id.eventRecyclerView));
                orgEvs.start();
                callback.registerNetworkCallback();
                callback.addDefaultNetworkActiveListener(() -> searchEvents(null, view, launcher));
                callback.unregisterNetworkCallback();
            }
        }

        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(c -> {
            Bundle b = new Bundle();
            b.putString("parent", "EventManagementFragment");
            Navigation.findNavController(view).navigate(R.id.action_eventManagement_to_eventSearchFragment, b);
        });


        mViewModel.getEvName().observe(getViewLifecycleOwner(), o -> {
            Activity activity1 = getActivity();
            if(activity1 != null && isAdded()) {
                if (callback.isOnline(requireActivity())) {
                    searchEvents(o, view, launcher);
                } else {
                    DBOrgEvents orgEvs;
                    if (o == null) {
                        orgEvs = new DBOrgEvents(this, "getAll", view.findViewById(R.id.eventRecyclerView));
                    } else {
                        orgEvs = new DBOrgEvents(this, "getEventsByName", o,
                                view.findViewById(R.id.eventRecyclerView));
                    }
                    orgEvs.start();
                }
            }
        });
    }

    private void searchEvents(@Nullable String evName,
                              @NonNull View view, @Nullable ActivityResultLauncher<Intent> launcher) {
        if (evName != null && !evName.equals("")) {
            mViewModel.getOrgEvents(this, view, userJwt, evName, launcher);
        } else {
            mViewModel.getOrgEvents(this, view, userJwt, launcher);
        }
    }
}