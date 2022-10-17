package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_logout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

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

    public void userLogout(Fragment f) {
        gsi = GoogleSignIn.getClient(f.requireActivity().getApplicationContext(), gso);
        gsi.signOut();
    }
}