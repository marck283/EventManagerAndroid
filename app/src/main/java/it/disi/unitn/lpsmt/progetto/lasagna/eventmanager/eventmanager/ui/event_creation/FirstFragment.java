package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.FragmentFirstBinding;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.speechListeners.EventSpeechRecognizer;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.speechListeners.SpeechOnTouchListener;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.spinnerImplementation.SpinnerArrayAdapter;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private EventViewModel evm;

    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.nomeAtt.setEndIconOnClickListener(c -> {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(binding.getRoot().getContext());
            speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            speechRecognizer.setRecognitionListener(new EventSpeechRecognizer(binding.getRoot(), R.id.category));

            SpeechOnTouchListener speech = new SpeechOnTouchListener(speechRecognizer, speechRecognizerIntent);
            speech.performClick();
        });
        binding.button5.setOnClickListener(c -> {
            if (binding.nomeAtt.getEditText() != null && !binding.nomeAtt.getEditText().getText().toString().equals("")) {
                if (binding.planetsSpinner.getEditText() != null && !binding.planetsSpinner.getEditText()
                        .getText().toString().equals("---")) {
                    evm.setNomeAtt(binding.nomeAtt.getEditText().getText().toString());
                    Navigation.findNavController(binding.button5).navigate(R.id.action_FirstFragment_to_SecondFragment);
                } else {
                    AlertDialog ad = new AlertDialog.Builder(requireContext()).create();
                    ad.setTitle(R.string.invalid_category);
                    ad.setMessage(getString(R.string.invalid_category_message));
                    ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                    ad.show();
                }
            } else {
                AlertDialog ad = new AlertDialog.Builder(requireContext()).create();
                ad.setTitle(R.string.event_name_required);
                ad.setMessage(getString(R.string.event_name_required_message));
                ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                ad.show();
            }
        });

        //Richiedo un'istanza di Spinner (menù dropdown). Maggiori informazioni qui:
        // https://developer.android.com/develop/ui/views/components/spinner#java
        TextInputLayout spinner = view.findViewById(R.id.planets_spinner);
        MaterialAutoCompleteTextView category = spinner.findViewById(R.id.actv);

        // Apply the adapter to the spinner
        category.setAdapter(SpinnerArrayAdapter.createFromResources(requireActivity(), R.array.category_spinner_array,
                android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item));

        category.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Nulla qui...
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (category.getText() != null && !category.getText()
                        .toString().equals("") && !category.getText()
                        .toString().equals("---")) {
                    evm.setCategoria(category.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Nulla qui...
            }
        });
        CheckBox checkBox = view.findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener((c, d) -> evm.setPrivEvent(d));
    }

    public void onStart() {
        super.onStart();

        evm = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        binding = null;
    }
}