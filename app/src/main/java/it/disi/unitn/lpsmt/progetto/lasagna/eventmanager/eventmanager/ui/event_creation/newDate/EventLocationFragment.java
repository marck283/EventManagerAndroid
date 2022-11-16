package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.newDate;

import androidx.fragment.app.DialogFragment;
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

public class EventLocationFragment extends DialogFragment {

    private EventLocationViewModel mViewModel;
    private NewDateViewModel nd;

    @NonNull
    @Contract(" -> new")
    public static EventLocationFragment newInstance() {
        return new EventLocationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_location, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(EventLocationViewModel.class);
        nd = new ViewModelProvider(requireActivity()).get(NewDateViewModel.class);
    }

}