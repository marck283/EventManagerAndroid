package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;
    private String displayEmail;

    public LoggedInUser(String userId, String displayName, String displayEmail) {
        this.userId = userId;
        this.displayName = displayName;
        this.displayEmail = displayEmail;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
    public String getDisplayEmail() {
        return displayEmail;
    }
}