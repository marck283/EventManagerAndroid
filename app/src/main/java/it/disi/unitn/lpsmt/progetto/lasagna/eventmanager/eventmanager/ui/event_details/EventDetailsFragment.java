package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.Contract;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;

public class EventDetailsFragment extends Fragment {

    private EventDetailsViewModel mViewModel;

    //Indica il tipo della schermata (ad esempio "iscr" per utente iscritto, od "org" per "organizzatore")
    private String screenType;
    private String eventId;

    public EventDetailsFragment(String type, String eventId) {
        screenType = type;
        this.eventId = eventId;
    }

    @NonNull
    public static EventDetailsFragment newInstance(String type, String eventId) {
        return new EventDetailsFragment(type, eventId);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        switch(screenType) {
            case "pub": {
                return inflater.inflate(R.layout.public_event_info, container, false);
            }
            case "iscr": {
                //Qualcosa
            }
            case "org": {
                //Qualcosa
            }
        }

        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EventDetailsViewModel.class);

        switch(screenType) {
            case "pub": {
                mViewModel.getEventInfo("pub", eventId);
                break;
            }
            case "iscr": {
                //Qualcosa
            }
            case "org": {
                //Qualcosa
            }
        }
    }

}