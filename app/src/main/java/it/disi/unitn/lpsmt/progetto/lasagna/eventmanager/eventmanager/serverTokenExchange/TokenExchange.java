package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.serverTokenExchange;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TokenExchange {
    private ServerTokenExchange stex;
    public TokenExchange() {
        Retrofit retro = new Retrofit.Builder()
                .baseUrl("https://eventmanagerzlf.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        stex = retro.create(ServerTokenExchange.class);
    }

    public void getToken(@NonNull String code, @NonNull JsonParser t) {
        Call<JsonObject> call = stex.getAccessToken(code);
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
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if(response.body() != null) {
                    if(response.isSuccessful()) {
                        t.parseJSON(response.body());
                    } else {
                        Log.i("success", "Unsuccessful operation");
                    }
                } else {
                    Log.i("null", "response is null");
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
                } catch(Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
