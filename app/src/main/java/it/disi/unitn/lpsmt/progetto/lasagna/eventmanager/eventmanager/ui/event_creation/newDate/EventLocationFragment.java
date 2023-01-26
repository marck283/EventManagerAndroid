package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.newDate;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.jetbrains.annotations.Contract;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.EventViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.spinnerImplementation.SpinnerOnItemSelectedListener;

public class EventLocationFragment extends DialogFragment {

    private EventLocationViewModel mViewModel;
    private EventViewModel evm;
    private NewDateViewModel ndvm;

    @NonNull
    @Contract(" -> new")
    public static EventLocationFragment newInstance() {
        return new EventLocationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_location, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(requireActivity()).get(EventLocationViewModel.class);
        mViewModel.setDialogFragment(this);

        evm = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        ndvm = new ViewModelProvider(requireActivity()).get(NewDateViewModel.class);

        Spinner spinner = view.findViewById(R.id.province);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> a = ArrayAdapter.createFromResource(requireContext(),
                R.array.province_spinner_array, android.R.layout.simple_spinner_item);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(a);

        SpinnerOnItemSelectedListener itemSelected = new SpinnerOnItemSelectedListener();
        spinner.setOnItemSelectedListener(itemSelected);

        Button b = view.findViewById(R.id.button6);
        b.setOnClickListener(c -> {
            EditText t = view.findViewById(R.id.location_address);
            EditText t1 = view.findViewById(R.id.house_number);
            EditText t2 = view.findViewById(R.id.location_city);
            EditText t3 = view.findViewById(R.id.zipcode);

            spinner.setOnItemSelectedListener(itemSelected);

            itemSelected.getItem().observe(requireActivity(), o -> {
                if(o instanceof String && !o.equals("") && !o.equals("---")) {
                    mViewModel.setProvincia((String) o);
                    mViewModel.parseAddress(evm.getPrivEvent(), t, t1, t2, t3, evm, ndvm);
                } else {
                    AlertDialog ad = new AlertDialog.Builder(requireContext()).create();
                    ad.setTitle(R.string.no_province_selected);
                    ad.setMessage(getString(R.string.invalid_province));
                    ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                    ad.show();
                }
            });
        });
    }

    public void onStart() {
        super.onStart();
    }

}