package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.privateEvents;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.Event;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventHolder;

public class PrivEvViewHolder extends EventHolder {
    private final MaterialButton t;
    private final View v;

    private String day;

    public PrivEvViewHolder(@NonNull View itemView, @NonNull String day) {
        super(itemView);
        t = itemView.findViewById(R.id.event_name_text_view);
        t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        v = itemView;
        this.day = day;
    }

    public void bindData(Event dataModel) {
        try {
            t.setText(dataModel.getString("name"));
            t.setOnClickListener(c -> {
                try {
                    Bundle b = new Bundle();
                    b.putString("eventType", "iscr");
                    b.putString("eventId", dataModel.getString("eventid"));
                    b.putString("day", day);
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
