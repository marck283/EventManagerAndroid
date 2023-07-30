package it.disi.unitn.lpsmt.lasagna.eventinfo.publicEvent.eventReviews;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class ReviewCallback extends DiffUtil.ItemCallback<Review> {
    @Override
    public boolean areItemsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
        boolean eq = false;

        try {
            eq = Objects.equals(oldItem.userName(), newItem.userName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return eq;
    }

    @Override
    public boolean areContentsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
        return oldItem.equals(newItem);
    }
}
