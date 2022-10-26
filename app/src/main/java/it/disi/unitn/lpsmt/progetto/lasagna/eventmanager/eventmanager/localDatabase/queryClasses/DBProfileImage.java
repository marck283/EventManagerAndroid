package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.NavigationDrawerActivity;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.DAOs.UserDAO;

public class DBProfileImage extends DBThread {
    private UserDAO user;
    private String email;
    private Activity a;
    private LinearLayout l;

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
                while(profilePic.equals("")) {
                    wait();
                }
                if(a instanceof NavigationDrawerActivity) {
                    ImageView v = l.findViewById(R.id.imageView);
                    v.setImageURI(Uri.parse(profilePic));
                }
                db.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
