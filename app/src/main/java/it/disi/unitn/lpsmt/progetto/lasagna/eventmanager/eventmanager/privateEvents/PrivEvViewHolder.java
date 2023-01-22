package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.privateEvents;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.Event;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventHolder;

public class PrivEvViewHolder extends EventHolder {
    private final TextView t;

    public PrivEvViewHolder(@NonNull View itemView) {
        super(itemView);
        t = itemView.findViewById(R.id.event_name_text_view);
        t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        t.setOnClickListener(c -> Log.i("click", "box clicked"));
    }

    public void bindData(Event dataModel) {
        try {
            t.setText(dataModel.getString("name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
