package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_search;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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

        root.findViewById(R.id.search_for_event_name).setOnClickListener(c -> {
            EditText t1 = root.findViewById(R.id.nomeAtt2);
            if(t1.getText().toString().equals("")) {
                Activity activity = getActivity();
                if(activity != null && isAdded()) {
                    AlertDialog ad = new AlertDialog.Builder(requireActivity()).create();
                    ad.setTitle(R.string.event_name_field_empty);
                    ad.setMessage(getString(R.string.event_name_field_empty_message));
                    ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                    ad.show();
                }
            } else {
                if(parent.equals("EventListFragment")) {
                    elvm.setEvName(t1.getText().toString());
                } else {
                    emvm.setEvName(t1.getText().toString());
                }
                dismiss();
            }
        });

        MaterialButton searchForOrgName = root.findViewById(R.id.search_for_org_name);
        TextInputLayout orgName = root.findViewById(R.id.orgName);
        if(parent.equals("EventManagementFragment")) {
            searchForOrgName.setEnabled(false);
            searchForOrgName.setVisibility(View.GONE);
            orgName.setEnabled(false);
            orgName.setVisibility(View.GONE);
        } else {
            searchForOrgName.setEnabled(true);
            searchForOrgName.setVisibility(View.VISIBLE);
            orgName.setEnabled(true);
            orgName.setVisibility(View.VISIBLE);
            if(!searchForOrgName.hasOnClickListeners()) {
                searchForOrgName.setOnClickListener(c -> {
                    EditText t = root.findViewById(R.id.organizerName);
                    if(t.getText().toString().equals("")) {
                        Activity activity = getActivity();
                        if(activity != null && isAdded()) {
                            AlertDialog ad = new AlertDialog.Builder(requireActivity()).create();
                            ad.setTitle(R.string.org_name_field_empty);
                            ad.setMessage(getString(R.string.org_name_field_empty_message));
                            ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                            ad.show();
                        }
                    } else {
                        elvm.setOrgName(t.getText().toString());
                        dismiss();
                    }
                });
            }
        }
    }

    public void onStart() {
        super.onStart();

        TextInputLayout text1 = root.findViewById(R.id.orgName);
        text1.setEndIconOnClickListener(c -> {
            speechRecognizer = new SpeechRecognizerInterface(root, R.id.organizerName);
            speechRecognizer.performClick();
        });

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