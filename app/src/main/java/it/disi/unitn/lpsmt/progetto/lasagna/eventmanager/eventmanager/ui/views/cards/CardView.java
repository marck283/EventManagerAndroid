package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.views.cards;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;

public class CardView extends MaterialCardView {
    private ImageView image;
    private String nomeAtt, categoria, durata, indirizzo, citta;

    CardView(Context context) {
        super(context);
    }

    public CardView(Context context, String eventPicUrl, String _n, String _c, String _d,
                    String _ind, String _cit) {
        super(context);
        image = new ImageView(context);
        Picasso.get().load(eventPicUrl).into(image);
        nomeAtt = _n;
        categoria = _c;
        durata = _d;
        indirizzo = _ind;
        citta = _cit;
    }

    public void setBody() {
        setBackground(image.getDrawable()); //This may be null

        TextView na = new TextView(this.getContext());
        na.setText(String.format("%s%s", getContext().getString(R.string.event_name), nomeAtt));
        addView(na);

        TextView c = new TextView(this.getContext());
        c.setText(String.format("%s%s", getContext().getString(R.string.event_category_name), categoria));
        addView(c);

        TextView d = new TextView(this.getContext());
        d.setText(String.format("%s%s", getContext().getString(R.string.event_duration), durata));
        addView(d);

        TextView ind = new TextView(this.getContext());
        ind.setText(String.format("%s%s", getContext().getString(R.string.event_address), indirizzo));
        addView(ind);

        TextView cit = new TextView(this.getContext());
        cit.setText(String.format("%s%s", getContext().getString(R.string.event_city), citta));
        addView(cit);
    }
}
