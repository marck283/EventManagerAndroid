package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication.accountIntegration;

import androidx.annotation.NonNull;

public class GoogleIntegration {
    private String googleJwt;

    public GoogleIntegration(@NonNull String gJwt) {
        googleJwt = gJwt;
    }

    public String getJWT() {
        return googleJwt;
    }
}
