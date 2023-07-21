package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.LuogoEv;

@Entity(tableName = "OrgEvents")
public class OrgEvent implements Serializable {
    @NonNull
    @ColumnInfo(name = "id")
    private String eventType;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "idevent")
    private String idevent;

    @NonNull
    @ColumnInfo(name = "self")
    private String self;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "category")
    private String category;

    @NonNull
    @ColumnInfo(name = "eventPic")
    private String eventPic;

    @NonNull
    @ColumnInfo(name = "orgName")
    private String orgName;

    @NonNull
    @ColumnInfo(name = "luogoEv")
    private ArrayList<LuogoEv> luogoEv;

    @ColumnInfo(name = "durata")
    private String durata;

    public OrgEvent(@NonNull String eventType, @NonNull String idevent, @NonNull String self,
                    @NonNull String name, @NonNull String category, @NonNull String eventPic,
                    @NonNull String orgName, @NonNull ArrayList<LuogoEv> luogoEv, @NonNull String durata) {
        this.eventType = eventType;
        this.idevent = idevent;
        this.self = self;
        this.name = name;
        this.category = category;
        this.eventPic = eventPic;
        this.orgName = orgName;
        this.luogoEv = luogoEv;
        this.durata = durata;
    }

    @NonNull
    public String getEventType() {
        return eventType;
    }

    public void setEventType(@NonNull String val) {
        eventType = val;
    }

    @NonNull
    public String getIdevent() {
        return idevent;
    }

    public void setIdevent(@NonNull String val) {
        idevent = val;
    }

    @NonNull
    public String getSelf() {
        return self;
    }

    public void setSelf(@NonNull String val) {
        self = val;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String val) {
        name = val;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    public void setCategory(@NonNull String val) {
        category = val;
    }

    @NonNull
    public String getEventPic() {
        return eventPic;
    }

    public Bitmap decodeBase64() {
        byte[] decodedImg = Base64.decode(eventPic
                .replace("data:image/png;base64,", "")
                .replace("data:image/jpeg;base64,",""), Base64.DEFAULT); //Ritorna una stringa in formato Base64
        return BitmapFactory.decodeByteArray(decodedImg, 0, decodedImg.length); //Decodifico la stringa ottenuta
    }

    public void setEventPic(@NonNull String val) {
        eventPic = val;
    }

    @NonNull
    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(@NonNull String val) {
        orgName = val;
    }

    @NonNull
    public ArrayList<LuogoEv> getLuogoEv() {
        return luogoEv;
    }

    public void setLuogoEv(@NonNull ArrayList<LuogoEv> val) {
        luogoEv = val;
    }

    public LuogoEv getLuogo(@NonNull String data, @NonNull String ora) {
        LuogoEv res = null;
        
        for(LuogoEv l: luogoEv) {
            if(l.getData().equals(data) && l.getOra().equals(ora)) {
                res = l;
            }
        }

        return res;
    }

    public List<LuogoEv> getOrari(@NonNull String day) {
        ArrayList<LuogoEv> res = new ArrayList<>();

        for(LuogoEv l: luogoEv) {
            if(l.getData().equals(day)) {
                res.add(l);
            }
        }

        return res;
    }

    @NonNull
    public String getDurata() {
        return durata;
    }

    public void setDurata(@NonNull String val) {
        durata = val;
    }
}
