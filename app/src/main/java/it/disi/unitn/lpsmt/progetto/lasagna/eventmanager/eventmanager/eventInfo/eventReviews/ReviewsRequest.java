package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.eventReviews;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ReviewsRequest extends Thread {
    private final OkHttpClient client = new OkHttpClient();
    private String eventId = "";
    private RecyclerView rv;
    private final Request request;
    private ReviewAdapter adapter;
    private final Activity a;

    public ReviewsRequest(@NonNull Activity a, @NonNull View layout, @NonNull String id) {
        eventId = id;
        this.a = a;
        request = new Request.Builder()
                .url("https://eventmanagerzlf.herokuapp.com/api/v2/EventiPubblici/" + eventId + "/recensioni")
                .build();
        rv = layout.findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(layout.getContext());
        rv.setLayoutManager(mLayoutManager);

        adapter = new ReviewAdapter(new ReviewCallback());
        rv.setAdapter(adapter);
    }

    public void run() {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                try {
                    throw e;
                } catch (Throwable e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(!response.isSuccessful() || response.body() == null) {
                    Log.i("noResponse", String.valueOf(response.code()));
                } else {
                    ResponseBody resBody = response.body();
                    String responseBody = resBody.string();

                    Log.i("response", responseBody);

                    Gson gson = new Gson();
                    JsonObject r = gson.fromJson(responseBody, JsonObject.class);
                    JsonArray jsonArr = r.getAsJsonArray("recensioni");
                    ReviewList list = new ReviewList();
                    list.parseJSON(jsonArr);

                    adapter = new ReviewAdapter(new ReviewCallback(), list.getList());

                    a.runOnUiThread(() -> {
                        adapter.submitList(list.getList());
                        rv.setAdapter(adapter);
                    });
                }
            }
        });
    }
}
