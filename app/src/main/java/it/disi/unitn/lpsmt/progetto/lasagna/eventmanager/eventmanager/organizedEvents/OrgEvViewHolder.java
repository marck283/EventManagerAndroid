package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.organizedEvents;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventHolder;

public class OrgEvViewHolder extends EventHolder {
    private final MaterialButton t;

    private final String day;

    @IdRes
    private final int actionId;

    public OrgEvViewHolder(@NonNull View itemView, @NonNull String day) {
        super(itemView);
        t = itemView.findViewById(R.id.event_name_text_view);
        t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        this.day = day;
        actionId = R.id.action_user_calendar_dialog_to_eventDetailsFragment;
    }

    public OrgEvViewHolder(@NonNull View itemView) {
        super(itemView);
        t = itemView.findViewById(R.id.event_name_text_view);
        t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        this.day = null;
        this.actionId = R.id.action_eventManagement_to_eventDetailsFragment;
    }

    @Override
    public void bindData(it.disi.unitn.lpsmt.lasagna.localdatabase.Event dataModel) {
        try {
            t.setText(dataModel.getString("name"));
            if(!t.hasOnClickListeners()) {
                t.setOnClickListener(c -> {
                    try {
                        Bundle b = new Bundle();
                        b.putString("eventType", "org");
                        b.putString("eventId", dataModel.getString("eventid"));
                        b.putString("day", day);

                        //Rendere il tipo di azione dinamico
                        Navigation.findNavController(t).navigate(actionId, b);
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
