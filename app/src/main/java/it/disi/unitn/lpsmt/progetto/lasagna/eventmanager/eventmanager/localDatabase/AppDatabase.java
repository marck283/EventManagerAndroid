package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.daos.OrgEvDAO;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.daos.UserDAO;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.converters.ListConverter;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.entities.OrgEvent;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.entities.User;

//NOTA: classe ed entità associate sono da implementare come qui indicato: https://developer.android.com/training/data-storage/room#java

@Database(entities = {User.class, OrgEvent.class}, version = 5)
@TypeConverters({ListConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    /**
     * Creates the open helper to access the database. Generated class already implements this
     * method.
     * Note that this method is called when the RoomDatabase is initialized.
     *
     * @param config The configuration of the Room database.
     * @return A new SupportSQLiteOpenHelper to be used while connecting to the database.
     */
    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(@NonNull DatabaseConfiguration config) {
        return config.sqliteOpenHelperFactory.create(SupportSQLiteOpenHelper.Configuration.builder(config.context).build());
    }

    /**
     * Called when the RoomDatabase is created.
     * <p>
     * This is already implemented by the generated code.
     *
     * @return Creates a new InvalidationTracker.
     */
    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, "OrgEvents", "Users");
    }

    /**
     * Deletes all rows from all the tables that are registered to this database as
     * database entities.
     * After deleting the rows, Room will set a WAL checkpoint and run VACUUM. This means that the
     * data is completely erased. The space will be reclaimed by the system if the amount surpasses
     * the threshold of database file size.
     *
     * @see <a href="https://www.sqlite.org/fileformat.html">Database File Format</a>
     */
    @Override
    public void clearAllTables() {
    }

    public abstract UserDAO getUserDAO();

    public abstract OrgEvDAO getOrgEvDAO();
}
