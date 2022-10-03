package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents.publicEvent;

import android.util.Log;

import androidx.annotation.NonNull;

public class PublicEvent {
    private String id; //Tipo dell'evento
    private String eventid; //ID dell'evento
    private String self; //URL dell'evento
    private String name, category, eventPic; //Informazioni sull'evento

    public PublicEvent(String id, String evId, String s, String n, String c, String ep) {
        this.id = id;
        eventid = evId;
        Log.i("idev", String.valueOf(eventid)); //Perché non viene eseguita questa istruzione?

        //Devo anche risolvere l'errore su RecyclerView "no adapter attached; skipping layout"

        self = s;
        name = n;
        category = c;
        eventPic = ep;
    }

    public String getString(@NonNull String field) throws Exception {
        switch(field) {
            case "id": {
                return id;
            }
            case "eventid": {
                return eventid;
            }
            case "self": {
                return self;
            }
            case "name": {
                return name;
            }
            case "category": {
                return category;
            }
            case "eventPic": {
                return eventPic;
            }
            default: {
                Log.i("field", field);
                throw new Exception("No field using this name.");
            }
        }
    }
}
