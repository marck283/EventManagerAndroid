package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents.publicEvent.PublicEvent;

public class PubEvCallback extends DiffUtil.ItemCallback<PublicEvent> {
    @Override
    public boolean areItemsTheSame(@NonNull PublicEvent oldItem, @NonNull PublicEvent newItem) {
        boolean eq = false;

        try {
            eq = Objects.equals(oldItem.getString("eventid"), newItem.getString("eventid"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return eq;
    }

    @Override
    public boolean areContentsTheSame(@NonNull PublicEvent oldItem, @NonNull PublicEvent newItem) {
        return oldItem.equals(newItem);
    }
}
