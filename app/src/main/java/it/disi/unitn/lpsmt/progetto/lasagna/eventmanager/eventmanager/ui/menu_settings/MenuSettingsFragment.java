package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.menu_settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.FragmentMenuSettingsBinding;

public class MenuSettingsFragment extends Fragment {

    private FragmentMenuSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MenuSettingsViewModel menuSettingsViewModel =
                new ViewModelProvider(this).get(MenuSettingsViewModel.class);

        binding = FragmentMenuSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        menuSettingsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}