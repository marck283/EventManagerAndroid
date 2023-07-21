package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class Event {
    private final String id; //Tipo dell'evento
    private final String eventid; //ID dell'evento
    private final String self; //URL dell'evento
    private final String name;
    private final String category;
    private final String eventPic; //Informazioni sull'evento
    private final String orgName;
    private final ArrayList<LuogoEv> luogo;

    private final String durata;

    /**
     * Costruisce l'oggetto PublicEvent. NOTA: nessuno dei seguenti parametri pu&ograve; essere null.
     * @param id Il tipo dell'evento ("pub" poiché si tratta di eventi pubblici)
     * @param idevent L'ID dell'evento
     * @param s L'URL dell'evento
     * @param n Il nome dell'evento
     * @param c La categoria dell'evento
     * @param ep L'immagine dell'evento codificata come valore stringa base64
     * @param orgName Il nome dell'utente organizzatore
     * @param luogo La lista di luoghi in cui si terrà l'evento
     */
    public Event(@NotNull String id, @NotNull String idevent, @NotNull String s,
                 @NotNull String n, @NotNull String c, @NotNull String ep, @NotNull String orgName,
                 @NotNull ArrayList<LuogoEv> luogo, @NonNull String durata) {
        this.id = id;
        eventid = idevent;
        self = s;
        name = n;
        category = c;
        eventPic = ep;
        this.orgName = orgName;
        this.luogo = luogo;
        this.durata = durata;
    }

    /**
     * Decodifica il valore della stringa base64 che rappresenta l'immagine dell'evento in Bitmap.
     * @return Il valore decodificato in tipo Bitmap
     */
    public Bitmap decodeBase64() {
        byte[] decodedImg = Base64.decode(eventPic
                .replace("data:image/png;base64,", "")
                .replace("data:image/jpeg;base64,",""), Base64.DEFAULT); //Ritorna una stringa in formato Base64
        return BitmapFactory.decodeByteArray(decodedImg, 0, decodedImg.length); //Decodifico la stringa ottenuta
    }

    public String getId() {
        return eventid;
    }

    public String getEventType() {
        return id;
    }

    public String getSelf() {
        return self;
    }

    public String getEventName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getEventPic() {
        return eventPic;
    }

    public String getOrgName() {
        return orgName;
    }

    public ArrayList<LuogoEv> getLuogoEv() {
        return luogo;
    }

    public String getDurata() {
        return durata;
    }

    public LuogoEv getLuogo(int i) {
        return luogo.get(i);
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
            case "orgName": {
                return orgName;
            }
            default: {
                Log.i("field", field);
                throw new Exception("No field using this name.");
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event that = (Event) o;
        return id.equals(that.id) && eventid.equals(that.eventid) && self.equals(that.self) && name.equals(that.name) && category.equals(that.category) && eventPic.equals(that.eventPic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventid, self, name, category, eventPic);
    }
}
