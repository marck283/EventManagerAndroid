package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonObject;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents.publicEvent.PubEvList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PublicEvents {
    private Retrofit retro;
    private PublicEventsInterface pubEv;

    public PublicEvents() {
        retro = new Retrofit.Builder()
                .baseUrl("https://eventmanagerzlf.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        pubEv = retro.create(PublicEventsInterface.class);
    }

    //Next: convert from array to List
    public PubEvList getEvents(@Nullable String token, @Nullable String nomeAtt,
                                      @Nullable String categoria, @Nullable String durata,
                                      @Nullable String indirizzo, @Nullable String citta) {
        Call<JsonObject> call = pubEv.pubEv(token, nomeAtt, categoria, durata, indirizzo, citta);
        final PubEvList list = new PubEvList();
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
                PubEvList l2 = new PubEvList();
                if (response.body() != null) {
                    l2 = l2.parseJSON(response.body());
                } else {
                    Log.d("null", "null");
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
        return list;
    }
}
