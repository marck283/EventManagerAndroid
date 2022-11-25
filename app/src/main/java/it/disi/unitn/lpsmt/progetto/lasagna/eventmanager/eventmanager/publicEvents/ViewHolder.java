package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.Event;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventHolder;

public class ViewHolder extends EventHolder {
    private final ImageView imgView;
    private final TextView evName, orgName, firstDate, firstHour; //TextView per le informazioni sull'evento

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        imgView = itemView.findViewById(R.id.imageView2);
        evName = itemView.findViewById(R.id.textView3);
        orgName = itemView.findViewById(R.id.organizerName);
        firstDate = itemView.findViewById(R.id.dayTextView);
        firstHour = itemView.findViewById(R.id.firstHour);

        itemView.setOnClickListener(l -> {
            //Avvia un'altro Fragment che richiede le informazioni sull'evento
            //Passare l'ID dell'evento tramite il metodo newInstance
        });
    }

    public void bindData(Event dataModel) {
        try {
            imgView.setImageBitmap(dataModel.decodeBase64());
            evName.setText(dataModel.getString("name"));
            orgName.setText(dataModel.getString("orgName"));

            String[] firstDate1 = dataModel.getLuogo(0).getData().split("-");
            String firstDate2 = firstDate1[2] + "/" + firstDate1[1] + "/" + firstDate1[0];
            firstDate.setText(firstDate2);
            firstHour.setText(dataModel.getLuogo(0).getOra());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
