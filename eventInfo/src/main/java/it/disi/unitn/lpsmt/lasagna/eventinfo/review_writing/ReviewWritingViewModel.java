package it.disi.unitn.lpsmt.lasagna.eventinfo.review_writing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavDeepLinkRequest;
import androidx.navigation.Navigation;

import org.jetbrains.annotations.NotNull;

import it.disi.unitn.lpsmt.lasagna.eventinfo.R;
import it.disi.unitn.lpsmt.lasagna.network.client.RetrofitClient;
import it.disi.unitn.lpsmt.lasagna.network.repository.ReviewRepository;

public class ReviewWritingViewModel extends ViewModel {

    public void postReview(@NonNull String userId, @NonNull String eventId, float rating,
                           @NonNull String title, @NonNull String description, @NonNull Fragment f,
                           @NonNull View v, @NotNull ActivityResultLauncher<Intent> loginLauncher,
                           @NotNull Class<? extends Activity> c) {
        if (!userId.isEmpty()) {
            RetrofitClient.getInstance().setAccessToken(userId);
            ReviewRepository repo = new ReviewRepository();

            // Convert 5-star float rating to 10-point scale string
            String evaluationStr = String.valueOf(2 * rating);

            repo.postReview(eventId, title, evaluationStr, description, new ReviewRepository.ReviewActionCallback() {
                @Override
                public void onSuccess(int statusCode) {
                    Activity activity = f.getActivity();
                    if (activity != null && !activity.isFinishing() && !activity.isDestroyed() && f.isAdded()) {
                        activity.runOnUiThread(() -> {
                            if (statusCode == 201) {
                                AlertDialog dialog = new AlertDialog.Builder(activity).create();
                                dialog.setTitle(R.string.review_creation_successful);
                                dialog.setMessage(f.getString(R.string.review_creation_successful_message));
                                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> {
                                    dialog1.dismiss();
                                    NavDeepLinkRequest deepLinkRequest = NavDeepLinkRequest.Builder
                                            .fromUri(Uri.parse("app://eventmanager/user_calendar"))
                                            .build();
                                    Navigation.findNavController(v).navigate(deepLinkRequest);
                                });
                                dialog.show();
                            } else if (statusCode == 401) {
                                Intent loginIntent = new Intent(activity, c);
                                loginLauncher.launch(loginIntent);
                            } else {
                                AlertDialog dialog = new AlertDialog.Builder(activity).create();
                                dialog.setTitle(R.string.unknown_error);
                                dialog.setMessage(f.getString(R.string.unknown_error_message));
                                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                                dialog.show();
                            }
                        });
                    }
                }

                @Override
                public void onError(int statusCode, String errorMessage) {
                    Activity activity = f.getActivity();
                    if (activity != null && !activity.isFinishing() && !activity.isDestroyed() && f.isAdded()) {
                        activity.runOnUiThread(() -> {
                            AlertDialog dialog = new AlertDialog.Builder(activity).create();
                            dialog.setTitle(R.string.unknown_error);
                            dialog.setMessage(f.getString(R.string.unknown_error_message));
                            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                            dialog.show();
                        });
                    }
                }
            });
        }
    }
}