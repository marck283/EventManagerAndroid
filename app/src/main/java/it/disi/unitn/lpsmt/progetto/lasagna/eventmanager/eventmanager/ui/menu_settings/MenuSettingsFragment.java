package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.menu_settings;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
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
    private View v;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        menuSettingsViewModel =
                new ViewModelProvider(requireActivity()).get(MenuSettingsViewModel.class);

        v = inflater.inflate(R.layout.fragment_menu_settings, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        view.findViewById(R.id.button2).setOnClickListener(v -> Navigation.findNavController(requireActivity().findViewById(R.id.nav_host_fragment_content_navigation_drawer))
                .navigate(R.id.action_nav_user_settings_to_nav_user_profile));
        view.findViewById(R.id.button4).setOnClickListener(v -> showInfo());
        ((SwitchMaterial)view.findViewById(R.id.switch1)).setOnCheckedChangeListener((c, c1) -> {
            menuSettingsViewModel.setChecked(c1);
            SharedPreferences sp = requireActivity().getSharedPreferences(
                    "it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.MenuSettingsSharedPreferences",
                    MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            if(menuSettingsViewModel != null && menuSettingsViewModel.getChecked().getValue() != null) {
                editor.putBoolean("showTel", menuSettingsViewModel.getChecked().getValue());
                editor.apply();
            }
        });
        if(savedInstanceState != null) {
            ((SwitchMaterial)view.findViewById(R.id.switch1)).setChecked(savedInstanceState.getBoolean("checked"));
        } else {
            //Da salvare nelle SharedPreferences per condividerne il valore tra più esecuzioni dell'applicazione
            if(menuSettingsViewModel.getChecked().getValue() != null) {
                ((SwitchMaterial)view.findViewById(R.id.switch1)).setChecked(menuSettingsViewModel.getChecked().getValue());
            } else {
                SharedPreferences sp = requireActivity().getSharedPreferences(
                        "it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.MenuSettingsSharedPreferences",
                        MODE_PRIVATE);
                ((SwitchMaterial)view.findViewById(R.id.switch1)).setChecked(sp.getBoolean("showTel", false));
            }
        }
    }

    private void showInfo() {
        AlertDialog dialog = new AlertDialog.Builder(this.requireContext()).create();
        dialog.setTitle(R.string.app_version);
        dialog.setMessage(getString(R.string.app_version) + ": " + BuildConfig.VERSION_NAME);
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", (dialog1, which) -> dialog1.dismiss());
        dialog.show();
    }

    public void onSaveInstanceState(@NonNull Bundle b) {
        if(menuSettingsViewModel.getChecked().getValue() == null) {
            menuSettingsViewModel.setChecked(((SwitchMaterial)v.findViewById(R.id.switch1)).isChecked());
        }
        b.putBoolean("checked", menuSettingsViewModel.getChecked().getValue());
    }
}