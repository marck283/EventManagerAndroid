package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.event_restrictions;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;

public class EventRestrictionsFragment extends Fragment {

    private EventRestrictionsViewModel mViewModel;

    public static EventRestrictionsFragment newInstance() {
        return new EventRestrictionsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_restrictions, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EventRestrictionsViewModel.class);
        // TODO: Use the ViewModel
    }

}