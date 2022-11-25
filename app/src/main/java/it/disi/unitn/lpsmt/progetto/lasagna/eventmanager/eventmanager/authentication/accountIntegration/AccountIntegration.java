package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication.accountIntegration;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountIntegration {
    private final IntegrationInterface integrationInterface;

    public AccountIntegration() {
        Retrofit retro = new Retrofit.Builder()
                .baseUrl("https://eventmanagerzlf.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        integrationInterface = retro.create(IntegrationInterface.class);
    }

    public void googleIntegrate(@NonNull String gJwt) {
        Call<JsonObject> call = integrationInterface.googleIntegrate(new GoogleIntegration(gJwt));
        call.enqueue(new Callback<>() {
            /**
             * Invoked for a received HTTP response.
             *
             * <p>Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call {@link Response#isSuccessful()} to determine if the response indicates success.
             *
             * @param call
             * @param response
             */
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {

            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected exception
             * occurred creating the request or processing the response.
             *
             * @param call
             * @param t
             */
            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                try {
                    throw t;
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void facebookIntegrate(int fbUserId, @NonNull String fbUserToken) {
        Call<JsonObject> call = integrationInterface.fbIntegrate(new FBIntegration(fbUserId, fbUserToken));
        call.enqueue(new Callback<>() {
            /**
             * Invoked for a received HTTP response.
             *
             * <p>Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call {@link Response#isSuccessful()} to determine if the response indicates success.
             *
             * @param call
             * @param response
             */
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected exception
             * occurred creating the request or processing the response.
             *
             * @param call
             * @param t
             */
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                try {
                    throw t;
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
