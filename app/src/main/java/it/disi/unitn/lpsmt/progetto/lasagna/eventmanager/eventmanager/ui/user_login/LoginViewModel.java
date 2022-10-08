package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.data.googleSignInClient.GSignInClient;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.data.model.LoggedInUser;

public class LoginViewModel extends ViewModel {
    private LoggedInUser liuser;
    private GSignInClient gsi;

    public LoginViewModel() {
        liuser = null;
        gsi = null;
    }

    public void init(@NonNull View root) {
        Log.i("call", "Call to LoginViewModel on constructor");
        gsi = new GSignInClient(root);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(root.getContext());
        if(account != null) {
            liuser = new LoggedInUser(account.getId(), account.getDisplayName(), account.getEmail());
        }
    }

    public void login(@NonNull Fragment f) {
        gsi.signIn(f);
    }
}