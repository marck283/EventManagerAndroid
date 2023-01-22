package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_search;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.speechListeners.EventSpeechRecognizer;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.speechListeners.SpeechOnTouchListener;

public class EventSearchFragment extends DialogFragment {

    private EventSearchViewModel mViewModel;
    private View root;

    private SpeechRecognizer speechRecognizer;

    private Intent speechRecognizerIntent;

    public EventSearchFragment() {

    }

    @NonNull
    public static EventSearchFragment newInstance() {
        return new EventSearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_event_search, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Controllo di che siano stati garantiti i permessi necessari a registrare la voce dell'utente
        //Se i peressi non sono garantiti, allora chiamo checkPermission().
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

        mViewModel = new ViewModelProvider(requireActivity()).get(EventSearchViewModel.class);
        root.findViewById(R.id.search_for_event_name).setOnClickListener(c -> {
            EditText t1 = root.findViewById(R.id.nomeAtt2);
            if(t1.getText().toString().equals("")) {
                AlertDialog ad = new AlertDialog.Builder(requireActivity()).create();
                ad.setTitle(R.string.event_name_field_empty);
                ad.setMessage(getString(R.string.event_name_field_empty_message));
                ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                ad.show();
            } else {
                mViewModel.setEventName(t1.getText().toString());
                dismiss();
            }
        });

        root.findViewById(R.id.search_for_org_name).setOnClickListener(c -> {
            EditText t = root.findViewById(R.id.organizerName);
            if(t.getText().toString().equals("")) {
                AlertDialog ad = new AlertDialog.Builder(requireActivity()).create();
                ad.setTitle(R.string.org_name_field_empty);
                ad.setMessage(getString(R.string.org_name_field_empty_message));
                ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                ad.show();
            } else {
                mViewModel.setOrgName(t.getText().toString());
                dismiss();
            }
        });
    }

    public void onStart() {
        super.onStart();

        TextInputLayout text1 = root.findViewById(R.id.orgName);
        text1.setEndIconOnClickListener(c -> {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(root.getContext());
            speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            speechRecognizer.setRecognitionListener(new EventSpeechRecognizer(root, R.id.organizerName));

            SpeechOnTouchListener speech = new SpeechOnTouchListener(speechRecognizer, speechRecognizerIntent);
            speech.performClick();
        });

        TextInputLayout text2 = root.findViewById(R.id.nomeAtt4);
        text2.setEndIconOnClickListener(c -> {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(root.getContext());
            speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            speechRecognizer.setRecognitionListener(new EventSpeechRecognizer(root, R.id.nomeAtt2));

            SpeechOnTouchListener speech = new SpeechOnTouchListener(speechRecognizer, speechRecognizerIntent);
            speech.performClick();
        });
    }

    private void checkPermission() {
        //Richiedo i permessi per la registrazione della voce dell'utente
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 23);
    }

}