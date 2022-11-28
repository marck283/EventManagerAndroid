package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.room.Room;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.AppDatabase;

public class DBThread extends Thread {
    protected static AppDatabase db;

    public DBThread(@NonNull Activity a) {
        db = Room.databaseBuilder(a.getApplicationContext(), AppDatabase.class, "EventManagerDB").fallbackToDestructiveMigration().build();
    }

    public void close() {
        db.close();
    }
}
