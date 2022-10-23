package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.Event;

public class ViewHolder extends RecyclerView.ViewHolder {
    private ImageView imgView;
    private TextView evName, catName; //TextView per il nome dell'evento e la sua categoria

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        imgView = itemView.findViewById(R.id.imageView2);
        evName = itemView.findViewById(R.id.textView3);
        catName = itemView.findViewById(R.id.textView5);

        itemView.setOnClickListener(l -> {
            //Avvia un'altra Activity che richiede le informazioni sull'evento
            //Passare l'ID dell'evento tramite Bundle
            Log.i("click", "card clicked");
        });
    }

    public void bindData(Event dataModel, Context context) {
        try {
            imgView.setImageBitmap(dataModel.decodeBase64());
            evName.setText(dataModel.getString("name"));
            catName.setText(dataModel.getString("category"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
