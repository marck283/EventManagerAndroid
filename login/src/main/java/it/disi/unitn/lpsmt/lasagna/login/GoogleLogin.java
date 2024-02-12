package it.disi.unitn.lpsmt.lasagna.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import it.disi.unitn.lpsmt.lasagna.csrfToken.CsrfToken;
import it.disi.unitn.lpsmt.lasagna.gSignIn.GSignIn;
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
        signIn.signIn(a, 2);
    }

    public GSignIn getSignIn() {
        return signIn;
    }

    @NonNull
    public Intent setUpIntent(@NonNull String which, @Nullable AccessToken accessToken) {
        Intent intent = new Intent();
        intent.setClassName("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui", "NavigationDrawerActivity");
        if(which.equals("google")) {
            intent.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.gAccount",
                    getSignIn().getAccount());
        } else {
            if(accessToken != null) {
                Log.i("profileNull", accessToken.getToken());
            }
            intent.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fAccessToken", accessToken);
        }
        return intent;
    }

    public void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        try {
            signIn.getAccountFromCompletedTask(completedTask);

            // Signed in successfully, update the database and return to caller with the results

            Intent intent = setUpIntent("google", null);
            CsrfToken token = new CsrfToken(a, signIn.getAccount().getIdToken(), null, "google", intent);
            signIn.setAccount(completedTask.getResult());
            token.start();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("fail", "signInResult:failed code=" + e.getStatusCode());
            if(e.getMessage() != null) {
                Log.w("fail", e.getMessage());
            }
            Log.i("info1", String.valueOf(signIn.getAccount() != null));
            //Not signed in, so return to caller with null results
            a.setResult(Activity.RESULT_CANCELED);
            a.finish();
        }
    }
}
