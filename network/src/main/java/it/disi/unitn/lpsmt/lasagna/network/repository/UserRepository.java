package it.disi.unitn.lpsmt.lasagna.network.repository;

import androidx.annotation.NonNull;
import com.google.gson.JsonObject;
import it.disi.unitn.lpsmt.lasagna.network.client.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    public interface UserProfileCallback {
        void onSuccess(JsonObject userProfile);
        void onError(int statusCode, String errorMessage);
    }

    public void getUserProfile(UserProfileCallback callback) {
        RetrofitClient.getInstance().getUserApi().getUserProfile().enqueue(new Callback<>() {
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
}