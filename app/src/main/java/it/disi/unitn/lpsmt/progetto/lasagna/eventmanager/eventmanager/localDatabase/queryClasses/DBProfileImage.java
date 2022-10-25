package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.NavigationDrawerActivity;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.DAOs.UserDAO;

public class DBProfileImage extends DBThread {
    private UserDAO user;
    private String email;
    private Activity a;

    public DBProfileImage(@NonNull Activity a, @NonNull String email) {
        super(a);
        user = db.getUserDAO();
        this.email = email;
        this.a = a;
    }

    @Override
    public void run() {
        String profilePic = user.getProfilePic(email);
        if(a instanceof NavigationDrawerActivity) {
            if(profilePic != null && !profilePic.equals("")) {
                String cleanImage;
                if(profilePic.contains("data:image/png;base64,")) {
                    cleanImage = profilePic.replace("data:image/png;base64,", "");
                } else {
                    cleanImage = profilePic.replace("data:image/jpeg;base64,","");
                }
                byte[] decodedString = Base64.decode(cleanImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                ImageView v = a.findViewById(R.id.imageView);
                v.setImageBitmap(decodedByte);
            } else {
                Log.i("null", "no profile image");
            }
        }
        db.close();
    }
}
