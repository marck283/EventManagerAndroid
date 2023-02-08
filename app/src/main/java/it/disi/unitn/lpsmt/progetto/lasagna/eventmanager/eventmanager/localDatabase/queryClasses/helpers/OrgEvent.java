package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses.helpers;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.LuogoEv;

public class OrgEvent {

    private String eventType, idevent, self, name, category, eventPic, orgName, durata;

    private ArrayList<LuogoEv> luogoEv;

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

    @NonNull
    public String getDurata() {
        return durata;
    }

    public void setDurata(@NonNull String val) {
        durata = val;
    }
}
