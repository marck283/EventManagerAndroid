package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.event_restrictions;

import androidx.annotation.StringRes;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.Contract;

import java.util.Locale;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.NavigationSharedViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.EventViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.speechListeners.EventSpeechRecognizer;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.speechListeners.SpeechOnTouchListener;

public class EventRestrictionsFragment extends Fragment {

    private EventRestrictionsViewModel mViewModel;

    private EventViewModel evm;

    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;

    private NavigationSharedViewModel nsvm;

    @NonNull
    @Contract(" -> new")
    public static EventRestrictionsFragment newInstance() {
        return new EventRestrictionsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_restrictions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EventRestrictionsViewModel.class);
        evm = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        nsvm = new ViewModelProvider(requireActivity()).get(NavigationSharedViewModel.class);

        CheckBox etaMin = view.findViewById(R.id.minimumAgeCheckBox), etaMax = view.findViewById(R.id.maximumAgeCheckBox);
        TextInputLayout etaMinLayout = view.findViewById(R.id.minimum_age_text_layout);
        TextInputLayout etaMaxLayout = view.findViewById(R.id.maximum_age_text_layout);

        etaMinLayout.setVisibility(View.INVISIBLE);
        etaMaxLayout.setVisibility(View.INVISIBLE);

        if(evm.getPrivEvent()) {
            etaMin.setVisibility(View.GONE);
            etaMax.setVisibility(View.GONE);
            etaMinLayout.setVisibility(View.GONE);
            etaMaxLayout.setVisibility(View.GONE);
        } else {
            etaMin.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked) {
                    etaMinLayout.setVisibility(View.VISIBLE);
                } else {
                    etaMinLayout.setVisibility(View.INVISIBLE);
                }
            });

            etaMax.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked) {
                    etaMaxLayout.setVisibility(View.VISIBLE);
                } else {
                    etaMaxLayout.setVisibility(View.INVISIBLE);
                }
            });

            etaMinLayout.setEndIconOnClickListener(c -> {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(view.getContext());
                speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                speechRecognizer.setRecognitionListener(new EventSpeechRecognizer(view, R.id.minimum_age_text_view));

                SpeechOnTouchListener speech = new SpeechOnTouchListener(speechRecognizer, speechRecognizerIntent);
                speech.performClick();
            });

            etaMaxLayout.setEndIconOnClickListener(c -> {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(view.getContext());
                speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                speechRecognizer.setRecognitionListener(new EventSpeechRecognizer(view, R.id.maximum_age_text_view));

                SpeechOnTouchListener speech = new SpeechOnTouchListener(speechRecognizer, speechRecognizerIntent);
                speech.performClick();
            });
        }

        Button createEvent = view.findViewById(R.id.createEvent);
        createEvent.setOnClickListener(c -> {
            TextInputEditText etaMinEdit = view.findViewById(R.id.minimum_age_text_view),
                    etaMaxEdit = view.findViewById(R.id.maximum_age_text_view);
            checkEtaValues("min", etaMin, etaMinEdit, R.string.illegal_min_age, R.string.illegal_min_age_message);
            checkEtaValues("max", etaMax, etaMaxEdit, R.string.illegal_max_age, R.string.illegal_max_age_message);

            if(etaMin.isChecked() && etaMax.isChecked() && evm.getEtaMin() > evm.getEtaMax()) {
                setAlertDialog(R.string.illegal_min_age, R.string.min_eta_gt_max_eta_message);
            } else {
                //Valori OK, ora crea l'evento...
                SharedPreferences prefs = requireActivity().getSharedPreferences("AccTok", Context.MODE_PRIVATE);
                mViewModel.createPublicEvent(this, prefs.getString("accessToken", ""), evm);
            }
        });
    }

    private void checkEtaValues(String which, @NonNull CheckBox eta, TextInputEditText etaEdit,
                                @StringRes int title, @StringRes int message) {
        try {
            if(eta.isChecked()) {
                if(etaEdit.getText() == null || etaEdit.getText().toString().equals("")) {
                    setAlertDialog(title, message);
                } else {
                    if(which.equals("min")) {
                        evm.setEtaMin(Integer.parseInt(etaEdit.getText().toString()));
                    } else {
                        evm.setEtaMax(Integer.parseInt(etaEdit.getText().toString()));
                    }
                }
            }
        } catch(NumberFormatException ex) {
            setAlertDialog(title, message);
        }
    }

    private void setAlertDialog(@StringRes int title, @StringRes int message) {
        AlertDialog dialog = new AlertDialog.Builder(requireActivity()).create();
        dialog.setTitle(title);
        dialog.setMessage(getString(message));
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
        dialog.show();
    }

    public void onDestroy() {
        super.onDestroy();

        if(speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }

}