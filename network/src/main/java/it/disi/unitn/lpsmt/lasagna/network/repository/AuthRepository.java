package it.disi.unitn.lpsmt.lasagna.network.repository;

import androidx.annotation.NonNull;
import it.disi.unitn.lpsmt.lasagna.network.client.RetrofitClient;
import it.disi.unitn.lpsmt.lasagna.network.model.CsrfResponse;
import it.disi.unitn.lpsmt.lasagna.network.model.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    public interface AuthResultCallback {
        void onSuccess(LoginResponse user);
        void onError(String errorMessage);
    }

    public void loginWithGoogle(String googleIdToken, AuthResultCallback callback) {
        // Step 1: Fetch CSRF Token
        RetrofitClient.getInstance().getAuthApi().getCsrfToken().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CsrfResponse> call, @NonNull Response<CsrfResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String csrfToken = response.body().getCsrfToken();

                    // Step 2: Post CSRF + Google Token to login
                    performGoogleLogin(csrfToken, googleIdToken, callback);
                } else {
                    callback.onError("CSRF Token Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CsrfResponse> call, @NonNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    private void performGoogleLogin(String csrfToken, String googleIdToken, AuthResultCallback callback) {
        RetrofitClient.getInstance().getAuthApi().loginWithGoogle(csrfToken, googleIdToken)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            LoginResponse user = response.body();

                            // Automatically set token for all future Retrofit calls
                            RetrofitClient.getInstance().setAccessToken(user.getToken());

                            callback.onSuccess(user);
                        } else {
                            callback.onError("Login failed: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }
}