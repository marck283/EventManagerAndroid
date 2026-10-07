package it.disi.unitn.lpsmt.lasagna.network.repository;

import androidx.annotation.NonNull;
import com.google.gson.JsonObject;
import it.disi.unitn.lpsmt.lasagna.network.client.RetrofitClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventRepository {

    public interface EventDataCallback {
        void onSuccess(JsonObject data);
        void onError(int statusCode, String errorMessage);
    }

    public interface EventActionCallback {
        void onSuccess();
        void onError(int statusCode, String errorMessage);
    }

    public void getPublicCalendarEvents(EventDataCallback callback) {
        RetrofitClient.getInstance().getEventApi().getPublicCalendarEvents().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(response.code(), "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                callback.onError(-1, t.getMessage());
            }
        });
    }

    public void createPublicEvent(RequestBody body, EventActionCallback callback) {
        RetrofitClient.getInstance().getEventApi().createPublicEvent(body).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
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
}