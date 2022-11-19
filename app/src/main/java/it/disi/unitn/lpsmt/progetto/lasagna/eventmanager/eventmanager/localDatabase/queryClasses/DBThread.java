package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.AppDatabase;

public class DBThread extends Thread {
    protected static AppDatabase db;

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE User1 ('id' INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT '0'," +
                    "'email' TEXT NOT NULL DEFAULT '', 'nome' TEXT, 'profilePic' TEXT, 'gServerAuthCode' TEXT," +
                    "'gToken' TEXT, 'tel' TEXT, 'eventiCreati' TEXT, 'eventiIscritto' TEXT, 'numEvOrg' INTEGER," +
                    "'valutazioneMedia' REAL)");
            database.execSQL("INSERT INTO User1 SELECT * FROM User;");
            database.execSQL("DROP TABLE User");
            database.execSQL("ALTER TABLE User1 RENAME TO User");
        }
    };

    public DBThread(@NonNull Activity a) {
        db = Room.databaseBuilder(a.getApplicationContext(), AppDatabase.class, "EventManagerDB").addMigrations(MIGRATION_1_2).build();
    }

    public void close() {
        db.close();
    }
}
