package it.disi.unitn.lpsmt.lasagna.eventinfo.review_writing;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import it.disi.unitn.lpsmt.lasagna.eventinfo.registeredEvent.post_review.PostReview;

public class ReviewWritingViewModel extends ViewModel {
    public void postReview(@NonNull String userId, @NonNull String eventId, float rating,
                           @NonNull String title, @NonNull String description, @NonNull Fragment f,
                           @NonNull View v, @NotNull ActivityResultLauncher<Intent> loginLauncher,
                           @NotNull Class<? extends Activity> c) {
        PostReview review = new PostReview(userId, eventId, title, description, rating, f, v,
                loginLauncher, c);
        review.start();
    }
}