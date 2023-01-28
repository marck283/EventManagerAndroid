package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.privateEvents;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.Event;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventHolder;

public class PrivEvViewHolder extends EventHolder {
    private final MaterialButton t;
    private final View v;

    public PrivEvViewHolder(@NonNull View itemView) {
        super(itemView);
        t = itemView.findViewById(R.id.event_name_text_view);
        t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        v = itemView;
    }

    public void bindData(Event dataModel) {
        try {
            t.setText(dataModel.getString("name"));
            v.setOnClickListener(c -> {
                try {
                    Bundle b = new Bundle();
                    b.putString("eventType", "priv");
                    b.putString("eventId", dataModel.getString("eventid"));
                    Navigation.findNavController(v).navigate(R.id.action_user_calendar_dialog_to_eventDetailsFragment, b);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
