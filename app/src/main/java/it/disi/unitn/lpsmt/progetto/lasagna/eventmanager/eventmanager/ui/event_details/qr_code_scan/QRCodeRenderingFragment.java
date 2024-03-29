package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.qr_code_scan;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.disi.unitn.lpsmt.lasagna.eventinfo.qr_code_scan.QRCodeRenderingViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;

public class QRCodeRenderingFragment extends DialogFragment {

    private final String eventId, userId, data, ora;

    private View v;

    public QRCodeRenderingFragment(@NonNull Bundle b) {
        eventId = b.getString("eventId");
        userId = b.getString("userId");
        data = b.getString("data");
        ora = b.getString("ora");
    }

    @NonNull
    public static QRCodeRenderingFragment newInstance(@NonNull Bundle b) {
        return new QRCodeRenderingFragment(b);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_q_r_code_rendering, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        QRCodeRenderingViewModel mViewModel = new ViewModelProvider(this).get(QRCodeRenderingViewModel.class);
        mViewModel.getBarcode(this, v, eventId, userId, data, ora);
    }

}