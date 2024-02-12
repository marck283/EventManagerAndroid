package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class EventCallback extends DiffUtil.ItemCallback<it.disi.unitn.lpsmt.lasagna.localdatabase.Event> {
    @Override
    public boolean areItemsTheSame(@NonNull it.disi.unitn.lpsmt.lasagna.localdatabase.Event oldItem,
                                   @NonNull it.disi.unitn.lpsmt.lasagna.localdatabase.Event newItem) {
        boolean eq = false;

        try {
            eq = Objects.equals(oldItem.getString("eventid"), newItem.getString("eventid"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return eq;
    }

    @Override
    public boolean areContentsTheSame(@NonNull it.disi.unitn.lpsmt.lasagna.localdatabase.Event oldItem,
                                      @NonNull it.disi.unitn.lpsmt.lasagna.localdatabase.Event newItem) {
        return oldItem.equals(newItem);
    }
}
