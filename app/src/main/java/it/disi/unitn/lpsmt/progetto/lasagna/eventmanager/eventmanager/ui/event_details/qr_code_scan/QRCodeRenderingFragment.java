package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.qr_code_scan;

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

public class QRCodeRenderingFragment extends Fragment {

    private QRCodeRenderingViewModel mViewModel;

    private String eventId, userId, data, ora;

    private View v;

    @NonNull
    @Contract(" -> new")
    public static QRCodeRenderingFragment newInstance() {
        return new QRCodeRenderingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle b = getArguments();
        if(b != null) {
            eventId = b.getString("eventId");
            userId = b.getString("userId");
            data = b.getString("data");
            ora = b.getString("ora");
        }

        v = inflater.inflate(R.layout.fragment_q_r_code_rendering, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(QRCodeRenderingViewModel.class);
        mViewModel.getBarcode(this, v, eventId, userId, data, ora);
    }

}