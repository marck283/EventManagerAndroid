package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication;

public class AuthObject {
    private String csrfToken, googleJwt;

    public AuthObject(String ct, String jwt) {
        csrfToken = ct;
        googleJwt = jwt;
    }

    public String getToken() {
        return csrfToken;
    }

    public String getJwt() {
        return googleJwt;
    }
}
