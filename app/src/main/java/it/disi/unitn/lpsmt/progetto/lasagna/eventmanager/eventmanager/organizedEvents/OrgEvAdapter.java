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
    private final String day;

    public OrgEvAdapter(@NonNull DiffUtil.ItemCallback<Event> diffCallback, List<Event> evList,
                        @NonNull String day) {
        super(diffCallback, evList);
        this.day = day;
    }

    public OrgEvAdapter(@NonNull DiffUtil.ItemCallback<Event> diffCallback, List<Event> evList) {
        super(diffCallback, evList);
        this.day = null;
    }

    protected OrgEvAdapter(@NonNull DiffUtil.ItemCallback<Event> diffCallback) {
        super(diffCallback);
        day = null;
    }

    @NonNull
    @Override
    public OrgEvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_dialog_button, parent, false);
        // Return a new view holder
        if(day != null) {
            return new OrgEvViewHolder(view, day);
        }
        return new OrgEvViewHolder(view);
    }
}
