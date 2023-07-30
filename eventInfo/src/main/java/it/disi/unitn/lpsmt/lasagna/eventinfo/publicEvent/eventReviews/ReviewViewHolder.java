package it.disi.unitn.lpsmt.lasagna.eventinfo.publicEvent.eventReviews;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.reviews.ReviewsFragment;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.special_buttons.ListenerButton;

public class ReviewViewHolder extends RecyclerView.ViewHolder {
    private final View itemView;

    private final ReviewsFragment f;

    public ReviewViewHolder(@NonNull ReviewsFragment f, @NonNull View itemView) {
        super(itemView);
        this.f = f;
        this.itemView = itemView;
    }

    public void bindData(@NonNull Review review) {
        TextView userName = itemView.findViewById(R.id.userName);
        TextView rating = itemView.findViewById(R.id.userRating);
        ImageView userPic = itemView.findViewById(R.id.userPicture);

        userName.setText(f.getString(R.string.user_name, review.userName()));
        rating.setText(f.getString(R.string.evaluation, review.rating()));

        ListenerButton showAll = itemView.findViewById(R.id.showAll);
        showAll.setOnClickListener(c -> {
            Bundle b = new Bundle();
            b.putSerializable("review", review);
            Navigation.findNavController(itemView).navigate(R.id.action_reviewsFragment_to_fullReviewFragment, b);
        });

        Glide.with(userPic.getContext()).load(review.userPic()).circleCrop().into(userPic);
    }
}
