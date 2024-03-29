package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class EventAdapter extends ListAdapter<it.disi.unitn.lpsmt.lasagna.localdatabase.Event, EventHolder> {
    private final List<it.disi.unitn.lpsmt.lasagna.localdatabase.Event> evList;

    public EventAdapter(@NonNull DiffUtil.ItemCallback<it.disi.unitn.lpsmt.lasagna.localdatabase.Event> diffCallback,
                        List<it.disi.unitn.lpsmt.lasagna.localdatabase.Event> evList) {
        super(diffCallback);
        this.evList = evList;
    }

    protected EventAdapter(@NonNull DiffUtil.ItemCallback<it.disi.unitn.lpsmt.lasagna.localdatabase.Event> diffCallback) {
        super(diffCallback);
        evList = new ArrayList<>();
    }

    /**
     * Called when RecyclerView needs a new {@link RecyclerView.ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(RecyclerView.ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     */
    @NonNull
    @Override
    public abstract EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link RecyclerView.ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link RecyclerView.ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(RecyclerView.ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull EventHolder holder, int position) {
        holder.bindData(evList.get(position));
    }

    @Override
    @NonNull
    public List<it.disi.unitn.lpsmt.lasagna.localdatabase.Event> getCurrentList() {
        return evList;
    }

    public void clearEventList() {
        if(evList != null && evList.size() > 0) {
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
