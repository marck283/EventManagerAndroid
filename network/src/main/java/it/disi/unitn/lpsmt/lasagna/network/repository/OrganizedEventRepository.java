package it.disi.unitn.lpsmt.lasagna.network.repository;

import androidx.annotation.NonNull;
import com.google.gson.JsonObject;
import it.disi.unitn.lpsmt.lasagna.network.client.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizedEventRepository {

    public interface EventListCallback {
        void onSuccess(JsonObject eventsJson);
        void onError(int statusCode, String errorMessage);
    }

    public interface ActionCallback {
        void onSuccess();
        void onError(int statusCode, String errorMessage);
    }

    public void getOrganizedEvents(EventListCallback callback) {
        RetrofitClient.getInstance().getOrganizedEventsApi().getOrganizedEvents().enqueue(new Callback<>() {
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

    public void getOrganizedEventsByName(String name, EventListCallback callback) {
        RetrofitClient.getInstance().getOrganizedEventsApi().getOrganizedEventsByName(name).enqueue(new Callback<>() {
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

    public void cancelEvent(String eventId, ActionCallback callback) {
        RetrofitClient.getInstance().getOrganizedEventsApi().cancelEvent(eventId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError(response.code(), "Cancel failed: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onError(-1, t.getMessage());
            }
        });
    }

    public void terminateEvent(String eventId, String data, String ora, ActionCallback callback) {
        RetrofitClient.getInstance().getOrganizedEventsApi().terminateEvent(eventId, data, ora).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError(response.code(), "Terminate failed: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onError(-1, t.getMessage());
            }
        });
    }
}