package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.menu_settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.BuildConfig;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.FragmentMenuSettingsBinding;

public class MenuSettingsFragment extends Fragment {
    private FragmentMenuSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MenuSettingsViewModel menuSettingsViewModel =
                new ViewModelProvider(this).get(MenuSettingsViewModel.class);

        binding = FragmentMenuSettingsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        //Ancora da implementare: schermata dettaglio profilo e abilitazione visualizzazione
        //numero di telefono in dettaglio profilo tramite lo switch presente nel layout di questo
        //Fragment.
        view.findViewById(R.id.button2).setOnClickListener(v -> Navigation.findNavController(requireActivity().findViewById(R.id.nav_host_fragment_content_navigation_drawer))
                .navigate(R.id.action_nav_user_settings_to_nav_user_profile));
        view.findViewById(R.id.button4).setOnClickListener(v -> showInfo());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void showNotImplementedDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this.requireContext()).create();
        dialog.setTitle(R.string.not_implemented);
        dialog.setMessage(getString(R.string.not_implemented));
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", (dialog1, which) -> dialog1.dismiss());
        dialog.show();
    }

    private void showInfo() {
        AlertDialog dialog = new AlertDialog.Builder(this.requireContext()).create();
        dialog.setTitle(R.string.app_version);
        dialog.setMessage(getString(R.string.app_version) + ": " + BuildConfig.VERSION_NAME);
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", (dialog1, which) -> dialog1.dismiss());
        dialog.show();
    }
}