package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.NavigationDrawerActivity;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.FragmentEventListBinding;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.menu_settings.MenuSettingsFragment;

public class EventListFragment extends Fragment {

    private FragmentEventListBinding binding;
    private EventListViewModel eventListViewModel;
    private View root;

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

    public void onStart() {
        super.onStart();
        eventListViewModel.getEvents(root, "");
        requireActivity().findViewById(R.id.action_settings).setOnClickListener(l -> showSettings());
    }

    public void showSettings() {
        //Da rivedere
        MenuSettingsFragment f = new MenuSettingsFragment();
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.constraintLayout, f)
                .addToBackStack("settingsFragment")
                .commit();
    }

    /**
     * Metodo per ottenere una nuova lista di eventi pubblici.
     * @param accessToken Il token di accesso dell'utente
     */
    public void getData(String accessToken) {
        eventListViewModel.getEvents(root, accessToken);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}