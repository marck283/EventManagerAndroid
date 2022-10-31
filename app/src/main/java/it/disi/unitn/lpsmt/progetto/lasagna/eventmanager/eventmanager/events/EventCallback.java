package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.Event;

public class EventCallback extends DiffUtil.ItemCallback<Event> {
    @Override
    public boolean areItemsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
        boolean eq = false;

        try {
            eq = Objects.equals(oldItem.getString("eventid"), newItem.getString("eventid"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return eq;
    }

    @Override
    public boolean areContentsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
        return oldItem.equals(newItem);
    }
}
