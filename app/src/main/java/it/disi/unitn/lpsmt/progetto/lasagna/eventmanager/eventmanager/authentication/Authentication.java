package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import java.util.Objects;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.data.model.LoggedInUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Authentication {
    private ServerAuthentication authentication;

    public Authentication() {
        Retrofit retro = new Retrofit.Builder()
                .baseUrl("https://71ef-213-215-189-91.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        authentication = retro.create(ServerAuthentication.class);
    }

    public void login(@NonNull String csrfToken, String googleJwt) throws Exception {
        if(Objects.equals(googleJwt, "")) {
            throw new Exception("Il token JWT di Google non può essere una stringa vuota");
        }
        Call<JsonObject> auth = authentication.authentication(csrfToken, googleJwt);
        auth.enqueue(new Callback<JsonObject>() {

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
                if (response.isSuccessful() && response.body() != null) {
                    LoggedInUser info = new LoggedInUser();
                    Log.i("response", String.valueOf(response.body()));
                    info = info.parseJSON(response.body());
                } else {
                    Log.i("null1", "Unsuccessful or null response");
                }
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
}
