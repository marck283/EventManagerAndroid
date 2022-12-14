package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.userinfo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.Arrays;

public class UserInfo {
    private String profilePic, nome, email, tel;
    private final Integer numEvOrg;
    private final Double valutazioneMedia;

    public UserInfo() {
        numEvOrg = 0;
        valutazioneMedia = 0.0;
    }

    public UserInfo(@NonNull String profilePic, @NonNull String nome, @NonNull String email, @NonNull String tel,
                    @NonNull Integer numEvOrg, @NonNull Double valutazioneMedia) {
        this.profilePic = profilePic;
        this.nome = nome;
        this.email = email;
        this.tel = tel;
        this.numEvOrg = numEvOrg;
        this.valutazioneMedia = valutazioneMedia;
    }

    /**
     * Decodifica il valore della stringa che rappresenta l'immagine dell'evento in Bitmap.
     * @return Il valore decodificato in tipo Bitmap
     */
    public Bitmap decodeBase64(@NonNull String profilePic) {
        Bitmap bitmap = null;
        if(!profilePic.equals("")) {
            Log.i("profilePic", profilePic);
            byte[] decodedImg = null;
            try {
                decodedImg = Base64.decode(profilePic
                        .replace("data:image/png;base64,", "")
                        .replace("data:image/jpeg;base64,",""), Base64.DEFAULT); //Ritorna una stringa in formato Base64
            } catch(IllegalArgumentException ex) {
                decodedImg = profilePic.getBytes();
            } finally {
                Log.i("decodedBytes", Arrays.toString(decodedImg));
                if(decodedImg != null) {
                    bitmap = BitmapFactory.decodeByteArray(decodedImg, 0, decodedImg.length); //Decodifico la stringa ottenuta
                } else {
                    Log.i("nullImg", "Nessuna immagine");
                }
            }
        }
        return bitmap;
    }

    public String getString(@NonNull String string) {
        switch(string) {
            case "profilePic": {
                return profilePic;
            }
            case "nome": {
                return nome;
            }
            case "email": {
                return email;
            }
            case "tel": {
                return tel;
            }
        }
        return "";
    }

    public Integer getNumEvOrg() {
        return numEvOrg;
    }

    public Double getValutazioneMedia() {
        return valutazioneMedia;
    }

    private String stringFromJson(@NonNull Gson gs1, String name, @NonNull JsonObject json) {
        return gs1.fromJson(json.get(name), String.class);
    }

    private Integer integerFromJson(@NonNull Gson gs1, String name, @NonNull JsonObject json) {
        return gs1.fromJson(json.get(name), Integer.class);
    }

    private Double doubleFromJson(@NonNull Gson gs1, String name, @NonNull JsonObject json) {
        return gs1.fromJson(json.get(name), Double.class);
    }

    public UserInfo parseJSON(@NonNull JsonObject json) {
        GsonBuilder gson = new GsonBuilder();
        Gson gs1 = gson.create();

        return new UserInfo(stringFromJson(gs1, "picture", json), stringFromJson(gs1, "nome", json),
                stringFromJson(gs1, "email", json), stringFromJson(gs1, "tel", json),
                integerFromJson(gs1, "numEvOrg", json), doubleFromJson(gs1, "valutazioneMedia", json));
    }
}
