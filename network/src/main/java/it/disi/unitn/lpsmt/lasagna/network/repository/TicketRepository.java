package it.disi.unitn.lpsmt.lasagna.network.repository;

import androidx.annotation.NonNull;
import com.google.gson.JsonObject;
import it.disi.unitn.lpsmt.lasagna.network.client.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketRepository {

    public interface TicketActionCallback {
        void onSuccess(int statusCode);
        void onError(int statusCode, String errorMessage);
    }

    public interface TicketDataCallback {
        void onSuccess(JsonObject data);
        void onError(int statusCode, String errorMessage);
    }

    private final TicketActionCallback ta_cb;

    private final Callback<ResponseBody> tacb_cb = new Callback<>() {
        @Override
        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                ta_cb.onSuccess(response.code());
            } else {
                ta_cb.onError(response.code(), "Error: " + response.code());
            }
        }

        @Override
        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
            ta_cb.onError(-1, throwable.getMessage());
        }
    };

    public TicketRepository(TicketActionCallback ta_cb) {
        this.ta_cb = ta_cb;
    }

    public void registerForPublicEvent(String eventId, String day, String time) {
        RetrofitClient.getInstance().getTicketApi().registerForPublicEvent(eventId, day, day, time).enqueue(tacb_cb);
    }

    public void checkQRCode(String qrCode, String eventoId, String day, String hour) {
        RetrofitClient.getInstance().getTicketApi().checkQRCode(qrCode, eventoId, day, hour).enqueue(tacb_cb);
    }

    public void deleteTicket(String eventId, String ticketId, String data, String ora) {
        RetrofitClient.getInstance().getTicketApi().deleteTicket(eventId, ticketId, data, ora).enqueue(tacb_cb);
    }

    public void getTicketInfo(String eventId, String giorno, String ora, TicketDataCallback td_cb) {
        RetrofitClient.getInstance().getTicketApi().getTicketInfo(eventId, giorno, ora).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    td_cb.onSuccess(response.body());
                } else {
                    td_cb.onError(response.code(), "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable throwable) {
                td_cb.onError(-1, throwable.getMessage());
            }
        });
    }
}