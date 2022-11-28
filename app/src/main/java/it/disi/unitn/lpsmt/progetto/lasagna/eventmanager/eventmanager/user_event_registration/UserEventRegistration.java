package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.user_event_registration;

import android.app.AlertDialog;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.EventDetailsFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserEventRegistration {
    private UserEventRegistrationInterface ueInterface;

    public UserEventRegistration() {
        Retrofit retro = new Retrofit.Builder()
                .baseUrl("https://eventmanagerzlf.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ueInterface = retro.create(UserEventRegistrationInterface.class);
    }

    public void registerUser(@NonNull String accessToken, @NonNull String eventId, @NonNull EventDetailsFragment f) {
        Call<JsonObject> call = ueInterface.registerUser(eventId, accessToken);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if(response.body() != null && response.code() == 201) {
                    //Successo
                    AlertDialog ad = new AlertDialog.Builder(f.requireActivity()).create();
                    ad.setTitle(R.string.event_registration_success_title);
                    ad.setMessage(f.getString(R.string.event_registration_success));
                    ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                    ad.show();
                } else {
                    //Interpretare la risposta di errore (si ricordi che il messaggio di errore è
                    //contenuto nel campo "error" della risposta).
                    if(response.code() == 400) {
                        Log.i("malformed", "Richiesta malformata");
                    } else {
                        //COntinuare interpretazione risposta considerando i diversi casi di errore
                    }
                }
            }

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
