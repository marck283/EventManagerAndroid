package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_list;

import static android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET;

import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.FragmentEventListBinding;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.NetworkCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.NavigationSharedViewModel;

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

        if(savedInstanceState != null && savedInstanceState.getString("accessToken") != null) {
            idToken = savedInstanceState.getString("accessToken");
        }

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

            NetworkRequest req = new NetworkRequest.Builder()
                    .addCapability(NET_CAPABILITY_INTERNET)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .build();
            NetworkCallback nc = new NetworkCallback(requireActivity());
            ConnectivityManager connectivityManager = requireActivity().getSystemService(ConnectivityManager.class);
            connectivityManager.registerNetworkCallback(req, nc);

            eventListViewModel.getEvents(root, idToken);
            RecyclerView rv = requireActivity().findViewById(R.id.recycler_view);
            if(rv != null) {
                rv.invalidate();
            }

            connectivityManager.unregisterNetworkCallback(nc);
        });
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