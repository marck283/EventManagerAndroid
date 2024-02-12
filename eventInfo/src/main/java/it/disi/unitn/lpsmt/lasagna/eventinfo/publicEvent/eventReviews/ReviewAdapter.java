package it.disi.unitn.lpsmt.lasagna.eventinfo.publicEvent.eventReviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.util.List;

public class ReviewAdapter extends ListAdapter<Review, ReviewViewHolder> {
    private List<Review> revList;
    private Fragment f;

    private final int revSmallLayout, username, userName, userRating, userEval, userPicture, showAll,
    revFragToFullRevFrag;

    /**
     * Costruisce un oggetto ReviewAdapter con i parametri forniti.
     * @param pubL La lista di recensioni ottenuta da remoto
     */
    public ReviewAdapter(@NonNull Fragment f, @NonNull DiffUtil.ItemCallback<Review> diffCallback,
                         List<Review> pubL, @LayoutRes int reviewSmallLayout, @IdRes int username,
                         @IdRes int userRating, @IdRes int userPicture, @StringRes int userName,
                         @StringRes int userEval, @IdRes int showAll, @NavigationRes int revFragToFullRevFrag) {
        super(diffCallback);
        this.f = f;
        revList = pubL;
        revSmallLayout = reviewSmallLayout;
        this.username = username;
        this.userName = userName;
        this.userRating = userRating;
        this.userEval = userEval;
        this.userPicture = userPicture;
        this.showAll = showAll;
        this.revFragToFullRevFrag = revFragToFullRevFrag;
    }

    public ReviewAdapter(@NonNull DiffUtil.ItemCallback<Review> diffCallback) {
        super(diffCallback);
        revSmallLayout = 0;
        username = 0;
        userName = 0;
        userRating = 0;
        userEval = 0;
        userPicture = 0;
        showAll = 0;
        revFragToFullRevFrag = 0;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(revSmallLayout, parent, false);

        return new ReviewViewHolder(f, view, username, userRating, userPicture, userName, userEval,
                showAll, revFragToFullRevFrag);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.bindData(revList.get(position));
    }
}
