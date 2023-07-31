package it.disi.unitn.lpsmt.lasagna.eventinfo.reviews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.Contract;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.publicEvent.eventReviews.Review;

public class FullReviewFragment extends Fragment {
    private Review review;

    @NonNull
    @Contract(" -> new")
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

        return inflater.inflate(R.layout.fragment_full_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView imgView = view.findViewById(R.id.userPicture);
        Glide.with(view.getContext()).load(review.userPic()).circleCrop().into(imgView);

        TextView userName = view.findViewById(R.id.userName), rating = view.findViewById(R.id.rating);
        userName.setText(getString(R.string.user_name, review.userName()));
        rating.setText(getString(R.string.evaluation, review.rating()));

        MaterialTextView description = view.findViewById(R.id.description);
        description.setText(review.description());
    }

}