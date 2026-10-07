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

    public void registerForPublicEvent(String eventId, TicketActionCallback callback) {
        RetrofitClient.getInstance().getTicketApi().registerForPublicEvent(eventId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code());
                } else {
                    callback.onError(response.code(), "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onError(-1, t.getMessage());
            }
        });
    }

    public void checkQRCode(String qrCode, TicketActionCallback callback) {
        RetrofitClient.getInstance().getTicketApi().checkQRCode(qrCode).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                callback.onSuccess(response.code());
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onError(-1, t.getMessage());
            }
        });
    }
}