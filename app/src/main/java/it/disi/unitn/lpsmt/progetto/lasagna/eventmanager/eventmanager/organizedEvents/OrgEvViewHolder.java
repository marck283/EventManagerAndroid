package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.organizedEvents;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.Event;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventHolder;

public class OrgEvViewHolder extends EventHolder {
    private final MaterialButton t;
    private final View v;
    private final Fragment f;

    public OrgEvViewHolder(@NonNull Fragment f, @NonNull View itemView) {
        super(itemView);
        t = itemView.findViewById(R.id.event_name_text_view);
        t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        v = itemView;
        this.f = f;
    }

    @Override
    public void bindData(Event dataModel) {
        try {
            t.setText(dataModel.getString("name"));
            t.setOnClickListener(c -> {
                try {
                    Bundle b = new Bundle();
                    b.putString("eventType", "org");
                    b.putString("eventId", dataModel.getString("eventid"));
                    Navigation.findNavController(t).navigate(R.id.action_user_calendar_dialog_to_eventDetailsFragment, b);
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
