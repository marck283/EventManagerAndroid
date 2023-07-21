package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.NavigationDrawerActivity;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.sharedpreferences.SharedPrefs;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.NavigationSharedViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;

public class UserCalendarFragment extends Fragment {

    private View view;
    private NavigationSharedViewModel vm;

    private ActivityResultLauncher<Intent> launcher;

    private int d, m, y;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    switch (result.getResultCode()) {
                        case Activity.RESULT_OK: {
                            Bundle b = new Bundle();
                            b.putString("idToken", vm.getToken().getValue());
                            b.putInt("day", d);
                            b.putInt("month", m + 1);
                            b.putInt("year", y);
                            Navigation.findNavController(view).navigate(R.id.action_nav_user_calendar_to_user_calendar_dialog, b);
                            break;
                        }
                        case Activity.RESULT_CANCELED: {
                            Activity activity = getActivity();
                            if(activity != null && isAdded()) {
                                ((NavigationDrawerActivity)requireActivity()).updateUI("logout",
                                        null, null, null, false);
                                Navigation.findNavController(view).navigate(R.id.action_nav_user_calendar_to_nav_event_list);
                            }
                            break;
                        }
                    }
                });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_user_calendar, container, false);

        return view;
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        vm = new ViewModelProvider(requireActivity()).get(NavigationSharedViewModel.class);
    }

    public void onStart() {
        super.onStart();

        CalendarView v = view.findViewById(R.id.calendarView);
        v.setOnDateChangeListener((v1, d, m, y) -> {
            Bundle b = new Bundle();

            SharedPrefs prefs = new SharedPrefs("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.AccTok",
                    requireActivity());
            String token = prefs.getString("accessToken");
            if (token.equals("")) {
                //Eseguire login, poi permettere la visualizzazione degli eventi
                Intent login = new Intent(requireContext(), LoginActivity.class);
                launcher.launch(login);
            } else {
                this.d = d;
                this.m = m;
                this.y = y;
                b.putString("idToken", token);
                b.putInt("day", d);
                b.putInt("month", m + 1);
                b.putInt("year", y);
                Navigation.findNavController(view).navigate(R.id.action_nav_user_calendar_to_user_calendar_dialog, b);
            }
        });
    }
}