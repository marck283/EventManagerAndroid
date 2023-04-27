package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.publicEvent.eventReviews;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.reviews.ReviewsFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ReviewsCallback implements Callback {

    private ReviewAdapter adapter;

    private final ReviewsFragment f;

    private final RecyclerView rv;

    public ReviewsCallback(@NotNull ReviewAdapter adapter, @NotNull ReviewsFragment f, @NotNull RecyclerView rv) {
        this.adapter = adapter;
        this.f = f;
        this.rv = rv;
    }

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
                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) ->
                                dialog1.dismiss());
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
}
