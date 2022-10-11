package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.data.googleSignInClient;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.Objects;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.NavigationDrawerActivity;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication.Authentication;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.csrfToken.ApiCSRFClass;

public class GSignInClient extends ApiCSRFClass {
    private BeginSignInRequest request;
    private SignInClient oneTapClient;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;

    public GSignInClient(Activity a) {
        request = BeginSignInRequest.builder()
                .setAutoSelectEnabled(true)
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId("22819640695-40ie511a43vdbh8p82o5uhm6b62529rm.apps.googleusercontent.com")
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                .build();
        oneTapClient = Identity.getSignInClient(a);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestServerAuthCode("22819640695-40ie511a43vdbh8p82o5uhm6b62529rm.apps.googleusercontent.com")
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(a, gso);
    }

    public GoogleSignInAccount getAccount(@NonNull Activity a) {
        return GoogleSignIn.getLastSignedInAccount(a.getApplicationContext());
    }

    public void oneTapSignIn(Activity a, int REQ_ONE_TAP, int REQ_SIGN_IN) {
        oneTapClient.beginSignIn(request)
                .addOnSuccessListener(a, result -> {
                    try {
                        a.startIntentSenderForResult(
                                result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                null, 0, 0, 0);
                    } catch (IntentSender.SendIntentException e) {
                        Log.e("error", "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                    }
                })
                .addOnFailureListener(a, e -> {
                    // No saved credentials found. Launch the One Tap sign-up flow, or
                    // do nothing and continue presenting the signed-out UI.
                    Log.d("noCredentials", e.getLocalizedMessage());
                    signIn(a, REQ_ONE_TAP);
                });
    }

    public void signIn(@NonNull Activity a, int REQ_SIGN_IN) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        a.startActivityForResult(signInIntent, REQ_SIGN_IN);
    }

    public void silentSignIn(NavigationDrawerActivity a) {
        mGoogleSignInClient.silentSignIn()
                .addOnCompleteListener(a, a::handleSignInResult)
                .addOnFailureListener(a, failure -> a.updateUI(null));
    }

    public void serverLogin(@NonNull GoogleSignInAccount account) {
        Authentication auth = new Authentication();

        try {
            auth.login(getToken(), Objects.requireNonNull(account.getServerAuthCode()));
        } catch(NullPointerException ex) {
            Log.w("null", ex.getMessage());
        }
    }
}
