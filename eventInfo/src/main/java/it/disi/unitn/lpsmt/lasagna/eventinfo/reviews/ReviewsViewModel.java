package it.disi.unitn.lpsmt.lasagna.eventinfo.reviews;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import it.disi.unitn.lpsmt.lasagna.eventinfo.publicEvent.eventReviews.ReviewAdapter;
import it.disi.unitn.lpsmt.lasagna.eventinfo.publicEvent.eventReviews.ReviewCallback;
import it.disi.unitn.lpsmt.lasagna.eventinfo.publicEvent.eventReviews.ReviewsCallback;
import it.disi.unitn.lpsmt.lasagna.network.repository.ReviewRepository;

public class ReviewsViewModel extends ViewModel {

    public void getReviews(@NonNull Fragment f, @NonNull View layout, String eventId,
                           @IdRes int recyclerView, @StringRes int norevs, @StringRes int norevsmsg,
                           @LayoutRes int revSmallLayout,
                           @IdRes int username,
                           @IdRes int userRating, @IdRes int userPicture, @StringRes int userName,
                           @StringRes int userEval, @IdRes int showAll, @NavigationRes int revFragToFullRevFrag) {
        RecyclerView rv = layout.findViewById(recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(layout.getContext());
        rv.setLayoutManager(mLayoutManager);

        ReviewAdapter adapter = new ReviewAdapter(new ReviewCallback());
        rv.setAdapter(adapter);

        ReviewsCallback callback = new ReviewsCallback(adapter, f, rv, norevs, norevsmsg, revSmallLayout,
                username, userRating, userPicture, userName, userEval, showAll, revFragToFullRevFrag);

        ReviewRepository repo = new ReviewRepository();
        repo.getEventReviews(eventId, new ReviewRepository.ReviewDataCallback() {
            @Override
            public void onSuccess(JsonObject reviewsJson) {
                callback.handleReviewsSuccess(reviewsJson);
            }

            @Override
            public void onError(int statusCode, String errorMessage) { }
        });
    }
}