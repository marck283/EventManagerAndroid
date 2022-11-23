package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.newDate.EventLocationViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.newDate.NewDateFragment;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.newDate.NewDateViewModel;

public class SecondFragment extends Fragment {

    private int countRows;
    private NewDateViewModel nd;
    private EventViewModel evm;
    private EventLocationViewModel elvm;
    private View view;
    private ArrayList<String> lrows;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        view = inflater.inflate(R.layout.fragment_second, container, false);
        if(savedInstanceState != null && savedInstanceState.getStringArrayList("lrows") != null) {
            lrows = savedInstanceState.getStringArrayList("lrows");
            countRows = lrows.size();
        } else {
            lrows = new ArrayList<>();
            countRows = 0;
        }
        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_second).setOnClickListener(view1 -> NavHostFragment.findNavController(SecondFragment.this)
                .navigate(R.id.action_SecondFragment_to_FirstFragment));

        nd = new ViewModelProvider(requireActivity()).get(NewDateViewModel.class);
        evm = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        elvm = new ViewModelProvider(this).get(EventLocationViewModel.class);

        if(savedInstanceState != null) {
            //Ripristinare le View con le TableRow come erano prima di essere distrutte per la creazione
            // della nuova data dell'evento usando il metodo addInfo scritto sotto.
        }
    }

    public void onStart() {
        super.onStart();

        view.findViewById(R.id.floatingActionButton).setOnClickListener(c -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_SecondFragment_to_newDateFragment);
            /*FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            NewDateFragment ndf = NewDateFragment.newInstance();
            ft.add(ndf, "NewDateFragment");
            ft.commit();*/
        });

        if(elvm.getOk()) {
            addInfo(requireActivity().findViewById(R.id.constraintTableLayout));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("lrows", lrows);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        nd = null;
    }

    public void addInfo(@NonNull View view) {
        //Crea una riga della tabella
        TableLayout l = view.findViewById(R.id.tableLayout);
        LayoutInflater inflater = (LayoutInflater)requireActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TableRow t = (TableRow) inflater.inflate(R.layout.tablerow, null);
        l.addView(t);
        ++countRows;
    }
}