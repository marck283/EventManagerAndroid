package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents;

import android.util.Log;
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
        orgName = itemView.findViewById(R.id.textView5);

        itemView.setOnClickListener(l -> {
            //Avvia un'altra Activity che richiede le informazioni sull'evento
            //Passare l'ID dell'evento tramite Bundle
            Log.i("click", "card clicked");
        });
    }

    public void bindData(Event dataModel) {
        try {
            imgView.setImageBitmap(dataModel.decodeBase64());
            evName.setText(dataModel.getString("name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
