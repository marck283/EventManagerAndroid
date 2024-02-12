package it.disi.unitn.lpsmt.lasagna.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.List;

import it.disi.unitn.lpsmt.lasagna.csrfToken.CsrfToken;
import it.disi.unitn.lpsmt.lasagna.network.NetworkCallback;

public class FacebookLogin {
    private final CallbackManager callbackManager;
    private final LoginButton loginButton;
    private final LoginManager loginManager;

    private final Activity a;

    private final int fbloginerrortitle, fbloginerrormsg;

    public FacebookLogin(@NonNull Activity a, @IdRes int loginbid, @StringRes int noconn,
                         @StringRes int noconnmsgshort, @StringRes int fberrortitle, @StringRes int fberrormsg) {
        this.a = a;
        fbloginerrortitle = fberrortitle;
        fbloginerrormsg = fberrormsg;
        callbackManager = CallbackManager.Factory.create();
        loginButton = a.findViewById(loginbid);
        loginManager = LoginManager.getInstance();

        LoginBehavior behavior;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            behavior = LoginBehavior.KATANA_ONLY;
        } else {
            behavior = LoginBehavior.WEB_ONLY;
        }
        loginManager.setLoginBehavior(behavior);

        if(!loginButton.hasOnClickListeners()) {
            loginButton.setOnClickListener(c -> {
                NetworkCallback callback = new NetworkCallback(a);
                if(callback.isOnline(a)) {
                    loginManager.logInWithReadPermissions(a,
                            List.of("public_profile", "email"));
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(a).create();
                    dialog.setTitle(noconn);
                    dialog.setMessage(a.getString(noconnmsgshort));
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                    dialog.show();
                }
            });
        }
    }

    @NonNull
    public Intent setUpIntent(@Nullable AccessToken accessToken) {
        Intent intent = new Intent();
        intent.setClassName("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui", "NavigationDrawerActivity");
        if(accessToken != null) {
            Log.i("profileNull", accessToken.getToken());
        }
        intent.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fAccessToken", accessToken);
        return intent;
    }

    public void registerCallback() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Intent i = setUpIntent(accessToken);

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,picture,email");
                GraphRequest req1 = new GraphRequest(accessToken, accessToken.getUserId(),
                        parameters, HttpMethod.GET, graphResponse -> {
                    JSONObject jsonObject = graphResponse.getJSONObject();
                    if(jsonObject != null) {
                        Profile p = new Profile(jsonObject);
                        i.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fAccount", p);

                        CsrfToken token = new CsrfToken(a, null, accessToken, "facebook", i);
                        token.start();
                    } else {
                        Log.i("nullResult", "Risposta null");
                        a.setResult(Activity.RESULT_CANCELED);
                        a.finish();
                    }
                });
                req1.setParameters(parameters);
                req1.executeAsync();
            }

            @Override
            public void onCancel() {
                a.setResult(Activity.RESULT_CANCELED);
                a.finish();
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                AlertDialog dialog = new AlertDialog.Builder(a.getApplicationContext()).create();
                dialog.setTitle(fbloginerrortitle);
                dialog.setMessage(a.getString(fbloginerrormsg));
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> {
                    dialog1.dismiss();
                    a.setResult(Activity.RESULT_CANCELED);
                    a.finish();
                });
                dialog.show();
            }
        });
    }
}
