package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.newDate;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

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
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.listeners.SpinnerOnItemSelectedListener;

public class EventLocationFragment extends DialogFragment {

    private EventLocationViewModel mViewModel;
    private View view;

    @NonNull
    @Contract(" -> new")
    public static EventLocationFragment newInstance() {
        return new EventLocationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_location, container, false);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        NewDateViewModel nd = new ViewModelProvider(requireActivity()).get(NewDateViewModel.class);
        mViewModel = new EventLocationViewModel(this, nd);

        Button b = view.findViewById(R.id.button6);
        b.setOnClickListener(c -> {
            EditText t = view.findViewById(R.id.location_address);
            EditText t1 = view.findViewById(R.id.house_number);
            EditText t2 = view.findViewById(R.id.location_city);
            EditText t3 = view.findViewById(R.id.zipcode);
            Spinner t4 = view.findViewById(R.id.province);

            SpinnerOnItemSelectedListener itemSelected = new SpinnerOnItemSelectedListener();
            t4.setOnItemSelectedListener(itemSelected);

            //Perché qui il valore non viene sempre aggiornato?
            itemSelected.getItem().observe(requireActivity(), o -> mViewModel.setProvincia((String) o));
            mViewModel.parseAddress(t, t1, t2, t3);
        });
    }

    public void onStart() {
        super.onStart();

        Spinner spinner = view.findViewById(R.id.province);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> a = ArrayAdapter.createFromResource(requireContext(),
                R.array.province_spinner_array, android.R.layout.simple_spinner_item);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(a);

        SpinnerOnItemSelectedListener itemSelected = new SpinnerOnItemSelectedListener();
        spinner.setOnItemSelectedListener(itemSelected);
    }

}