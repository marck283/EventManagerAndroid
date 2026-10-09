package it.disi.unitn.lpsmt.lasagna.network.repository;

import androidx.annotation.NonNull;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import it.disi.unitn.lpsmt.lasagna.network.client.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewRepository {

    public interface ReviewDataCallback {
        void onSuccess(JsonObject reviewsJson);
        void onError(int statusCode, String errorMessage);
    }

    public interface ReviewActionCallback {
        void onSuccess(int statusCode);
        void onError(int statusCode, String errorMessage);
    }

    public void getEventReviews(String eventId, @NotNull ReviewDataCallback callback) {
        RetrofitClient.getInstance().getReviewApi().getEventReviews(eventId).enqueue(new Callback<>() {
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

    public void postReview(String eventId, String title, String evaluation, String description, @NonNull ReviewActionCallback callback) {
        RetrofitClient.getInstance().getReviewApi().postReview(eventId, title, evaluation, description).enqueue(new Callback<>() {
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