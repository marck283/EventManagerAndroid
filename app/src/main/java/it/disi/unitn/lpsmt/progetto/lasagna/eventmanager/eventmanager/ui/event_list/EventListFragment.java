package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.FragmentEventListBinding;

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

        //Questo Fragment e NavigationDrawerActivity usano entrambi l'Activity come scope,
        //quindi saranno associati allo stesso ViewModel.
        eventListViewModel = new ViewModelProvider(requireActivity()).get(EventListViewModel.class);
    }

    public void onStart() {
        super.onStart();
        eventListViewModel.getEvents(root, "");
    }

    public void getData(String accessToken) {
        eventListViewModel.getEvents(root, accessToken);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}