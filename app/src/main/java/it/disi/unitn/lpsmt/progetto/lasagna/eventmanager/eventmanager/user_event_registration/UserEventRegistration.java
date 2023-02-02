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
    private final UserEventRegistrationInterface ueInterface;

    public UserEventRegistration() {
        Retrofit retro = new Retrofit.Builder()
                .baseUrl("https://eventmanagerzlf.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ueInterface = retro.create(UserEventRegistrationInterface.class);
    }

    public void registerUser(@NonNull String accessToken, @NonNull String eventId, @NonNull String day,
                             @NonNull String time, @NonNull EventDetailsFragment f) {
        Log.i("day", day);
        Log.i("time", time);
        Call<JsonObject> call = ueInterface.registerUser(eventId, accessToken, new EventDayHour(day, time));
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                //Interpretare la risposta di errore (si ricordi che il messaggio di errore è
                //contenuto nel campo "error" della risposta).
                //Log.i("response", String.valueOf(response.body()));
                //Errore sul body della risposta? Perché? Non ci dovrebbe essere un body...
                switch(response.code()) {
                    case 201: {
                        //Successo
                        AlertDialog ad = new AlertDialog.Builder(f.requireActivity()).create();
                        ad.setTitle(R.string.event_registration_success_title);
                        ad.setMessage(f.getString(R.string.event_registration_success));
                        ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                        ad.show();
                        break;
                    }
                    case 400: {
                        Log.i("malformed", "Richiesta malformata");
                        break;
                    }
                    case 403: {
                        if(response.body() != null) {
                            AlertDialog ad = new AlertDialog.Builder(f.requireActivity()).create();
                            ad.setTitle(R.string.error);
                            ad.setMessage(f.getString(R.string.max_pers_reached_or_user_already_registered));
                            ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                            ad.show();
                        }
                    }
                    case 500: {
                        AlertDialog ad = new AlertDialog.Builder(f.requireActivity()).create();
                        ad.setTitle(R.string.internal_server_error);
                        ad.setMessage(f.getString(R.string.service_unavailable));
                        ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                        ad.show();
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
