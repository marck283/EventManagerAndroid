package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.review_writing;

import androidx.annotation.StringRes;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;

import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.Contract;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;

public class ReviewWriting extends Fragment {

    private ReviewWritingViewModel mViewModel;

    private String userJwt, eventId;

    @NonNull
    @Contract(" -> new")
    public static ReviewWriting newInstance() {
        return new ReviewWriting();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle b = getArguments();
        if(b != null) {
            userJwt = b.getString("userId", "");
            eventId = b.getString("eventId", "");
        }
        return inflater.inflate(R.layout.fragment_review_writing, container, false);
    }

    private void setAlertDialog(@StringRes int title, @StringRes int message) {
        AlertDialog dialog = new AlertDialog.Builder(requireActivity()).create();
        dialog.setTitle(title);
        dialog.setMessage(getString(message));
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
        dialog.show();
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReviewWritingViewModel.class);

        Button postReview = v.findViewById(R.id.button15);
        postReview.setOnClickListener(c -> {
            TextInputLayout titleLayout = v.findViewById(R.id.title), descriptionLayout = v.findViewById(R.id.description);
            RatingBar ratingBar = v.findViewById(R.id.ratingBar);
            String title, description;
            if(titleLayout != null && titleLayout.getEditText() != null && !titleLayout.getEditText().toString().equals("")) {
                title = titleLayout.getEditText().getText().toString();
                if(descriptionLayout != null && descriptionLayout.getEditText() != null &&
                        !descriptionLayout.getEditText().toString().equals("")) {
                    description = descriptionLayout.getEditText().toString();
                    float rating = ratingBar.getRating();
                    if(rating > 1.0) {
                        mViewModel.postReview(userJwt, eventId, rating, title, description);
                    } else {
                        //Set alert dialog for wrong rating
                        setAlertDialog(R.string.wrong_rating, R.string.wrong_rating_message);
                    }
                } else {
                    //Set alert dialog
                    setAlertDialog(R.string.no_review_description, R.string.no_review_description_message);
                }
            } else {
                //Set alert dialog
                setAlertDialog(R.string.no_review_title, R.string.no_review_title_message);
            }
        });
    }

}