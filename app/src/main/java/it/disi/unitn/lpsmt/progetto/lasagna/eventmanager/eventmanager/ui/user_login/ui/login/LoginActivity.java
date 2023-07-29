package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

import it.disi.unitn.lpsmt.lasagna.login.AuthenticationInterface;
import it.disi.unitn.lpsmt.lasagna.login.model.LoggedInUser;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements AuthenticationInterface {

    private static final int REQ_SIGN_IN = 2;

    private GoogleLogin gLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gLogin = new GoogleLogin(this);

        FacebookLogin fbLogin = new FacebookLogin(this);
        fbLogin.registerCallback();
    }

    @NonNull
    public Intent setUpIntent(@NonNull String which, @Nullable AccessToken accessToken) {
        Intent intent = new Intent();
        intent.setClassName("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui", "NavigationDrawerActivity");
        if(which.equals("google")) {
            intent.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.gAccount",
                    gLogin.getSignIn().getAccount());
        } else {
            if(accessToken != null) {
                Log.i("profileNull", accessToken.getToken());
            }
            intent.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fAccessToken", accessToken);
        }
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == REQ_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            gLogin.handleSignInResult(task);
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void shareData(@NonNull LoggedInUser data, @Nullable Intent intent) {
        if(intent == null) {
            setResult(Activity.RESULT_CANCELED);
        } else {
            intent.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fEmail",
                    data.getEmail());
            intent.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fPicture",
                    data.getProfilePic());
            setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }

    public void logout(@Nullable Intent intent) {
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    public void showNotLoggedInMsg() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(R.string.user_not_logged_in);
        dialog.setMessage(getString(R.string.user_not_logged_in_message));
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
        dialog.show();
    }
}