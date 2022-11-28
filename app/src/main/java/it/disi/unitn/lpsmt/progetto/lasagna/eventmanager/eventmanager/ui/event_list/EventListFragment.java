package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_list;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.NavigationSharedViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_search.EventSearchViewModel;

public class EventListFragment extends Fragment {

    private EventListViewModel eventListViewModel;
    private View root;
    private String idToken = "";
    private NavigationSharedViewModel vm;
    private EventSearchViewModel esvm;
    private String eventName = "", orgName = ""; //Utilizzate per il filtro degli eventi
    private boolean beginning;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beginning = true;

        Bundle args = getArguments();
        if (args != null && args.getString("accessToken") != null) {
            idToken = args.getString("accessToken");
        } else {
            if (savedInstanceState != null && savedInstanceState.getString("accessToken") != null) {
                idToken = savedInstanceState.getString("accessToken");
            }
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_event_list, container, false);

        if (savedInstanceState != null && savedInstanceState.getString("accessToken") != null) {
            idToken = savedInstanceState.getString("accessToken");
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        vm = new ViewModelProvider(requireActivity()).get(NavigationSharedViewModel.class);
        eventListViewModel = new ViewModelProvider(requireActivity()).get(EventListViewModel.class);
        esvm = new ViewModelProvider(requireActivity()).get(EventSearchViewModel.class);

        root.findViewById(R.id.eventSearch).setOnClickListener(c -> NavHostFragment.findNavController(this).navigate(R.id.action_nav_event_list_to_eventSearchFragment));
    }

    public void onStart() {
        super.onStart();

        vm.getToken().observe(requireActivity(), o -> {
            idToken = o;

            RecyclerView rv = requireActivity().findViewById(R.id.recycler_view);
            if (rv != null) {
                rv.invalidate();
            }
            eventListViewModel.getEvents(root, idToken, null, null);
        });

        esvm.getOrgName().observe(requireActivity(), o -> {
            orgName = o;
            RecyclerView rv = requireActivity().findViewById(R.id.recycler_view);
            if (rv != null) {
                rv.invalidate();
            }

            if (o.equals("")) {
                o = null;
            }
            eventListViewModel.getEvents(root, idToken, null, o);
        });

        esvm.getEventName().observe(requireActivity(), o1 -> {
            RecyclerView rv = requireActivity().findViewById(R.id.recycler_view);
            if (rv != null) {
                rv.invalidate();
            }
            if (o1.equals("")) {
                o1 = null;
            }

            eventListViewModel.getEvents(root, idToken, o1, null);
        });
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("accessToken", idToken);
    }
}