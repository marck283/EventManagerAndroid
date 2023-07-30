package it.disi.unitn.lpsmt.lasagna.eventinfo.publicEvent.eventReviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.reviews.ReviewsFragment;

public class ReviewAdapter extends ListAdapter<Review, ReviewViewHolder> {
    private List<Review> revList;
    private ReviewsFragment f;

    /**
     * Costruisce un oggetto ReviewAdapter con i parametri forniti.
     * @param pubL La lista di recensioni ottenuta da remoto
     */
    public ReviewAdapter(@NonNull ReviewsFragment f, @NonNull DiffUtil.ItemCallback<Review> diffCallback, List<Review> pubL) {
        super(diffCallback);
        this.f = f;
        revList = pubL;
    }

    public ReviewAdapter(@NonNull DiffUtil.ItemCallback<Review> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_small_layout, parent, false);

        return new ReviewViewHolder(f, view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.bindData(revList.get(position));
    }
}
