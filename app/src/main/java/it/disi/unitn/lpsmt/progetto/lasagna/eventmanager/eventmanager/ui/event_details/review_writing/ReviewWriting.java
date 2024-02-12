package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.review_writing;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.StringRes;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.Contract;

import it.disi.unitn.lpsmt.lasagna.eventinfo.review_writing.ReviewWritingViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.lasagna.sharedprefs.sharedpreferences.SharedPrefs;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.special_buttons.ListenerButton;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;

public class ReviewWriting extends Fragment {

    private ReviewWritingViewModel mViewModel;

    private String userJwt, eventId;

    private ActivityResultLauncher<Intent> loginLauncher;

    private SharedPrefs prefs;

    private String title, description;

    private float rating;

    private View v;

    @NonNull
    @Contract(" -> new")
    public static ReviewWriting newInstance() {
        return new ReviewWriting();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        userJwt = prefs.getString("accessToken");
                        mViewModel.postReview(userJwt, eventId, rating, title, description, this,
                                v, loginLauncher, LoginActivity.class);
                    }
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle b = getArguments();
        if(b != null) {
            userJwt = b.getString("userId", "");
            eventId = b.getString("eventId", "");
        }
        v = inflater.inflate(R.layout.fragment_review_writing, container, false);

        return v;
    }

    private void setAlertDialog(@StringRes int title, @StringRes int message) {
        Activity activity = getActivity();
        if(activity != null && isAdded()) {
            AlertDialog dialog = new AlertDialog.Builder(requireActivity()).create();
            dialog.setTitle(title);
            dialog.setMessage(getString(message));
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
            dialog.show();
        }
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReviewWritingViewModel.class);
        prefs = new SharedPrefs("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.AccTok",
                requireActivity());

        ListenerButton postReview = v.findViewById(R.id.button15);
        postReview.setOnClickListener(c -> {
            TextInputLayout titleLayout = v.findViewById(R.id.title), descriptionLayout = v.findViewById(R.id.description);
            RatingBar ratingBar = v.findViewById(R.id.ratingBar);

            if(titleLayout.getEditText() != null && !titleLayout.getEditText().getText().toString().equals("")) {
                title = titleLayout.getEditText().getText().toString();
                if(descriptionLayout.getEditText() != null && !descriptionLayout.getEditText().getText().toString().equals("")) {
                    description = descriptionLayout.getEditText().getText().toString();
                    rating = ratingBar.getRating();
                    if(rating >= 0.5) {
                        mViewModel.postReview(userJwt, eventId, rating, title, description, this,
                                v, loginLauncher, LoginActivity.class);
                    } else {
                        //Imposta AlertDialog per valutazione non consentita
                        setAlertDialog(R.string.wrong_rating, R.string.wrong_rating_message);
                    }
                } else {
                    //Imposta AlertDialog per descrizione mancante
                    setAlertDialog(R.string.no_review_description, R.string.no_review_description_message);
                }
            } else {
                //Imposta AlertDialog per titolo mancante
                setAlertDialog(R.string.no_review_title, R.string.no_review_title_message);
            }
        });
    }

}