package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents.publicEvent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class PublicEvent {
    private String id; //Tipo dell'evento
    private String eventid; //ID dell'evento
    private String self; //URL dell'evento
    private String name, category, eventPic; //Informazioni sull'evento

    /**
     * Costruisce l'oggetto PublicEvent. NOTA: nessuno dei seguenti parametri pu&ograve; essere null.
     * @param id Il tipo dell'evento ("pub" poiché si tratta di eventi pubblici)
     * @param idevent L'ID dell'evento
     * @param s L'URL dell'evento
     * @param n Il nome dell'evento
     * @param c La categoria dell'evento
     * @param ep L'immagine dell'evento codificata come valore stringa base64
     */
    public PublicEvent(@NotNull String id, @NotNull String idevent, @NotNull String s,
                       @NotNull String n, @NotNull String c, @NotNull String ep) {
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

    /**
     * Decodifica il valore della stringa che rappresenta l'immagine dell'evento in Bitmap.
     * @return Il valore decodificato in tipo Bitmap
     */
    public Bitmap decodeBase64() {
        byte[] decodedImg = Base64.decode(eventPic
                .replace("data:image/png;base64,", "")
                .replace("data:image/jpeg;base64,",""), Base64.DEFAULT); //Ritorna una stringa in formato Base64
        return BitmapFactory.decodeByteArray(decodedImg, 0, decodedImg.length); //Decodifico la stringa ottenuta
    }

    /**
     * Ottiene il valore del campo specificato come parametro. NOTA: il parametro fornito non può essere null.
     * @exception Exception Se il valore del parametro fornito è null, sarà ritornata un'eccezione di tipo Exception.
     * @param field Il nome del campo specificato scelto tra "id", "eventid", "self", "name", "category" o "eventPic".
     * @return Il valore del campo specificato come parametro.
     */
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
