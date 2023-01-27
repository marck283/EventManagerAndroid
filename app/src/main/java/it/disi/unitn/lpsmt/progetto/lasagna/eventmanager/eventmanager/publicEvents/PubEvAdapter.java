package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.Event;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventAdapter;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventHolder;

public class PubEvAdapter extends EventAdapter {
    private final Fragment f;

    /**
     * Costruisce un oggetto PubEvAdapter con i parametri forniti.
     * @param pubL La lista di eventi pubblici ottenuta da remoto
     */
    public PubEvAdapter(@NonNull Fragment f, @NonNull DiffUtil.ItemCallback<Event> diffCallback, List<Event> pubL) {
        super(diffCallback, pubL);
        this.f = f;
    }

    public PubEvAdapter(@NonNull Fragment f, @NonNull DiffUtil.ItemCallback<Event> diffCallback) {
        super(diffCallback);
        this.f = f;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate out card list item

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pub_ev_card_layout, parent, false);
        // Return a new view holder

        return new ViewHolder(f, view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventHolder holder, int position) {
        holder.bindData(getCurrentList().get(position));
    }
}
