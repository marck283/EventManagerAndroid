package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.newDate.EventLocationViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.newDate.NewDateViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.newDate.TableRowInstance;

public class SecondFragment extends Fragment {

    private int countRows;
    private NewDateViewModel nd;
    private EventViewModel evm;
    private EventLocationViewModel elvm;
    private View view;
    private ArrayList<TableRowInstance> lrows;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_second, container, false);
        if(savedInstanceState != null && savedInstanceState.getStringArrayList("lrows") != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                lrows = savedInstanceState.getParcelableArrayList("lrows", TableRowInstance.class);
            } else {
                lrows = savedInstanceState.getParcelableArrayList("lrows");
            }
            TableLayout t = view.findViewById(R.id.tableLayout);
            for(TableRowInstance t1: lrows) {
                TableRow t2 = (TableRow) getLayoutInflater().inflate(R.layout.tablerow, null);
                t.addView(t2);

                TableTextView t3 = t2.findViewById(R.id.textView32), t4 = t2.findViewById(R.id.textView30),
                        t5 = t2.findViewById(R.id.textView31), t6 = t2.findViewById(R.id.textView29);
                t3.setText(t1.getData());
                t4.setText(t1.getOra());
                t5.setText(t1.getLuogo());
                t6.setText(t1.getPosti());
            }
            countRows = lrows.size();
        } else {
            lrows = new ArrayList<>();
            countRows = 0;
        }

        Bundle b = getArguments();
        if(b != null) {
            //Aggiungi la tappa appena creata
            addInfo(view, b.getParcelable("luogoEv"));
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

        view.findViewById(R.id.floatingActionButton).setOnClickListener(c -> NavHostFragment.findNavController(this).navigate(R.id.action_SecondFragment_to_newDateFragment));

        /*elvm.getOk().observe(this, o -> {
            if(o) {
                addInfo(requireActivity().findViewById(R.id.constraintTableLayout));
            }
        });*/
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        TableLayout t = view.findViewById(R.id.tableLayout);
        for(int i = 0; i < t.getChildCount(); i++) {
            TableRow t1 = (TableRow) t.getChildAt(i);
            TextView t2 = (TextView) t1.getChildAt(0), t3 = (TextView) t1.getChildAt(1),
                    t4 = (TextView) t1.getChildAt(2), t5 = (TextView) t1.getChildAt(3);
            lrows.add(new TableRowInstance(t2.getText().toString(), t3.getText().toString(),
                    t4.getText().toString(), t5.getText().toString()));
        }
        outState.putParcelableArrayList("lrows", lrows);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        nd = null;
    }

    public void addInfo(@NonNull View view, @NonNull LuogoEv luogoEv) {
        nd = new ViewModelProvider(requireActivity()).get(NewDateViewModel.class);

        //Crea una riga della tabella
        TableLayout l = view.findViewById(R.id.tableLayout);

        //Il LayoutInflater dichiarato in questo modo serve perché, altrimenti, il metodo inflate() ritornerebbe un TableLayout.
        TableRow t = (TableRow) getLayoutInflater().inflate(R.layout.tablerow, null);
        l.addView(t);

        TableTextView t1 = t.findViewById(R.id.textView32), t2 = t.findViewById(R.id.textView30),
        t3 = t.findViewById(R.id.textView31), t4 = t.findViewById(R.id.textView29);
        t1.setText(nd.getData());
        t2.setText(nd.getOra());
        t3.setText(luogoEv.toString());
        t4.setText(nd.getPosti().toString());
        ++countRows;
    }
}