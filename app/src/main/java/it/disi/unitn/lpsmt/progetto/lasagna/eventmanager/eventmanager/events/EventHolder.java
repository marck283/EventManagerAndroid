package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class EventHolder extends RecyclerView.ViewHolder {
    public EventHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void bindData(it.disi.unitn.lpsmt.lasagna.localdatabase.Event dataModel);
}
