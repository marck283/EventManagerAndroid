package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

public class UserAccount {
    private String token, email, id, self;
    private Uri profilePic;

    public UserAccount(String token, String email, String id, String self, Uri profilePic) {
        this.token = token;
        this.email = email;
        this.id = id;
        this.self = self;
        this.profilePic = profilePic;
    }

    public String getString(@NonNull String name) {
        switch(name) {
            case "token": {
                return token;
            }
            case "email": {
                return email;
            }
            case "id": {
                return id;
            }
            case "self": {
                return self;
            }
            default: {
                Log.i("null", "Nessun parametro con tale nome.");
                return null;
            }
        }
    }
}
