package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.ActivityLoginBinding;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.gSignIn.GSignIn;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private static final int REQ_SIGN_IN = 2;
    private GSignIn signIn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        signIn = new GSignIn(this);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        //account = GoogleSignIn.getLastSignedInAccount(this);
        if(signIn.getAccount() != null) {
            Intent intent = setUpIntent();
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            findViewById(R.id.sign_in_button).setOnClickListener(v -> {
                //Sign in the user when the button is clicked
                if(v.getId() == R.id.sign_in_button) {
                    signIn();
                }
            });
        }
    }

    @NonNull
    private Intent setUpIntent() {
        Intent intent = new Intent();
        intent.setClassName("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui", "NavigationDrawerActivity");
        intent.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.gAccount", signIn.getAccount());
        return intent;
    }

    private void signIn() {
        signIn.signIn(this, REQ_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == REQ_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            finish();
        }
    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        try {
            signIn.getAccountFromCompletedTask(completedTask);

            // Signed in successfully, return to caller with the results
            Intent intent = setUpIntent();
            setResult(Activity.RESULT_OK, intent);
            finish();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("fail", "signInResult:failed code=" + e.getStatusCode());
            Log.w("fail", e.getMessage());
            Log.i("info1", String.valueOf(signIn.getAccount() != null));
            //Not signed in, so return to caller with null results
            setResult(Activity.RESULT_CANCELED);
        }
    }
}