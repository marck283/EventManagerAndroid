package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.eventReviews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.annotation.Nullable;

public class Review {
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

    /**
     * Decodifica il valore della stringa base64 che rappresenta l'immagine dell'evento in Bitmap.
     * @return Il valore decodificato in tipo Bitmap
     */
    public Bitmap decodeBase64() {
        byte[] decodedImg = Base64.decode(userPic
                .replace("data:image/png;base64,", "")
                .replace("data:image/jpeg;base64,",""), Base64.DEFAULT); //Ritorna una stringa in formato Base64
        return BitmapFactory.decodeByteArray(decodedImg, 0, decodedImg.length); //Decodifico la stringa ottenuta
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Review) {
            return userName.equals(((Review) obj).userName) && userPic.equals(((Review)obj).userPic);
        }
        return super.equals(obj);
    }
}
