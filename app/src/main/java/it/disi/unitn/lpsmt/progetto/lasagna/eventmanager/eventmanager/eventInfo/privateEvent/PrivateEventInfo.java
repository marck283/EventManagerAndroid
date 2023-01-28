package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.privateEvent;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PrivateEventInfo extends Thread {
    private OkHttpClient client;

    private String userJwt;
    private String eventId;

    public PrivateEventInfo(@NonNull String userJwt, @NonNull String evId) {
        client = new OkHttpClient();
        this.userJwt = userJwt;
        eventId = evId;
    }

    public void run() {
        Request request = new Request.Builder()
                .addHeader("x-access-token", userJwt)
                .url("https://eventmanagerzlf.herokuapp.com/api/v2/EventiPrivati/" + eventId)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
    }
}
