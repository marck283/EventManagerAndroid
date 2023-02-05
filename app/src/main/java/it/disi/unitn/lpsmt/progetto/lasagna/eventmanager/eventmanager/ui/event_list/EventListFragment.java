package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_list;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.NetworkCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.NavigationSharedViewModel;

public class EventListFragment extends Fragment {

    private EventListViewModel eventListViewModel;
    private View root;
    private String idToken = "";
    private NavigationSharedViewModel vm;

    private final MutableLiveData<String> evName = new MutableLiveData<>(), orgName = new MutableLiveData<>();

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
                evName.setValue(args.getString("evName"));
            }
            if(args.getString("orgName") != null) {
                orgName.setValue(args.getString("orgName"));
            }
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_event_list, container, false);

        if (savedInstanceState != null && savedInstanceState.getString("accessToken") != null) {
            idToken = savedInstanceState.getString("accessToken");
        }

        Bundle b = new Bundle();
        b.putString("parent", "EventListFragment");
        root.findViewById(R.id.eventSearch).setOnClickListener(c ->
                NavHostFragment.findNavController(this).navigate(R.id.action_nav_event_list_to_eventSearchFragment, b));

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        vm = new ViewModelProvider(requireActivity()).get(NavigationSharedViewModel.class);
        eventListViewModel = new ViewModelProvider(requireActivity()).get(EventListViewModel.class);
    }

    private void setAlertDialog(@StringRes int title, @StringRes int message) {
        AlertDialog dialog = new AlertDialog.Builder(requireActivity()).create();
        dialog.setTitle(title);
        dialog.setMessage(getString(message));
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
        dialog.show();
    }

    public void onStart() {
        super.onStart();

        SharedPreferences prefs = requireActivity().getSharedPreferences("EventListFragment", Context.MODE_PRIVATE);
        evName.setValue(prefs.getString("evName", null));
        orgName.setValue(prefs.getString("orgName", null));

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("evName", "");
        editor.putString("orgName", "");
        editor.apply();

        NetworkCallback callback = new NetworkCallback(requireActivity());
        if(callback.isOnline(requireActivity())) {
            vm.getToken().observe(requireActivity(), o -> {
                if(callback.isOnline(requireActivity())) {
                    idToken = o;

                    RecyclerView rv = requireActivity().findViewById(R.id.recycler_view);
                    if (rv != null) {
                        rv.invalidate();
                    }
                    eventListViewModel.getEvents(this, root, idToken, evName.getValue(), orgName.getValue());
                } else {
                    setAlertDialog(R.string.no_connection, R.string.no_connection_message);
                }
            });

            evName.observe(this, o -> {
                if(callback.isOnline(requireActivity())) {
                    if(o != null) {
                        RecyclerView rv = requireActivity().findViewById(R.id.recycler_view);
                        if (rv != null) {
                            rv.invalidate();
                        }
                        eventListViewModel.getEvents(this, root, idToken, evName.getValue(), o);
                    }
                } else {
                    setAlertDialog(R.string.no_connection, R.string.no_connection_message);
                }
            });

            orgName.observe(this, o -> {
                if(callback.isOnline(requireActivity())) {
                    if(o != null) {
                        RecyclerView rv = requireActivity().findViewById(R.id.recycler_view);
                        if (rv != null) {
                            rv.invalidate();
                        }
                        eventListViewModel.getEvents(this, root, idToken, o, orgName.getValue());
                    }
                } else {
                    setAlertDialog(R.string.no_connection, R.string.no_connection_message);
                }
            });
        } else {
            setAlertDialog(R.string.no_connection, R.string.no_connection_message);
        }
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("accessToken", idToken);
    }
}