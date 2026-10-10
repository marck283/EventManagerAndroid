package it.disi.unitn.lpsmt.lasagna.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.google.android.gms.common.SignInButton;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

import it.disi.unitn.lpsmt.lasagna.gSignIn.GSignIn;
import it.disi.unitn.lpsmt.lasagna.gSignIn.OnSignInListener;
import it.disi.unitn.lpsmt.lasagna.login.model.LoggedInUser;
import it.disi.unitn.lpsmt.lasagna.network.NetworkCallback;
import it.disi.unitn.lpsmt.lasagna.network.model.LoginResponse;
import it.disi.unitn.lpsmt.lasagna.network.repository.AuthRepository;
import it.disi.unitn.lpsmt.lasagna.sharedprefs.sharedpreferences.SharedPrefs;

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
                Intent intent = setUpIntent();

                AuthRepository authRepo = new AuthRepository();
                authRepo.loginWithGoogle(credential.getIdToken(), new AuthRepository.AuthResultCallback() {
                    @Override
                    public void onSuccess(LoginResponse user) {
                        // 1. Save token and userId to SharedPrefs
                        SharedPrefs prefs = new SharedPrefs(a.getApplicationContext());
                        prefs.setString("accessToken", user.getToken());
                        prefs.setString("userId", user.getId());
                        prefs.apply();

                        // 2. Pass LoggedInUser data back to NavigationDrawerActivity
                        Uri profilePic = user.getProfilePic() != null ? user.getProfilePic() : credential.getProfilePictureUri();
                        LoggedInUser info = new LoggedInUser(user.getToken(), user.getEmail(), user.getName(),
                                user.getId(), user.getSelf(), profilePic);

                        if (a instanceof AuthenticationInterface authInterface) {
                            authInterface.shareData(info, intent);
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        if (a instanceof AuthenticationInterface authInterface) {
                            Log.e("loginError", errorMessage);
                            authInterface.showNotLoggedInMsg();
                        }
                    }
                });
            }

            @Override
            public void onError(Exception error) {
                error.printStackTrace();
            }
        };
        signIn.signIn(a, listener);
    }

    @NonNull
    public Intent setUpIntent() {
        Intent intent = new Intent();
        intent.setClassName("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui", "NavigationDrawerActivity");
        return intent;
    }
}
