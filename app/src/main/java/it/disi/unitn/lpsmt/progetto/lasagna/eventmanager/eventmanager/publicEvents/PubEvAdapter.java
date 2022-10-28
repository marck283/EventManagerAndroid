package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.Event;

public class PubEvAdapter extends ListAdapter<Event, ViewHolder> {
    private List<Event> evList;
    private Context mContext;

    /**
     * Costruisce un oggetto PubEvAdapter con i parametri forniti.
     * @param pubL La lista di eventi pubblici ottenuta da remoto
     * @param c Il contesto a cui agganciare le View che saranno originate usando i metodi di questa classe.
     */
    public PubEvAdapter(@NonNull DiffUtil.ItemCallback<Event> diffCallback, List<Event> pubL, Context c) {
        super(diffCallback);
        evList = pubL;
        mContext = c;
    }

    public PubEvAdapter(@NonNull DiffUtil.ItemCallback<Event> diffCallback) {
        super(diffCallback);
        evList = new ArrayList<>();
        mContext = null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate out card list item

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pub_ev_card_layout, parent, false);
        // Return a new view holder

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(evList.get(position), mContext);
    }

    public void clearEventList() {
        if(evList.size() > 0) {
            evList.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return evList.size();
    }
}
