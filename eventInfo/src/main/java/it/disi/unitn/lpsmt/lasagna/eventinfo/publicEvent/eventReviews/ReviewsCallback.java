package it.disi.unitn.lpsmt.lasagna.eventinfo.publicEvent.eventReviews;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ReviewsCallback implements Callback {

    private ReviewAdapter adapter;

    private final Fragment f;

    private final RecyclerView rv;

    private final int norevs, norevsmsg, revSmallLayout, username, userRating, userPicture, userName,
    userEval, showAll, revFragToFullRevFrag;

    public ReviewsCallback(@NotNull ReviewAdapter adapter, @NotNull Fragment f, @NotNull RecyclerView rv,
                           @StringRes int norevs, @StringRes int norevsmsg, @LayoutRes int revSmallLayout,
                           @IdRes int username,
                           @IdRes int userRating, @IdRes int userPicture, @StringRes int userName,
                           @StringRes int userEval, @IdRes int showAll, @NavigationRes int revFragToFullRevFrag) {
        this.adapter = adapter;
        this.f = f;
        this.rv = rv;
        this.norevs = norevs;
        this.norevsmsg = norevsmsg;
        this.revSmallLayout = revSmallLayout;
        this.username = username;
        this.userRating = userRating;
        this.userPicture = userPicture;
        this.userName = userName;
        this.userEval = userEval;
        this.showAll = showAll;
        this.revFragToFullRevFrag = revFragToFullRevFrag;
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
            String responseBody = response.body().string();

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
                        dialog.setTitle(norevs);
                        dialog.setMessage(f.getString(norevsmsg));
                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) ->
                                dialog1.dismiss());
                        dialog.show();
                    });
                }
            } else {
                adapter = new ReviewAdapter(f, new ReviewCallback(), list.getList(), revSmallLayout,
                        username, userRating, userPicture, userName, userEval, showAll, revFragToFullRevFrag);

                if(activity != null && f.isAdded()) {
                    f.requireActivity().runOnUiThread(() -> {
                        adapter.submitList(list.getList());
                        rv.setAdapter(adapter);
                    });
                }
            }
            response.body().close();
        }
    }
}
