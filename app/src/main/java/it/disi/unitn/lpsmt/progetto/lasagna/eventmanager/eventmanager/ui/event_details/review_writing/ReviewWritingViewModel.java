package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.review_writing;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.registeredEvent.post_review.PostReview;

public class ReviewWritingViewModel extends ViewModel {
    public void postReview(@NonNull String userId, @NonNull String eventId, float rating,
                           @NonNull String title, @NonNull String description, @NonNull Fragment f, @NonNull View v) {
        PostReview review = new PostReview(userId, eventId, title, description, rating, f, v);
        review.start();
    }
}