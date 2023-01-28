package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.Event;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventHolder;

public class ViewHolder extends EventHolder {
    private final ImageView imgView;
    private final TextView evName, orgName, firstDate, firstHour; //TextView per le informazioni sull'evento

    private final View itemView;

    private final Fragment f;

    public ViewHolder(@NonNull Fragment f, @NonNull View itemView) {
        super(itemView);
        imgView = itemView.findViewById(R.id.imageView2);
        evName = itemView.findViewById(R.id.textView3);
        orgName = itemView.findViewById(R.id.textView24);
        firstDate = itemView.findViewById(R.id.textView5);
        firstHour = itemView.findViewById(R.id.textView4);
        this.f = f;
        this.itemView = itemView;
    }

    public void bindData(Event dataModel) {
        try {
            imgView.setImageBitmap(dataModel.decodeBase64());
            evName.setText(dataModel.getString("name"));
            orgName.setText(f.getString(R.string.organizer_name, dataModel.getString("orgName")));

            String[] firstDate1 = dataModel.getLuogo(0).getData().split("-");
            String firstDate2 = firstDate1[1] + "/" + firstDate1[0] + "/" + firstDate1[2];
            firstDate.setText(f.getString(R.string.event_day, firstDate2));
            firstHour.setText(f.getString(R.string.event_time, dataModel.getLuogo(0).getOra()));

            itemView.setOnClickListener(l -> {
                try {
                    Bundle b = new Bundle();
                    b.putString("eventType", "pub");
                    b.putString("eventId", dataModel.getString("eventid"));
                    Navigation.findNavController(itemView).navigate(R.id.action_nav_event_list_to_eventDetailsFragment, b);
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
