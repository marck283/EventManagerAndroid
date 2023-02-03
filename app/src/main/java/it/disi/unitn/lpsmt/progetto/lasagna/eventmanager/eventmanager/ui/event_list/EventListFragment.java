package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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

    private String evName = null, orgName = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.getString("accessToken") != null) {
            idToken = args.getString("accessToken");
        } else {
            if (savedInstanceState != null && savedInstanceState.getString("accessToken") != null) {
                idToken = savedInstanceState.getString("accessToken");
            }
        }
        if(args != null) {
            if(args.getString("evName") != null) {
                evName = args.getString("evName");
            }
            if(args.getString("orgName") != null) {
                orgName = args.getString("orgName");
            }
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_event_list, container, false);

        if (savedInstanceState != null && savedInstanceState.getString("accessToken") != null) {
            idToken = savedInstanceState.getString("accessToken");
        }

        root.findViewById(R.id.eventSearch).setOnClickListener(c -> NavHostFragment.findNavController(this).navigate(R.id.action_nav_event_list_to_eventSearchFragment));

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        vm = new ViewModelProvider(requireActivity()).get(NavigationSharedViewModel.class);
        eventListViewModel = new ViewModelProvider(requireActivity()).get(EventListViewModel.class);
        esvm = new ViewModelProvider(requireActivity()).get(EventSearchViewModel.class);
    }

    public void onStart() {
        super.onStart();

        vm.getToken().observe(requireActivity(), o -> {
            idToken = o;

            RecyclerView rv = requireActivity().findViewById(R.id.recycler_view);
            if (rv != null) {
                rv.invalidate();
            }
            eventListViewModel.getEvents(this, root, idToken, evName, orgName);
        });

        esvm.getOrgName().observe(requireActivity(), o -> {
            if(o != null) {
                RecyclerView rv = requireActivity().findViewById(R.id.recycler_view);
                if (rv != null) {
                    rv.invalidate();
                }
                eventListViewModel.getEvents(this, root, idToken, evName, o);
            }
        });

        esvm.getEventName().observe(requireActivity(), o -> {
            if(o != null) {
                RecyclerView rv = requireActivity().findViewById(R.id.recycler_view);
                if (rv != null) {
                    rv.invalidate();
                }
                eventListViewModel.getEvents(this, root, idToken, o, orgName);
            }
        });
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("accessToken", idToken);
    }
}