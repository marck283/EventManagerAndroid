package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication.accountIntegration;

import androidx.annotation.NonNull;

public class FBIntegration {
    private int fbId; //ID utente Facebook
    private final String fbToken;

    public FBIntegration(int fbId, @NonNull String fbToken) {
        this.fbId = fbId;
        this.fbToken = fbToken;
    }

    public int getID() {
        return fbId;
    }

    public String getFbToken() {
        return fbToken;
    }
}
