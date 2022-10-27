package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_list;

import android.os.Bundle;
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

    //Problema: come porto l'access token dall'Activity del Fragment a qui per poterlo salvare nel Bundle quando il Fragment viene posto in secondo piano?

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventListBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        eventListViewModel = new ViewModelProvider(requireActivity()).get(EventListViewModel.class);
    }

    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.getString("accessToken") != null) {
            eventListViewModel.getEvents(root, savedInstanceState.getString("accessToken"));
        } else {
            eventListViewModel.getEvents(root, "");
        }
    }

    public void onStart() {
        super.onStart();
        //requireActivity().findViewById(R.id.action_settings).setOnClickListener(l -> showSettings());
    }

    public void showSettings() {
        //Da rivedere
        MenuSettingsFragment f = new MenuSettingsFragment();
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.constraintLayout, f)
                .addToBackStack("settingsFragment")
                .commit();
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}