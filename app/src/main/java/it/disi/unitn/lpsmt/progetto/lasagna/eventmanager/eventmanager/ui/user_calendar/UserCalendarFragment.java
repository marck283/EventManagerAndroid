package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.NavigationSharedViewModel;

public class UserCalendarFragment extends Fragment {

    private View view;
    private NavigationSharedViewModel vm;

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
            b.putString("idToken", vm.getToken().getValue());
            b.putInt("day", d);
            b.putInt("month", m + 1);
            b.putInt("year", y);
            Navigation.findNavController(view).navigate(R.id.action_nav_user_calendar_to_user_calendar_dialog, b);
        });
    }
}