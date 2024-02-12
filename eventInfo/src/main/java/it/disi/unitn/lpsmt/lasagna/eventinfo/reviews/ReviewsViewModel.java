package it.disi.unitn.lpsmt.lasagna.eventinfo.reviews;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import it.disi.unitn.lpsmt.lasagna.eventinfo.publicEvent.eventReviews.ReviewsRequest;

public class ReviewsViewModel extends ViewModel {
    public void getReviews(@NonNull Fragment f, @NonNull View layout, String eventId,
                           @IdRes int recyclerView, @StringRes int norevs, @StringRes int norevsmsg,
                           @LayoutRes int revSmallLayout,
                           @IdRes int username,
                           @IdRes int userRating, @IdRes int userPicture, @StringRes int userName,
                           @StringRes int userEval, @IdRes int showAll, @NavigationRes int revFragToFullRevFrag) {
        ReviewsRequest request = new ReviewsRequest(f, layout, eventId, recyclerView, norevs, norevsmsg,
                revSmallLayout, username, userRating, userPicture, userName, userEval, showAll,
                revFragToFullRevFrag);
        request.start();
    }
}