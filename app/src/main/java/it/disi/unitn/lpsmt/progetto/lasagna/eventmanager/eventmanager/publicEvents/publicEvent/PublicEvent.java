package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents.publicEvent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

public class PublicEvent {
    private String id; //Tipo dell'evento
    private String eventid; //ID dell'evento
    private String self; //URL dell'evento
    private String name, category, eventPic; //Informazioni sull'evento

    public PublicEvent(String id, String idevent, String s, String n, String c, String ep) {
        this.id = id;
        eventid = idevent;
        self = s;
        name = n;
        category = c;
        eventPic = ep;
    }

    //Metodo di stampa per il debug
    public void print() {
        Log.i("info", String.valueOf(id));
        Log.i("info", String.valueOf(eventid));
        Log.i("info", String.valueOf(self));
        Log.i("info", String.valueOf(name));
    }

    public Bitmap decodeBase64() {
        byte[] decodedImg = Base64.decode(eventPic
                .replace("data:image/png;base64,", "")
                .replace("data:image/jpeg;base64,",""), Base64.DEFAULT); //Ritorna una stringa in formato Base64
        return BitmapFactory.decodeByteArray(decodedImg, 0, decodedImg.length); //Decodifico la stringa ottenuta
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
