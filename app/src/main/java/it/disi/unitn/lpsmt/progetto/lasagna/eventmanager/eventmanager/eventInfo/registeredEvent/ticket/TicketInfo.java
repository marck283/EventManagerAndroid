package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.registeredEvent.ticket;

import android.view.View;
import android.widget.ImageView;

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

public class TicketInfo extends Thread {
    private final String eventId, userId, data, ora;
    private final OkHttpClient client;
    private final View v;

    private final Fragment f;

    public TicketInfo(@NonNull Fragment f, @NonNull View v, @NonNull String eventId,
                      @NonNull String userId, @NonNull String data, @NonNull String ora) {
        client = new OkHttpClient();
        this.eventId = eventId;
        this.userId = userId;
        this.data = data;
        this.ora = ora;
        this.v = v;
        this.f = f;
    }

    public void run() {
        Request request = new Request.Builder()
                .addHeader("x-access-token", userId)
                .addHeader("giorno", data)
                .addHeader("ora", ora)
                .url("https://eventmanagerzlf.herokuapp.com/api/v2/ticket/" + eventId)
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
                    Ticket ticket = Ticket.parseJSON(gson.fromJson(response.body().string(), JsonObject.class));
                    f.requireActivity().runOnUiThread(() -> {
                        try {
                            ImageView imageViewQrCode = v.findViewById(R.id.qrCode);
                            Glide.with(v).load(ticket.getQR()).into(imageViewQrCode);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }
}
