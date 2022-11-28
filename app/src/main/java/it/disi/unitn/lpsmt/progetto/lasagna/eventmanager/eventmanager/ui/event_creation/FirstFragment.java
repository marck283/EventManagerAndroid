package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.FragmentFirstBinding;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.listeners.SpinnerOnItemSelectedListener;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private EventViewModel evm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.button5.setOnClickListener(c -> {
            evm.setNomeAtt(((EditText)requireActivity().findViewById(R.id.nomeAtt)).getText().toString());
            Navigation.findNavController(binding.button5).navigate(R.id.action_FirstFragment_to_SecondFragment);
        });
    }

    public void onStart() {
        super.onStart();

        evm = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        //Richiedo un'istanza di Spinner (menù dropdown). Maggiori informazioni qui:
        // https://developer.android.com/develop/ui/views/components/spinner#java
        Spinner spinner = requireActivity().findViewById(R.id.planets_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        /*DropdownSpinnerAdapter a = new DropdownSpinnerAdapter(requireContext(),
                R.array.category_spinner_array, android.R.layout.simple_spinner_item);
        a.create(android.R.layout.simple_spinner_dropdown_item);*/

        ArrayAdapter<CharSequence> a = ArrayAdapter.createFromResource(requireContext(),
                R.array.category_spinner_array, android.R.layout.simple_spinner_item);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(a);

        SpinnerOnItemSelectedListener itemSelected = new SpinnerOnItemSelectedListener();
        spinner.setOnItemSelectedListener(itemSelected);
        itemSelected.getItem().observe(requireActivity(), o -> evm.setCategoria((String) o));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}