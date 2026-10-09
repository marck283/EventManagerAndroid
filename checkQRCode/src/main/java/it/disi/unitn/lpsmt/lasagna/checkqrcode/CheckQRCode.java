package it.disi.unitn.lpsmt.lasagna.checkqrcode;

import it.disi.unitn.lpsmt.lasagna.network.repository.TicketRepository;

public class CheckQRCode {

    public void checkQRCode(String qrCode, String eventoId, String day, String hour, QRCodeCallback callback) {
        TicketRepository ticketRepo = new TicketRepository(new TicketRepository.TicketActionCallback() {
            @Override
            public void onSuccess(int statusCode) {
                callback.handleResponseCode(statusCode);
            }

            @Override
            public void onError(int statusCode, String errorMessage) {
                callback.handleResponseCode(statusCode);
            }
        });
        ticketRepo.checkQRCode(qrCode, eventoId, day, hour);
    }
}