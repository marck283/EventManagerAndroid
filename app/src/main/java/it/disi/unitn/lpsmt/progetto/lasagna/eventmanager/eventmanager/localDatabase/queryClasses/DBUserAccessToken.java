package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses;

import android.app.Activity;
import androidx.annotation.NonNull;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.DAOs.UserDAO;

public class DBUserAccessToken extends DBThread {
    private UserDAO user;
    private String userEmail, gToken;

    public DBUserAccessToken(@NonNull Activity a, @NonNull String gToken, @NonNull String userEmail) {
        super(a);
        user = db.getUserDAO();
        this.userEmail = userEmail;
        this.gToken = gToken;
    }

    public void run() {
        synchronized(this) {
            user.updateGToken(gToken, userEmail);
            close();
        }
    }
}
