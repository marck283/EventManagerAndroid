package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_list;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import it.disi.lasagna.navigationsvm.NavigationSharedViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.lasagna.network.NetworkCallback;
import it.disi.unitn.lpsmt.lasagna.sharedprefs.sharedpreferences.SharedPrefs;

public class EventListFragment extends Fragment {

    private EventListViewModel eventListViewModel;
    private View root;
    private String idToken = "";
    private NavigationSharedViewModel vm;

    private boolean prompt = true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.getBoolean("prompt")) {
            prompt = savedInstanceState.getBoolean("prompt");
        }

        //Aggiungo le transizioni del Fragment
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_right));
        setExitTransition(inflater.inflateTransition(R.transition.fade));
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
        Activity activity = getActivity();
        if (activity != null && isAdded()) {
            AlertDialog dialog = new AlertDialog.Builder(requireActivity()).create();
            dialog.setTitle(title);
            dialog.setMessage(getString(message));
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
            dialog.show();
        }
    }

    public void onStart() {
        super.onStart();
        Activity activity = getActivity();
        if (activity != null && isAdded()) {
            SharedPrefs prefs = new SharedPrefs("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.AccTok",
                    requireActivity());
            idToken = prefs.getString("accessToken");

            NetworkCallback callback = new NetworkCallback(requireActivity());
            if (!callback.isOnline(requireActivity())) {
                root.findViewById(R.id.eventSearch).setOnClickListener(c -> {
                    if (prompt) {
                        setAlertDialog(R.string.no_connection, R.string.no_connection_message);
                        prompt = false;
                    }
                });
                setAlertDialog(R.string.no_connection, R.string.no_connection_message_short);
            } else {
                idToken = prefs.getString("accessToken");
                eventListViewModel.getEvents(this, root, idToken);

                vm.getToken().observe(requireActivity(), o -> {
                    if(isAdded()) {
                        if (callback.isOnline(requireActivity())) {
                            if (o.equals("")) {
                                idToken = o;

                                RecyclerView rv = requireActivity().findViewById(R.id.recycler_view);
                                if (rv != null) {
                                    rv.invalidate();
                                }
                            }
                            eventListViewModel.getEvents(this, root, idToken);
                        } else {
                            if (prompt) {
                                prompt = false;
                                setAlertDialog(R.string.no_connection, R.string.no_connection_message_short);
                            }
                        }
                    }
                });

                eventListViewModel.getEvName().observe(getViewLifecycleOwner(), o -> {
                    if(isAdded()) {
                        if (callback.isOnline(requireActivity())) {
                            if (o != null && !o.equals("")) {
                                RecyclerView rv = requireActivity().findViewById(R.id.recycler_view);
                                if (rv != null) {
                                    rv.invalidate();
                                }
                                eventListViewModel.getEvents(this, root, idToken);
                            }
                        } else {
                            if (prompt) {
                                prompt = false;
                                setAlertDialog(R.string.no_connection, R.string.no_connection_message_short);
                            }
                        }
                    }
                });

                eventListViewModel.getOrgName().observe(getViewLifecycleOwner(), o -> {
                    if(isAdded()) {
                        if (callback.isOnline(requireActivity())) {
                            if (o != null && !o.equals("")) {
                                RecyclerView rv = requireActivity().findViewById(R.id.recycler_view);
                                if (rv != null) {
                                    rv.invalidate();
                                }
                                eventListViewModel.getEvents(this, root, idToken);
                            }
                        } else {
                            if (prompt) {
                                prompt = false;
                                setAlertDialog(R.string.no_connection, R.string.no_connection_message_short);
                            }
                        }
                    }
                });

                eventListViewModel.getCategory().observe(getViewLifecycleOwner(), o -> {
                    if(isAdded()) {
                        if (callback.isOnline(requireActivity())) {
                            if (o != null && !o.equals("")) {
                                RecyclerView rv = requireActivity().findViewById(R.id.recycler_view);
                                if (rv != null) {
                                    rv.invalidate();
                                }
                                eventListViewModel.getEvents(this, root, idToken);
                            }
                        } else {
                            if (prompt) {
                                prompt = false;
                                setAlertDialog(R.string.no_connection, R.string.no_connection_message_short);
                            }
                        }
                    }
                });

                eventListViewModel.getDuration().observe(getViewLifecycleOwner(), o -> {
                    if(isAdded()) {
                        if (callback.isOnline(requireActivity())) {
                            if (o != null && !o.equals("")) {
                                RecyclerView rv = requireActivity().findViewById(R.id.recycler_view);
                                if (rv != null) {
                                    rv.invalidate();
                                }
                                eventListViewModel.getEvents(this, root, idToken);
                            }
                        } else {
                            if (prompt) {
                                prompt = false;
                                setAlertDialog(R.string.no_connection, R.string.no_connection_message_short);
                            }
                        }
                    }
                });

                eventListViewModel.getAddress().observe(getViewLifecycleOwner(), o -> {
                    if(isAdded()) {
                        if (callback.isOnline(requireActivity())) {
                            if (o != null && !o.equals("")) {
                                RecyclerView rv = requireActivity().findViewById(R.id.recycler_view);
                                if (rv != null) {
                                    rv.invalidate();
                                }
                                eventListViewModel.getEvents(this, root, idToken);
                            }
                        } else {
                            if (prompt) {
                                prompt = false;
                                setAlertDialog(R.string.no_connection, R.string.no_connection_message_short);
                            }
                        }
                    }
                });

                eventListViewModel.getCity().observe(getViewLifecycleOwner(), o -> {
                    if(isAdded()) {
                        if (callback.isOnline(requireActivity())) {
                            if (o != null && !o.equals("")) {
                                RecyclerView rv = requireActivity().findViewById(R.id.recycler_view);
                                if (rv != null) {
                                    rv.invalidate();
                                }
                                eventListViewModel.getEvents(this, root, idToken);
                            }
                        } else {
                            if (prompt) {
                                prompt = false;
                                setAlertDialog(R.string.no_connection, R.string.no_connection_message_short);
                            }
                        }
                    }
                });
            }
        }
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("prompt", prompt);
    }
}