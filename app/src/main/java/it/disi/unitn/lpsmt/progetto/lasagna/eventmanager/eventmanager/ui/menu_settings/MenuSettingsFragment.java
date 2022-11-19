package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.menu_settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.switchmaterial.SwitchMaterial;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.BuildConfig;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;

public class MenuSettingsFragment extends Fragment {
    private MenuSettingsViewModel menuSettingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        menuSettingsViewModel =
                new ViewModelProvider(requireActivity()).get(MenuSettingsViewModel.class);

        return inflater.inflate(R.layout.fragment_menu_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        view.findViewById(R.id.button2).setOnClickListener(v -> Navigation.findNavController(requireActivity().findViewById(R.id.nav_host_fragment_content_navigation_drawer))
                .navigate(R.id.action_nav_user_settings_to_nav_user_profile));
        view.findViewById(R.id.button4).setOnClickListener(v -> showInfo());
        ((SwitchMaterial)view.findViewById(R.id.switch1)).setOnCheckedChangeListener((c, c1) -> menuSettingsViewModel.setChecked(c1));
        if(savedInstanceState != null) {
            ((SwitchMaterial)view.findViewById(R.id.switch1)).setChecked(savedInstanceState.getBoolean("checked"));
        } else {
            ((SwitchMaterial)view.findViewById(R.id.switch1)).setChecked(menuSettingsViewModel.getChecked().getValue());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void showInfo() {
        AlertDialog dialog = new AlertDialog.Builder(this.requireContext()).create();
        dialog.setTitle(R.string.app_version);
        dialog.setMessage(getString(R.string.app_version) + ": " + BuildConfig.VERSION_NAME);
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", (dialog1, which) -> dialog1.dismiss());
        dialog.show();
    }

    public void onSaveInstanceState(@NonNull Bundle b) {
        b.putBoolean("checked", menuSettingsViewModel.getChecked().getValue());
    }
}