package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

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

    private Bitmap getImageBitmap(String uri) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(uri);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("IOException", "Error getting bitmap", e);
        }
        return bm;
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
                        Bitmap bm = getImageBitmap(profilePic);
                        ImageView v = l.findViewById(R.id.imageView);
                        v.setImageBitmap(bm);
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
