package it.disi.unitn.lpsmt.lasagna.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.facebook.AccessToken;
import com.google.android.gms.common.SignInButton;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

import it.disi.unitn.lpsmt.lasagna.AuthProviders;
import it.disi.unitn.lpsmt.lasagna.csrfToken.CsrfToken;
import it.disi.unitn.lpsmt.lasagna.gSignIn.GSignIn;
import it.disi.unitn.lpsmt.lasagna.gSignIn.OnSignInListener;
import it.disi.unitn.lpsmt.lasagna.network.NetworkCallback;

public class GoogleLogin {
    private final GSignIn signIn;

    private final Activity a;

    public GoogleLogin(@NonNull Activity a, @IdRes int signinbid, @StringRes int noconn, @StringRes int noconnmsg,
                       @StringRes int clientID) {
        this.a = a;
        signIn = new GSignIn(a, clientID);
        SignInButton signInButton = a.findViewById(signinbid);
        if(!signInButton.hasOnClickListeners()) {
            signInButton.setOnClickListener(v -> {
                //Sign in the user when the button is clicked
                NetworkCallback callback = new NetworkCallback(a);
                if(callback.isOnline(a)) {
                    if(v.getId() == signinbid) {
                        signIn();
                    }
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(a).create();
                    dialog.setTitle(noconn);
                    dialog.setMessage(a.getString(noconnmsg));
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                    dialog.show();
                }
            });
        }
    }

    private void signIn() {
        final OnSignInListener listener = new OnSignInListener() {

            @Override
            public void onSuccess(GoogleIdTokenCredential credential) {
                Intent intent = setUpIntent(AuthProviders.GOOGLE, null);
                CsrfToken token = new CsrfToken(a, credential.getIdToken(), null, AuthProviders.GOOGLE, intent);
                token.start();
            }

            @Override
            public void onError(Exception error) {
                error.printStackTrace();
            }
        };
        signIn.signIn(a, listener);
    }

    @NonNull
    public Intent setUpIntent(@NonNull String which, @Nullable AccessToken accessToken) {
        Intent intent = new Intent();
        intent.setClassName("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui", "NavigationDrawerActivity");
        if(which.equals(AuthProviders.FACEBOOK) && accessToken != null) {
            intent.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fAccessToken", accessToken);
        }
        return intent;
    }
}
