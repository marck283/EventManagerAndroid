package it.disi.unitn.lpsmt.lasagna.localdatabase.daos;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import it.disi.unitn.lpsmt.lasagna.localdatabase.entities.OrgEvent;

@Dao
public interface OrgEvDAO {

    @Query("SELECT O.id, O.idevent, O.self, O.name, O.category, O.eventPic, O.orgName, O.luogoEv, O.durata " +
            "FROM OrgEvents O WHERE O.name = :evName")
    List<OrgEvent> getOrgEventsByName(@NonNull String evName);

    @Query("SELECT O.id, O.idevent, O.self, O.name, O.category, O.eventPic, O.orgName, O.luogoEv, O.durata " +
            "FROM OrgEvents O")
    List<OrgEvent> getAllOrgEvents();

    @Query("SELECT O.id, O.idevent, O.self, O.name, O.category, O.eventPic, O.orgName, O.luogoEv, O.durata " +
            "FROM OrgEvents O WHERE O.idevent = :idevent")
    OrgEvent getOrgEventById(@NonNull String idevent);

    @Query("DELETE FROM OrgEvents")
    void deleteAll();

    @Insert
    void insert(@NonNull OrgEvent o);

}
