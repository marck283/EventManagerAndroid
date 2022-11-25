package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;

public class EventDetailsFragment extends Fragment {

    private EventDetailsViewModel mViewModel;

    //Indica il tipo della schermata (ad esempio "iscr" per utente iscritto, od "org" per "organizzatore")
    private String screenType;
    private String eventId;



    @NonNull
    public static EventDetailsFragment newInstance() {
        return new EventDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle b = getArguments();
        if(b != null) {
            screenType = b.getString("eventType");
            eventId = b.getString("eventId");
        }
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
                mViewModel.getEventInfo("pub", eventId, view, this);
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