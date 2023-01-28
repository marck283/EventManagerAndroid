package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.publicEvent.eventReviews;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.reviews.ReviewsFragment;

public class ReviewViewHolder extends RecyclerView.ViewHolder {
    private View itemView;
    private TextView userName, rating;
    private ImageView userPic;

    private ReviewsFragment f;

    public ReviewViewHolder(@NonNull ReviewsFragment f, @NonNull View itemView) {
        super(itemView);
        this.f = f;
        this.itemView = itemView;
    }

    public void bindData(@NonNull Review review) {
        userName = itemView.findViewById(R.id.userName);
        rating = itemView.findViewById(R.id.userRating);
        userPic = itemView.findViewById(R.id.userPicture);

        userName.setText(f.getString(R.string.user_name, review.getUserName()));
        rating.setText(f.getString(R.string.evaluation, review.getRating()));

        Button showAll = itemView.findViewById(R.id.showAll);
        showAll.setOnClickListener(c -> {
            Bundle b = new Bundle();
            b.putSerializable("review", review);
            Navigation.findNavController(itemView).navigate(R.id.action_reviewsFragment_to_fullReviewFragment, b);
        });

        Glide.with(userPic.getContext()).load(review.getUserPic()).circleCrop().into(userPic);
    }
}
