package it.disi.unitn.lpsmt.lasagna.localdatabase.queryClasses;

import android.app.Activity;

import androidx.annotation.NonNull;

import it.disi.unitn.lpsmt.lasagna.localdatabase.AppDatabase;

public class DBThread extends Thread {
    protected static AppDatabase db;

    public DBThread(@NonNull Activity a) {
        db = AppDatabase.getInstance(a.getApplicationContext());
    }

    public void close() {
        db.close();
    }
}
