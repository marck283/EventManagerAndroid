package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.newDate;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.jetbrains.annotations.Contract;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;

public class NewDateFragment extends Fragment {

    private NewDateViewModel mViewModel;

    @NonNull
    @Contract(" -> new")
    public static NewDateFragment newInstance() {
        return new NewDateFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_date, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NewDateViewModel.class);
        Button b = requireActivity().findViewById(R.id.button3);
        b.setOnClickListener(c -> {
            EditText t = requireActivity().findViewById(R.id.begin_date);
            EditText t1 = requireActivity().findViewById(R.id.begin_time);
            EditText t2 = requireActivity().findViewById(R.id.venue_value);
            EditText t3 = requireActivity().findViewById(R.id.seats_value);
            if(t.getText() != null && t1.getText() != null && t2.getText() != null && t3.getText() != null) {
                //Check strings for correct formats
                try {
                    int posti = Integer.parseInt(String.valueOf(t3.getText()));
                } catch(NumberFormatException ex) {
                    AlertDialog ad = new AlertDialog.Builder(requireContext()).create();
                    ad.setTitle(R.string.incorrect_seats_format);
                    ad.setMessage(getString(R.string.incorrect_seats_format));
                    ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                    ad.show();
                }
            } else {
                //Tell user of missing or incorrect fields
            }
        });
    }

}