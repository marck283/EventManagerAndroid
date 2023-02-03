package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication.Authentication;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.csrfToken.CsrfToken;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.gSignIn.GSignIn;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses.DBSignInThread;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses.DBThread;

public class GoogleLogin {
    private final GSignIn signIn;

    private final LoginActivity a;

    private DBThread t2;

    public GoogleLogin(@NonNull LoginActivity a) {
        this.a = a;
        signIn = new GSignIn(a);
        SignInButton signInButton = a.findViewById(R.id.sign_in_button);
        if(!signInButton.hasOnClickListeners()) {
            signInButton.setOnClickListener(v -> {
                //Sign in the user when the button is clicked
                if(v.getId() == R.id.sign_in_button) {
                    signIn();
                }
            });
        }

        t2 = new DBThread(a);
    }

    private void signIn() {
        signIn.signIn(a, 2);
    }

    public GSignIn getSignIn() {
        return signIn;
    }

    public void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        try {
            signIn.getAccountFromCompletedTask(completedTask);

            // Signed in successfully, update the database and return to caller with the results
            t2 = new DBSignInThread(a, signIn);
            t2.start();

            Intent intent = a.setUpIntent("google", null);
            CsrfToken token = new CsrfToken();
            signIn.setAccount(completedTask.getResult());
            token.getCsrfToken(a, new Authentication(), signIn.getAccount().getIdToken(), null, "google", intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("fail", "signInResult:failed code=" + e.getStatusCode());
            Log.w("fail", e.getMessage());
            Log.i("info1", String.valueOf(signIn.getAccount() != null));
            //Not signed in, so return to caller with null results
            a.setResult(Activity.RESULT_CANCELED);
            a.finish();
        } finally {
            t2.close();
        }
    }
}
