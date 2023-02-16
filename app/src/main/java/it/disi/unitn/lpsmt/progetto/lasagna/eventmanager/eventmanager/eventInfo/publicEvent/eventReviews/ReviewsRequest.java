package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.publicEvent.eventReviews;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.reviews.ReviewsFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ReviewsRequest extends Thread {
    private final OkHttpClient client = new OkHttpClient();
    private final String eventId, screenType;
    private final RecyclerView rv;
    private final Request request;
    private ReviewAdapter adapter;
    private final ReviewsFragment f;

    private final View v;

    public ReviewsRequest(@NonNull ReviewsFragment f, @NonNull View layout, @NonNull String id, @NonNull String screenType) {
        eventId = id;
        this.screenType = screenType;
        this.f = f;
        request = new Request.Builder()
                .url("https://eventmanagerzlf.herokuapp.com/api/v2/EventiPubblici/" + eventId + "/recensioni")
                .build();
        v = layout;
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

                    Activity activity = f.getActivity();
                    if(list.getList() == null || list.getList().size() == 0) {
                        if(activity != null && f.isAdded()) {
                            f.requireActivity().runOnUiThread(() -> {
                                AlertDialog dialog = new AlertDialog.Builder(f.requireContext()).create();
                                dialog.setTitle(R.string.no_reviews);
                                dialog.setMessage(f.getString(R.string.no_reviews_message));
                                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> {
                                    dialog1.dismiss();

                                    //Queste righe sono da rimuovere per risolvere il problema della navigazione
                                    //in caso di assenza di recensioni
                                    Bundle b = new Bundle();
                                    b.putString("eventType", screenType);
                                    b.putString("eventId", eventId);
                                    Navigation.findNavController(v).navigate(R.id.action_reviewsFragment_to_eventDetailsFragment, b);
                                });
                                dialog.show();
                            });
                        }
                    } else {
                        adapter = new ReviewAdapter(f, new ReviewCallback(), list.getList());

                        if(activity != null && f.isAdded()) {
                            f.requireActivity().runOnUiThread(() -> {
                                adapter.submitList(list.getList());
                                rv.setAdapter(adapter);
                            });
                        }
                    }
                }
            }
        });
    }
}
