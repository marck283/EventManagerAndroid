package it.disi.unitn.lpsmt.lasagna.gSignIn;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class GSignIn {
    private GoogleSignInAccount account;
    private final GoogleSignInClient gsi;

    public GSignIn(@NonNull Activity a, @StringRes int clientID) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken(a.getString(clientID))
                .build();

        gsi = GoogleSignIn.getClient(a, gso);
        account = GoogleSignIn.getLastSignedInAccount(a);
    }

    public GoogleSignInAccount getAccount() {
        return account;
    }

    public void signIn(@NonNull Activity a, int REQ_SIGN_IN) {
        Intent signInIntent = gsi.getSignInIntent();
        a.startActivityForResult(signInIntent, REQ_SIGN_IN);
    }

    public void getAccountFromCompletedTask(@NonNull Task<GoogleSignInAccount> t) throws ApiException {
        account = t.getResult(ApiException.class);
    }

    public void setAccount(GoogleSignInAccount a) {
        account = a;
    }

    public Task<Void> signOut() {
        return gsi.signOut();
    }
}
