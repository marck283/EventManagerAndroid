package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.FragmentUserCalendarBinding;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.gSignIn.GSignIn;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.NavigationSharedViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_calendar.event_dialog.EventDialog;

public class UserCalendarFragment extends Fragment {

    private FragmentUserCalendarBinding binding;
    private View view;
    private NavigationSharedViewModel vm;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

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
        v.setOnDateChangeListener((v1, d, m, y) -> {
            FragmentManager m1 = getChildFragmentManager();
            FragmentTransaction t = m1.beginTransaction();
            EventDialog e = EventDialog.newInstance(new GSignIn(requireActivity()).getAccount().getIdToken(), d, m + 1, y);
            t.add(e, "");
            t.commit();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}