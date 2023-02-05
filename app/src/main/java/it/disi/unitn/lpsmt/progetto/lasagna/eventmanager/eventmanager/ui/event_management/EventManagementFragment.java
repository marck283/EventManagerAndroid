package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_management;

import static android.app.Activity.RESULT_OK;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
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
                            ((NavigationDrawerActivity)requireActivity()).updateUI("logout",
                                    "", "", false);
                        }
                    }
                });

        NetworkCallback callback = new NetworkCallback(requireActivity());
        if(callback.isOnline(requireActivity())) {
            mViewModel.getOrgEvents(this, view, userJwt, launcher);
        } else {
            callback.registerNetworkCallback();
            callback.addDefaultNetworkActiveListener(() -> mViewModel.getOrgEvents(this, view, userJwt, launcher));
            callback.unregisterNetworkCallback();
            AlertDialog dialog = new AlertDialog.Builder(requireActivity()).create();
            dialog.setTitle(R.string.no_connection);
            dialog.setMessage(getString(R.string.no_connection_message));
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
            dialog.show();
        }
    }

}