package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.csrfToken;

import android.util.Log;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CsrfToken {
    private final CsrfTokenRequest csrfToken;

    public CsrfToken() {
        Retrofit retro = new Retrofit.Builder()
                .baseUrl("https://eventmanagerzlf.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        csrfToken = retro.create(CsrfTokenRequest.class);
    }

    //Come associo il token CSRF alla classe di autenticazione senza dimenticare che potrebbe servirmi anche per altre classi in futuro?
    public void getCsrfToken(ApiCSRFClass token) {
        Call<JsonObject> call = csrfToken.getToken();
        call.enqueue(new Callback<JsonObject>() {

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
                //Handle the response
                if (response.isSuccessful() && response.body() != null) {
                    token.parseJSON(response.body());
                } else {
                    Log.i("null", "Unsuccessful or null response");
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
