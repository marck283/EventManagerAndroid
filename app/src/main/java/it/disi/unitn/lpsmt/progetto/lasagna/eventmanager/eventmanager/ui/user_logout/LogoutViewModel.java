package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_logout;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.NavigationDrawerActivity;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;

public class LogoutViewModel extends ViewModel {
    private GoogleSignInClient gsi;
    private GoogleSignInOptions gso;

    public LogoutViewModel() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken("22819640695-40ie511a43vdbh8p82o5uhm6b62529rm.apps.googleusercontent.com")
                .build();
    }

    private void handleSignInResult(NavigationDrawerActivity f) {
        NavigationView v = f.findViewById(R.id.nav_view);
        v.getMenu().clear();
        v.inflateMenu(R.menu.navmenu_not_logged_in);
    }

    public void userLogout(@NonNull NavigationDrawerActivity f) {
        gsi = GoogleSignIn.getClient(f.getApplicationContext(), gso);
        gsi.signOut().addOnSuccessListener(f, v -> handleSignInResult(f));
    }
}