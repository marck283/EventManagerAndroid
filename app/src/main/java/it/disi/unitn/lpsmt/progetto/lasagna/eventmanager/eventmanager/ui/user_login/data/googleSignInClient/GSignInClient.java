package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.data.googleSignInClient;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication.Authentication;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.csrfToken.ApiCSRFClass;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.csrfToken.CsrfToken;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.data.SignInResultContract;

public class GSignInClient extends ApiCSRFClass {
    private final GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;

    public GSignInClient(@NonNull View root) {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken("22819640695-40ie511a43vdbh8p82o5uhm6b62529rm.apps.googleusercontent.com")
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(root.getContext(), gso);
        account = GoogleSignIn.getLastSignedInAccount(root.getContext());
    }

    public void silentSignIn(Activity a) {
        mGoogleSignInClient.silentSignIn()
                .addOnCompleteListener(a,
                        this::handleSignInResult);
    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);

            //Ottieni il token CSRF
            CsrfToken token = new CsrfToken();
            token.getCsrfToken(this);

            //Chiama le API web
            Authentication auth = new Authentication();
            auth.login(getToken());
            //updateUI(account);
        } catch (ApiException e) {
            Log.w("error", "handleSignInResult:error", e);
            //updateUI(null);
        }
    }

    public ActivityResultLauncher<String> signIn(@NonNull Fragment a) {
        SignInResultContract contract = new SignInResultContract(mGoogleSignInClient, a.requireActivity());

        return (ActivityResultLauncher<String>) a.registerForActivityResult(contract, result -> {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(new Intent(result.toString()));
            handleSignInResult(task);
        });
    }

    public GoogleSignInAccount getAccount() {
        return account;
    }
}
