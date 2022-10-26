package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.serverTokenExchange;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses.DBUserAccessToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TokenExchange {
    private ServerTokenExchange tokenExchange;

    public TokenExchange() {
        Retrofit retro = new Retrofit.Builder()
                .baseUrl("https://eventmanagerzlf.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tokenExchange = retro.create(ServerTokenExchange.class);
    }

    public void getAccessToken(@NonNull String code, @NonNull Activity a, @NonNull String email) {
        Call<JsonObject> json = tokenExchange.getAccessTokenFromServer(code);
        json.enqueue(new Callback<JsonObject>() {

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
                synchronized(this) {
                    if(response.isSuccessful()) {
                        if(response.body() != null) {
                            GAccessToken token = new GAccessToken();
                            token = token.parseJSON(response.body());
                            DBUserAccessToken signIn = new DBUserAccessToken(a, token.getToken(), email);
                            signIn.start();
                        } else {
                            Log.i("nullAuthResponse", "response is null");
                        }
                    } else {
                        Log.i("noResponse", "Unsuccessful response. Error code: " + response.code());
                    }
                    notify();
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
                } catch(Throwable ex) {
                    Log.i("exception", ex.getMessage());
                }
            }
        });
    }
}
