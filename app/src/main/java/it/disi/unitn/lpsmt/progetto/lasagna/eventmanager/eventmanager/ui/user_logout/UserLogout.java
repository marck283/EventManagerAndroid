package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_logout;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class UserLogout {
    private GoogleSignInClient gsi;

    public UserLogout(Context c) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken("22819640695-40ie511a43vdbh8p82o5uhm6b62529rm.apps.googleusercontent.com")
                .build();

        gsi = GoogleSignIn.getClient(c, gso);
    }

    public void userSignOut() {
        gsi.signOut();
    }
}
