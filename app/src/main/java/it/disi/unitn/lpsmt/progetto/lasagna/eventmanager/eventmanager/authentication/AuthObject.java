package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication;

import androidx.annotation.Nullable;

public class AuthObject {
    private final String csrfToken;
    private final String googleJwt;

    @Nullable
    private final String userId;

    public AuthObject(String ct, String jwt, @Nullable String userId) {
        csrfToken = ct;
        googleJwt = jwt;
        this.userId = userId;
    }

    public String getJwt() {
        return googleJwt;
    }

    @Nullable
    public String getUserId() {
        return userId;
    }
}
