package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.facebook.AccessToken;
import com.google.gson.JsonObject;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.NavigationDrawerActivity;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication.accountIntegration.AccountIntegration;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.gSignIn.GSignIn;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses.DBUser;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.data.model.LoggedInUser;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Authentication {
    private final ServerAuthentication authentication;

    public Authentication() {
        Retrofit retro = new Retrofit.Builder()
                .baseUrl("https://eventmanagerzlf.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        authentication = retro.create(ServerAuthentication.class);
    }

    public void login(@NonNull Activity a, @NonNull String csrfToken, @Nullable String googleJwt,
                      @Nullable AccessToken fbJwt, @NonNull String which, @Nullable Intent i) {
        Call<JsonObject> auth;
        if(which.equals("google")) {
            if(googleJwt == null || googleJwt.equals("")) {
                Log.i("gJwtNull", "Il token JWT di Google non può essere null o una stringa vuota");
                return;
            }
            auth = authentication.authentication(new AuthObject(csrfToken, googleJwt, null));
        } else {
            if(fbJwt == null || fbJwt.getToken().equals("")) {
                Log.i("fbJwtNull", "L'Access Token di Facebook non può essere null o una stringa vuota");
                return;
            }

            Log.i("fbJwt", fbJwt.getToken());
            auth = authentication.fbAuth(new AuthObject(csrfToken, fbJwt.getToken(), fbJwt.getUserId()));
        }
        auth.enqueue(new Callback<>() {

            /**
             * Invoked for a received HTTP response.
             *
             * <p>Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call {@link Response#isSuccessful()} to determine if the response indicates success.
             *
             * @param call
             * @param response
             */
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                Log.i("response", String.valueOf(response));
                if (response.isSuccessful() && response.body() != null) {
                    LoggedInUser info = new LoggedInUser();
                    info = info.parseJSON(response.body());
                    new DBUser(a, info.getEmail(), "updateProfilePic", info).start();

                    final String email = info.getEmail(), profilePic = info.getProfilePic();
                    if(a instanceof NavigationDrawerActivity) {
                        a.runOnUiThread(() -> ((NavigationDrawerActivity)a).updateUI("login", email, profilePic));
                        ((NavigationDrawerActivity)a).getViewModel().setToken(info.getToken());
                    } else {
                        if(a instanceof LoginActivity) {
                            a.setResult(Activity.RESULT_OK, i);
                            a.finish();
                        }
                    }
                    SharedPreferences prefs = a.getSharedPreferences("AccTok", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("accessToken", info.getToken());
                    editor.apply();
                } else {
                    Log.i("null1", "Unsuccessful or null response");
                    if(response.code() == 409) {
                        AlertDialog dialog = new AlertDialog.Builder(a).create();
                        dialog.setTitle(R.string.email_conflict_facebook);
                        dialog.setMessage(a.getString(R.string.connect_facebook_account));
                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which1) -> {
                            AccountIntegration integration = new AccountIntegration();
                            if(which.equals("facebook")) {
                                integration.facebookIntegrate(fbJwt.getUserId(), fbJwt.getToken());
                            } else {
                                if(googleJwt != null) {
                                    integration.googleIntegrate(googleJwt);
                                } else {
                                    Log.i("noJwt", "Utente non autenticato per mancanza dei codici di accesso");
                                }
                            }
                        });
                        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", (dialog1, which) -> dialog1.dismiss());
                        dialog.show();
                    }
                    if (response.code() == 401 && which.equals("google")) {
                        GSignIn signIn = new GSignIn(a);
                        signIn.signIn(a, 2);
                    }
                }
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected exception
             * occurred creating the request or processing the response.
             *
             * @param call
             * @param t
             */
            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                try {
                    throw t;
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
