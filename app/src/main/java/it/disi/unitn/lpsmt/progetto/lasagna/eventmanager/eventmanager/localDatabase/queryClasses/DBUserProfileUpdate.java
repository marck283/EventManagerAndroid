package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses;

import android.app.Activity;

import androidx.annotation.NonNull;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.DAOs.UserDAO;

public class DBUserProfileUpdate extends DBThread {
    private UserDAO user;
    private String profilePic, userEmail;

    public DBUserProfileUpdate(@NonNull Activity a, String profilePic, String email) {
        super(a);
        user = db.getUserDAO();
        this.profilePic = profilePic;
        userEmail = email;
    }

    @Override
    public void run() {
        user.updateProfilePic(profilePic, userEmail);
    }
}
