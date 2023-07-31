package it.disi.unitn.lpsmt.lasagna.eventinfo.reviews;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.publicEvent.eventReviews.ReviewsRequest;

public class ReviewsViewModel extends ViewModel {
    public void getReviews(@NonNull ReviewsFragment f, @NonNull View layout, String eventId) {
        ReviewsRequest request = new ReviewsRequest(f, layout, eventId);
        request.start();
    }
}