package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;

public class LoginViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    private GoogleSignInClient mGoogleSignInClient;

    public LoginViewModel() {
        mText = new MutableLiveData<>();
        mGoogleSignInClient = null;
    }

    public void login(View root, Activity a) {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken("22819640695-40ie511a43vdbh8p82o5uhm6b62529rm.apps.googleusercontent.com")
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(root.getContext(), gso);
        root.findViewById(R.id.sign_in_button).setOnClickListener(c -> signIn(a));
    }

    private void signIn(Activity a) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        a.startActivityForResult(signInIntent, 9001);
    }

    public LiveData<String> getText() {
        return mText;
    }
}