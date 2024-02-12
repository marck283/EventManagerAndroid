package it.disi.unitn.lpsmt.lasagna.eventinfo.qr_code_scan;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import it.disi.unitn.lpsmt.lasagna.eventinfo.registeredEvent.ticket.TicketInfo;

public class QRCodeRenderingViewModel extends ViewModel {
    public void getBarcode(@NonNull Fragment f, @NonNull View v, @NonNull String eventId, @NonNull String userId, @NonNull String data, @NonNull String ora) {
        if(!eventId.equals("") && !userId.equals("") && !data.equals("") && !ora.equals("")) {
            TicketInfo info = new TicketInfo(f, v, eventId, userId, data, ora);
            info.start();
        }
    }
}