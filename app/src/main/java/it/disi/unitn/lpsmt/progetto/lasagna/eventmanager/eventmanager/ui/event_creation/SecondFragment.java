package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.LuogoEv;

public class SecondFragment extends Fragment {

    private int countRows = 0;
    private View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_second, container, false);

        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_second).setOnClickListener(view1 -> {
            if(countRows > 0) {
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_SecondFragment_to_eventAdditionalInfoFragment);
            } else {
                AlertDialog ad = new AlertDialog.Builder(requireContext()).create();
                ad.setTitle(R.string.one_row_required);
                ad.setMessage(getString(R.string.one_row_required_message));
                ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                ad.show();
            }
        });

        EventViewModel evm = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        for(LuogoEv l: evm.getLuogoEv()) {
            addInfo(view, l);
        }
    }

    public void onStart() {
        super.onStart();

        view.findViewById(R.id.floatingActionButton).setOnClickListener(c ->
                NavHostFragment.findNavController(this).navigate(R.id.action_SecondFragment_to_newDateFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void addInfo(@NonNull View view, @NonNull LuogoEv luogoEv) {
        //Crea una riga della tabella
        TableLayout l = view.findViewById(R.id.tableLayout);

        //Il LayoutInflater dichiarato in questo modo serve perché, altrimenti, il metodo inflate() ritornerebbe un TableLayout.
        TableRow t = (TableRow) ((TableLayout)getLayoutInflater().inflate(R.layout.tablerow, l))
                .getChildAt(l.getChildCount() - 1);

        TableTextView t1 = t.findViewById(R.id.textView32), t2 = t.findViewById(R.id.textView30),
        t3 = t.findViewById(R.id.textView31), t4 = t.findViewById(R.id.textView29);

        String[] dataArr = luogoEv.getData().split("-");
        t1.setText(dataArr[1] + "/" + dataArr[0] + "/" + dataArr[2]);
        t2.setText(luogoEv.getOra());
        t3.setText(luogoEv.getAddress());
        t4.setText(String.valueOf(luogoEv.getMaxPers()));

        ImageView modifyDestination = view.findViewById(R.id.imageView4);
        modifyDestination.setOnClickListener(c -> {
            Bundle b = new Bundle();

            b.putString("data", luogoEv.getData());
            b.putString("ora", luogoEv.getOra());
            b.putString("indirizzo", luogoEv.toString());
            b.putInt("numPosti", luogoEv.getPostiRimanenti());

            NavHostFragment.findNavController(this).navigate(R.id.action_SecondFragment_to_newDateFragment, b);
        });
        ++countRows;
    }
}