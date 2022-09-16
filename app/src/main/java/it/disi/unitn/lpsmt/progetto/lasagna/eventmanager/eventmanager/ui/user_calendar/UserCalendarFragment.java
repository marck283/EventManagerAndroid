package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.FragmentUserCalendarBinding;

public class UserCalendarFragment extends Fragment {

    private FragmentUserCalendarBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UserCalendarViewModel userCalendarViewModel =
                new ViewModelProvider(this).get(UserCalendarViewModel.class);

        binding = FragmentUserCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        userCalendarViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}