package it.disi.unitn.lpsmt.lasagna.localdatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.disi.unitn.lpsmt.lasagna.localdatabase.converters.ListConverter;
import it.disi.unitn.lpsmt.lasagna.localdatabase.daos.OrgEvDAO;
import it.disi.unitn.lpsmt.lasagna.localdatabase.daos.UserDAO;
import it.disi.unitn.lpsmt.lasagna.localdatabase.entities.OrgEvent;
import it.disi.unitn.lpsmt.lasagna.localdatabase.entities.User;

@Database(entities = {User.class, OrgEvent.class}, version = 6, exportSchema = false)
@TypeConverters({ListConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "EventManagerDB").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract UserDAO getUserDAO();

    public abstract OrgEvDAO getOrgEvDAO();
}
