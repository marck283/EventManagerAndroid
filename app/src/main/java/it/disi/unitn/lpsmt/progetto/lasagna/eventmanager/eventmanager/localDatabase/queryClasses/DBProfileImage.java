package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.DAOs.UserDAO;

public class DBProfileImage extends DBThread {
    private final UserDAO user;
    private final String email;
    private MutableLiveData<String> profilePic;

    public DBProfileImage(@NonNull Activity a, @NonNull String email) {
        super(a);
        user = db.getUserDAO();
        this.email = email;
        profilePic = new MutableLiveData<>();
    }

    @Override
    public void run() {
        profilePic = new MutableLiveData<>(user.getProfilePic(email));
    }

    public LiveData<String> getProfilePic() {
        return profilePic;
    }
}
