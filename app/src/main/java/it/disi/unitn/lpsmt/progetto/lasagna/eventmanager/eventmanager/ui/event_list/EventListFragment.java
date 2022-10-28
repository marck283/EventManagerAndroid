package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.FragmentEventListBinding;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.menu_settings.MenuSettingsFragment;

public class EventListFragment extends Fragment {

    private FragmentEventListBinding binding;
    private EventListViewModel eventListViewModel;
    private View root;
    private String idToken = "";
    private NavigationSharedViewModel vm;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null && args.getString("accessToken") != null) {
            idToken = args.getString("accessToken");
            Log.i("token3", idToken);
        } else {
            if(savedInstanceState != null && savedInstanceState.getString("accessToken") != null) {
                idToken = savedInstanceState.getString("accessToken");
            }
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventListBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        vm = new ViewModelProvider(requireActivity()).get(NavigationSharedViewModel.class);
        eventListViewModel = new ViewModelProvider(requireActivity()).get(EventListViewModel.class);
    }

    public void onStart() {
        super.onStart();
        vm.getToken().observe(requireActivity(), o -> {
            idToken = o;
            eventListViewModel.getEvents(root, idToken);
        });
        eventListViewModel.getEvents(root, "");
    }

    public void showSettings() {
        //Da rivedere perché sarebbe di competenza dell'Activity NavigationDrawerActivity
        MenuSettingsFragment f = new MenuSettingsFragment();
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.constraintLayout, f)
                .addToBackStack("settingsFragment")
                .commit();
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("accessToken", idToken);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}