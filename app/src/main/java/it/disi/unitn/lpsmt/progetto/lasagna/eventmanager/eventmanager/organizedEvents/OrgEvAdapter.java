package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.organizedEvents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.Event;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventAdapter;

public class OrgEvAdapter extends EventAdapter {
    public OrgEvAdapter(@NonNull DiffUtil.ItemCallback<Event> diffCallback, List<Event> evList) {
        super(diffCallback, evList);
    }

    protected OrgEvAdapter(@NonNull DiffUtil.ItemCallback<Event> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public OrgEvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_dialog_text_view, parent, false);
        // Return a new view holder

        return new OrgEvViewHolder(view);
    }
}
