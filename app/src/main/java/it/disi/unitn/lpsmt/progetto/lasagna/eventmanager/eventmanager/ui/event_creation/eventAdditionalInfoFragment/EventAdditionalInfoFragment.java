package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.eventAdditionalInfoFragment;

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

public class EventAdditionalInfoFragment extends Fragment {

    private EventAdditionalInfoViewModel mViewModel;

    @NonNull
    @Contract(" -> new")
    public static EventAdditionalInfoFragment newInstance() {
        return new EventAdditionalInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.event_creation_additional_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EventAdditionalInfoViewModel.class);
        // TODO: Use the ViewModel
    }

}