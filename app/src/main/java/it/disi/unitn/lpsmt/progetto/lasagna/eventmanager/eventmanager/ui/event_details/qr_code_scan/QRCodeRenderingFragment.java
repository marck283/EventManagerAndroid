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

    private String eventId, userId, data, ora;

    private View v;

    @NonNull
    public static QRCodeRenderingFragment newInstance(@NonNull Bundle b) {
        QRCodeRenderingFragment fragment = new QRCodeRenderingFragment();
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            eventId = args.getString("eventId");
            userId = args.getString("userId");
            data = args.getString("data");
            ora = args.getString("ora");
        }
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
        mViewModel.getBarcode(this, v, eventId, userId, data, ora, R.id.qrCode);
    }

}