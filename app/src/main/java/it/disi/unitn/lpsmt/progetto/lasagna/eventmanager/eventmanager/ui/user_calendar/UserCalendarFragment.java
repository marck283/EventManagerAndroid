package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.FragmentUserCalendarBinding;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.NavigationSharedViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_calendar.event_dialog.EventDialog;

public class UserCalendarFragment extends Fragment {

    private FragmentUserCalendarBinding binding;
    private View view;
    private String idToken;
    private NavigationSharedViewModel vm;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UserCalendarViewModel userCalendarViewModel =
                new ViewModelProvider(this).get(UserCalendarViewModel.class);

        binding = FragmentUserCalendarBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        return view;
    }

    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) {
        vm = new ViewModelProvider(requireActivity()).get(NavigationSharedViewModel.class);
    }

    public void onStart() {
        super.onStart();

        CalendarView v = view.findViewById(R.id.calendarView);
        v.setOnDateChangeListener((v1, y, m, d) -> {
            //Click su un giorno del mese per mostrare gli eventi.
            EventDialog dialog = new EventDialog(this.requireContext(), d, m, y);

            vm.getToken().observe(requireActivity(), o -> idToken = o);
            if(idToken != null && !idToken.equals("")) {
                dialog.getEvents(idToken);
                dialog.showDialog();
            } else {
                Log.i("noToken", "no user token");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}