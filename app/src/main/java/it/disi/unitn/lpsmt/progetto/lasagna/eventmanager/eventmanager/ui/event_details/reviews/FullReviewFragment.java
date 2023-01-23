package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.reviews;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.eventReviews.Review;

public class FullReviewFragment extends Fragment {
    private Review review;
    private View root;

    public static FullReviewFragment newInstance() {
        return new FullReviewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle b = getArguments();
        if(b != null) {
            review = (Review)b.getSerializable("review");
        }

        root = inflater.inflate(R.layout.fragment_full_review, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView imgView = view.findViewById(R.id.userPicture);
        Glide.with(view.getContext()).load(review.getUserPic()).circleCrop().into(imgView);

        TextView userName = view.findViewById(R.id.userName), rating = view.findViewById(R.id.rating);
        userName.setText(getString(R.string.user_name, review.getUserName()));
        rating.setText(getString(R.string.evaluation, review.getRating()));

        MaterialTextView description = view.findViewById(R.id.description);
        description.setText(review.getDescription());
    }

}