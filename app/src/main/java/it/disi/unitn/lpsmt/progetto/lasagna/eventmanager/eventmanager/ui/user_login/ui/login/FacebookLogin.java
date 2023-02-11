package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication.Authentication;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.csrfToken.CsrfToken;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.NetworkCallback;

public class FacebookLogin {
    private final CallbackManager callbackManager;
    private final LoginButton loginButton;
    private final LoginManager loginManager;

    private final LoginActivity a;

    public FacebookLogin(@NonNull LoginActivity a) {
        this.a = a;
        callbackManager = CallbackManager.Factory.create();
        loginButton = a.findViewById(R.id.login_button);
        loginManager = LoginManager.getInstance();

        if(!loginButton.hasOnClickListeners()) {
            loginButton.setOnClickListener(c -> {
                NetworkCallback callback = new NetworkCallback(a);
                if(callback.isOnline(a)) {
                    loginManager.logInWithReadPermissions(a,
                            List.of("public_profile", "email"));
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(a).create();
                    dialog.setTitle(R.string.no_connection);
                    dialog.setMessage(a.getString(R.string.no_connection_message_short));
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                    dialog.show();
                }
            });
        }
    }

    public void registerCallback() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Intent i = a.setUpIntent("facebook", accessToken);

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,picture,email");
                GraphRequest req1 = new GraphRequest(accessToken, accessToken.getUserId(),
                        parameters, HttpMethod.GET, graphResponse -> {
                    try {
                        JSONObject jsonObject = graphResponse.getJSONObject();
                        if(jsonObject != null) {
                            Profile p = new Profile(jsonObject);

                            // Riscrivere questa parte e metodo setupIntent() in modo da sincronizzare
                            // la modifica del token di accesso nelle SharedPreferences con la NavigationDrawerActivity...
                            i.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fAccount", p);
                            i.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fEmail",
                                    jsonObject.getString("email"));
                            i.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fPicture",
                                    jsonObject.getJSONObject("picture").getJSONObject("data").getString("url"));

                            CsrfToken token = new CsrfToken();
                            token.getCsrfToken(a, new Authentication(), null, accessToken, "facebook", i);

                            //Log.i("picture", jsonObject.getJSONArray("picture").getJSONObject(0).getJSONObject("data").getString("url"));
                            //a.setResult(Activity.RESULT_OK, i);
                        } else {
                            Log.i("nullResult", "Risposta null");
                            a.setResult(Activity.RESULT_CANCELED);
                            a.finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
                dialog.setTitle(R.string.facebook_login_error_title);
                dialog.setMessage(a.getString(R.string.facebook_login_error));
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
