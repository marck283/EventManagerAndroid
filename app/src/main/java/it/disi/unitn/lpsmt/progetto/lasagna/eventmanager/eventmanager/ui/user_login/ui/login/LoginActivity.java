package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.ActivityLoginBinding;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.gSignIn.GSignIn;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses.DBSignInThread;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses.DBThread;

public class LoginActivity extends AppCompatActivity {

    private static final int REQ_SIGN_IN = 2;
    private GSignIn signIn;
    private DBThread t2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        signIn = new GSignIn(this);
        t2 = new DBThread(this);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        //account = GoogleSignIn.getLastSignedInAccount(this);
        if(signIn.getAccount() != null) {
            Intent intent = setUpIntent("google", null);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            findViewById(R.id.sign_in_button).setOnClickListener(v -> {
                //Sign in the user when the button is clicked
                if(v.getId() == R.id.sign_in_button) {
                    signIn();
                }
            });

            CallbackManager callbackManager = CallbackManager.Factory.create();
            LoginButton loginButton = findViewById(R.id.login_button);
            LoginManager loginManager = LoginManager.getInstance();

            loginButton.setOnClickListener(c -> loginManager.logInWithReadPermissions(this,
                    List.of("public_profile", "email")));
            loginButton.registerCallback(callbackManager, new FacebookCallback<>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    AccessToken accessToken = loginResult.getAccessToken();
                    Intent i = setUpIntent("facebook", accessToken);
                    accessToken.getUserId();

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,picture,email");
                    GraphRequest req1 = new GraphRequest(accessToken, accessToken.getUserId(),
                            null, HttpMethod.GET, graphResponse -> {
                        try {
                            JSONObject jsonObject = graphResponse.getJSONObject();
                            if(jsonObject != null) {
                                Profile p = new Profile(jsonObject);
                                i.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fAccount", p);
                                i.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fEmail", jsonObject.getString("email"));
                                setResult(Activity.RESULT_OK, i);
                                finish();
                            } else {
                                Log.i("nullResult", "Risposta null");
                                setResult(Activity.RESULT_CANCELED);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            setResult(Activity.RESULT_OK, i);
                            finish();
                        }
                            });
                    req1.setParameters(parameters);
                    req1.executeAsync();
                }

                @Override
                public void onCancel() {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }

                @Override
                public void onError(@NonNull FacebookException e) {
                    AlertDialog dialog = new AlertDialog.Builder(getApplicationContext()).create();
                    dialog.setTitle(R.string.facebook_login_error_title);
                    dialog.setMessage(getString(R.string.facebook_login_error));
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> {
                        dialog1.dismiss();
                        finish();
                    });
                    dialog.show();
                }
            });
        }
    }

    @NonNull
    private Intent setUpIntent(@NonNull String which, @Nullable AccessToken accessToken) {
        Intent intent = new Intent();
        intent.setClassName("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui", "NavigationDrawerActivity");
        if(which.equals("google")) {
            intent.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.gAccount", signIn.getAccount());
        } else {
            if(accessToken != null) {
                Log.i("profileNull", accessToken.getToken());
            }
            intent.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fAccessToken", accessToken);
        }
        return intent;
    }

    private void signIn() {
        signIn.signIn(this, REQ_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == REQ_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        try {
            signIn.getAccountFromCompletedTask(completedTask);

            // Signed in successfully, update the database and return to caller with the results
            t2 = new DBSignInThread(this, signIn);
            t2.start();

            Intent intent = setUpIntent("google", null);
            setResult(Activity.RESULT_OK, intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("fail", "signInResult:failed code=" + e.getStatusCode());
            Log.w("fail", e.getMessage());
            Log.i("info1", String.valueOf(signIn.getAccount() != null));
            //Not signed in, so return to caller with null results
            setResult(Activity.RESULT_CANCELED);
        } finally {
            t2.close();
            finish();
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }
}