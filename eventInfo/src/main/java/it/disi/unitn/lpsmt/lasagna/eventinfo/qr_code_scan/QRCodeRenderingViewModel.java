package it.disi.unitn.lpsmt.lasagna.eventinfo.qr_code_scan;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import it.disi.unitn.lpsmt.lasagna.eventinfo.registeredEvent.ticket.TicketInfoCallback;
import it.disi.unitn.lpsmt.lasagna.network.client.RetrofitClient;
import it.disi.unitn.lpsmt.lasagna.network.repository.TicketRepository;

public class QRCodeRenderingViewModel extends ViewModel {

    public void getBarcode(@NonNull Fragment f, @NonNull View v, @NonNull String eventId, @NonNull String userId, @NonNull String data, @NonNull String ora) {
        if (!eventId.isEmpty() && !userId.isEmpty() && !data.isEmpty() && !ora.isEmpty()) {
            RetrofitClient.getInstance().setAccessToken(userId);
            TicketInfoCallback callback = new TicketInfoCallback(f, v);

            TicketRepository repo = new TicketRepository(null);
            repo.getTicketInfo(eventId, data, ora, new TicketRepository.TicketDataCallback() {
                @Override
                public void onSuccess(JsonObject data) {
                    callback.handleTicketSuccess(data);
                }

                @Override
                public void onError(int statusCode, String errorMessage) {
                    callback.handleTicketError(statusCode);
                }
            });
        }
    }
}