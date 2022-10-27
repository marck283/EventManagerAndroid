package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.NavigationDrawerActivity;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.DAOs.UserDAO;

public class DBProfileImage extends DBThread {
    private final UserDAO user;
    private final String email;
    private final Activity a;
    private final LinearLayout l;

    public DBProfileImage(@NonNull Activity a, @NonNull String email, @NonNull LinearLayout l) {
        super(a);
        user = db.getUserDAO();
        this.email = email;
        this.a = a;
        this.l = l;
    }

    @Override
    public void run() {
        String profilePic = user.getProfilePic(email);
        synchronized (this) {
            try {
                if(profilePic != null) {
                    while(profilePic.equals("")) {
                        wait();
                    }
                    if(a instanceof NavigationDrawerActivity) {
                        ImageView v = l.findViewById(R.id.imageView);
                        v.setImageURI(Uri.parse(profilePic));
                    }
                } else {
                    Log.i("noPicture", "No profile picture available right now.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
