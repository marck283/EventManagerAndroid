package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents.publicEvent.PublicEvent;

public class PubEvAdapter extends RecyclerView.Adapter<PubEvAdapter.ViewHolder> {
    private List<PublicEvent> evList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgView;
        private TextView evName, catName; //TextView per il nome dell'evento e la sua categoria

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imageView2);
            evName = itemView.findViewById(R.id.textView3);
            catName = itemView.findViewById(R.id.textView5);
        }

        public void bindData(PublicEvent dataModel, Context context) {
            imgView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.evimgview));
            try {
                evName.setText(dataModel.getString("name"));
                catName.setText(dataModel.getString("category"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public PubEvAdapter(List<PublicEvent> pubL, Context c) {
        evList = pubL;
        mContext = c;
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
