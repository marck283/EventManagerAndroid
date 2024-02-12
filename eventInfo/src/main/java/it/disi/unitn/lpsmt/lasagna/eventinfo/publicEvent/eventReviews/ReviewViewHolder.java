package it.disi.unitn.lpsmt.lasagna.eventinfo.publicEvent.eventReviews;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import it.disi.unitn.lasagna.eventmanager.ui_extra.special_buttons.ListenerButton;

public class ReviewViewHolder extends RecyclerView.ViewHolder {
    private final View itemView;

    private final Fragment f;

    private final int username, userRating, userPicture, userName, userEval, showAll, revFragToFullRevFrag;

    public ReviewViewHolder(@NonNull Fragment f, @NonNull View itemView, @IdRes int username,
                            @IdRes int userRating, @IdRes int userPicture, @StringRes int userName,
                            @StringRes int userEval, @IdRes int showAll, @NavigationRes int revFragToFullRevFrag) {
        super(itemView);
        this.f = f;
        this.itemView = itemView;
        this.username = username;
        this.userName = userName;
        this.userRating = userRating;
        this.userEval = userEval;
        this.userPicture = userPicture;
        this.showAll = showAll;
        this.revFragToFullRevFrag = revFragToFullRevFrag;
    }

    public void bindData(@NonNull Review review) {
        TextView userName1 = itemView.findViewById(username);
        TextView rating = itemView.findViewById(userRating);
        ImageView userPic = itemView.findViewById(userPicture);

        userName1.setText(f.getString(userName, review.userName()));
        rating.setText(f.getString(userEval, review.rating()));

        ListenerButton showAll1 = itemView.findViewById(showAll);
        showAll1.setOnClickListener(c -> {
            Bundle b = new Bundle();
            b.putSerializable("review", review);
            Navigation.findNavController(itemView).navigate(revFragToFullRevFrag, b);
        });

        Glide.with(userPic.getContext()).load(review.userPic()).circleCrop().into(userPic);
    }
}
