package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.registeredEvent;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisteredEventInfo extends Thread {
    private final OkHttpClient client;

    private final String userJwt, eventId, data;

    private final Fragment f;

    private final View v;

    public RegisteredEventInfo(@NonNull String userJwt, @NonNull String eventId, @NonNull Fragment f,
                               @NonNull View v, @NonNull String data) {
        client = new OkHttpClient();
        this.userJwt = userJwt;
        this.eventId = eventId;
        this.f = f;
        this.v = v;
        this.data = data;
    }

    public void run() {
        Request request = new Request.Builder()
                .addHeader("x-access-token", userJwt)
                .addHeader("data", data)
                .url("https://eventmanagerzlf.herokuapp.com/api/v2/InfoEventoIscr/" + eventId)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //Nulla qui...
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.body() != null) {
                    Gson gson = new GsonBuilder().create();

                    String body = response.body().string();
                    RegisteredEvent event = RegisteredEvent.parseJSON(gson.fromJson(body, JsonObject.class));

                    if(!f.isDetached()) {
                        f.requireActivity().runOnUiThread(() -> {
                            ImageView image = v.findViewById(R.id.eventPicture);
                            Glide.with(v).load(event.decodeBase64()).into(image);

                            TextView title = v.findViewById(R.id.title);
                            title.setText(event.getEventName());

                            TextView organizzatore = v.findViewById(R.id.textView16);
                            organizzatore.setText(f.getString(R.string.organizer, event.getOrgName()));

                            TextView giorno = v.findViewById(R.id.textView11);
                            giorno.setText(f.getString(R.string.day_not_selectable,
                                    "\n" + event.getLuogoEv().getData()));

                            TextView ora = v.findViewById(R.id.textView20);
                            ora.setText(f.getString(R.string.time_not_selectable,
                                    "\n" + event.getLuogoEv().getOra()));

                            String[] sDurata = event.getDurata().split(":");
                            TextView durata = v.findViewById(R.id.textView39);
                            String wholeDuration = Integer.parseInt(sDurata[0]) + "g, "
                                    + Integer.parseInt(sDurata[0]) + "h e " + Integer.parseInt(sDurata[0]) + "m";
                            durata.setText(f.getString(R.string.duration, wholeDuration));

                            TextView address = v.findViewById(R.id.textView42);
                            address.setText(f.getString(R.string.event_address, event.getLuogoEv().toString()));
                        });
                    }
                } else {
                    Log.i("noResponse", "Nessuna informazione ricevuta sull'evento richiesto");
                }
            }
        });
    }
}
