package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.reviews;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.eventReviews.ReviewsRequest;

public class ReviewsViewModel extends ViewModel {
    public void getReviews(@NonNull Activity a, @NonNull View layout, String eventId) {
        ReviewsRequest request = new ReviewsRequest(a, layout, eventId);
        request.start();
    }
}