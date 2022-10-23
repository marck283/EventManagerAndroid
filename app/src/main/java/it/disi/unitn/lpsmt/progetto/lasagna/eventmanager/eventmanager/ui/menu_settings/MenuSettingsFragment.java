package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.menu_settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.BuildConfig;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;

public class MenuSettingsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MenuSettingsViewModel menuSettingsViewModel =
                new ViewModelProvider(this).get(MenuSettingsViewModel.class);

        return inflater.inflate(R.layout.fragment_menu_settings, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        //Ancora da implementare: schermata dettaglio profilo e abilitazione visualizzazione
        //numero di telefono in dettaglio profilo tramite lo switch presente nel layout di questo
        //Fragment.
        view.findViewById(R.id.button2).setOnClickListener(v -> showNotImplementedDialog());
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