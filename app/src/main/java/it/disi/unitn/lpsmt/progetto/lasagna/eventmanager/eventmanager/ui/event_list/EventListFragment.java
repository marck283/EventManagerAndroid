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
    private String idToken;


    /*@NonNull
    public static EventListFragment newInstance(@NonNull String token) {
        //La chiamata a questo metodo, in questo caso, serve semplicemente a portare l'access token di Google all'interno dello
        //scope di questo Fragment.
        EventListFragment f = new EventListFragment();
        Bundle b = new Bundle();
        b.putString("accessToken", token);
        f.setArguments(b);
        return f;
    }*/

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventListBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        if(savedInstanceState != null && savedInstanceState.getString("accessToken") != null) {
            idToken = savedInstanceState.getString("accessToken");
        }

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
        outState.putString("accessToken", idToken);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}