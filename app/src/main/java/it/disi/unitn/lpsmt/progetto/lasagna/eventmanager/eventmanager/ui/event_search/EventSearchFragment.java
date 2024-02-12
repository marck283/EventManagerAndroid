package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_search;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_list.EventListViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_management.EventManagementViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.speechListeners.SpeechRecognizerInterface;

public class EventSearchFragment extends DialogFragment {

    private EventManagementViewModel emvm;

    private View root;

    private EventListViewModel elvm;

    private SpeechRecognizerInterface speechRecognizer;

    private String parent;

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

    private String replaceCRLF(@NotNull String val) {
        return val.replace("\r", "").replace("\n", "");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle b = getArguments();
        if(b != null) {
            parent = b.getString("parent");
        }

        //Controllo di che siano stati garantiti i permessi necessari a registrare la voce dell'utente
        //Se i permessi non sono garantiti, allora chiamo checkPermission().
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

        elvm = new ViewModelProvider(requireActivity()).get(EventListViewModel.class);
        emvm = new ViewModelProvider(requireActivity()).get(EventManagementViewModel.class);

        MaterialButton reset_filters = root.findViewById(R.id.reset_filters);
        root.findViewById(R.id.apply_filters).setOnClickListener(c -> {
            EditText t = root.findViewById(R.id.organizerName), t1 = root.findViewById(R.id.nomeAtt2),
            t2 = root.findViewById(R.id.categoryValue), t3 = root.findViewById(R.id.durationValue),
            t4 = root.findViewById(R.id.addressValue), t5 = root.findViewById(R.id.cityValue);
            if(parent.equals("EventListFragment")) {
                elvm.setEvName(replaceCRLF(t1.getText().toString()));
                elvm.setOrgName(replaceCRLF(t.getText().toString()));
                elvm.setCategory(replaceCRLF(t2.getText().toString()));
                elvm.setDuration(replaceCRLF(t3.getText().toString()));
                elvm.setAddress(replaceCRLF(t4.getText().toString()));
                elvm.setCity(replaceCRLF(t5.getText().toString()));
            } else {
                emvm.setEvName(t1.getText().toString());
            }
            dismiss();
        });

        if(!reset_filters.hasOnClickListeners()) {
            reset_filters.setOnClickListener(c -> {
                if(parent.equals("EventManagementFragment")) {
                    emvm.setEvName("");
                } else {
                    elvm.setEvName("");
                    elvm.setOrgName("");
                    elvm.setCategory("");
                    elvm.setDuration("");
                    elvm.setAddress("");
                    elvm.setCity("");
                }
                dismiss();
            });
        }
    }

    public void onStart() {
        super.onStart();

        TextInputLayout text1 = root.findViewById(R.id.orgName);
        if(parent.equals("EventListFragment")) {
            text1.setEndIconOnClickListener(c -> {
                speechRecognizer = new SpeechRecognizerInterface(root, R.id.organizerName);
                speechRecognizer.performClick();
            });
        } else {
            text1.setEnabled(false);
            text1.setVisibility(View.GONE);
        }

        TextInputLayout text2 = root.findViewById(R.id.nomeAtt4);
        text2.setEndIconOnClickListener(c -> {
            speechRecognizer = new SpeechRecognizerInterface(root, R.id.nomeAtt2);
            speechRecognizer.performClick();
        });
    }

    private void checkPermission() {
        //Richiedo i permessi per la registrazione della voce dell'utente
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 23);
    }

}