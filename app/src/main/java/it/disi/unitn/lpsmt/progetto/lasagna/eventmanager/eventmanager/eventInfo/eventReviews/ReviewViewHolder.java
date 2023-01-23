package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.eventReviews;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;

public class ReviewViewHolder extends RecyclerView.ViewHolder {
    private View itemView;
    private TextView userName, rating;
    private ImageView userPic;

    public ReviewViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public void bindData(@NonNull Review review) {
        userName = itemView.findViewById(R.id.userName);
        rating = itemView.findViewById(R.id.userRating);
        userPic = itemView.findViewById(R.id.userPicture);

        userName.setText(review.getUserName());
        rating.setText(String.valueOf(review.getRating()));
        Glide.with(userPic.getContext()).load(review.getUserPic()).circleCrop().into(userPic);
    }
}
