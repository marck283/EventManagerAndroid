package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.eventReviews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Review implements Serializable {
    private String userName, userPic, description;
    private float rating; //Rating in decimi

    public Review(String userName, String userPic, String description, float rating) {
        this.userName = userName;
        this.userPic = userPic;
        this.description = description;
        this.rating = rating;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPic() {
        return userPic;
    }

    public String getDescription() {
        return description;
    }

    public float getRating() {
        return rating;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Review) {
            return userName.equals(((Review) obj).userName) && userPic.equals(((Review)obj).userPic);
        }
        return super.equals(obj);
    }
}
