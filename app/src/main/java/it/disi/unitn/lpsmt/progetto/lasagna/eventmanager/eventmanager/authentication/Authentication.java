package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InvalidObjectException;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.NavigationDrawerActivity;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.networkOps.ServerOperation;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.sharedpreferences.SharedPrefs;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.data.model.LoggedInUser;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Callback;
import okhttp3.Call;

public class Authentication extends ServerOperation {
    /*private final ServerAuthentication authentication;

    public Authentication() {
        Retrofit retro = new Retrofit.Builder()
                .baseUrl("https://https://eventmanager-uo29.onrender.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        authentication = retro.create(ServerAuthentication.class);
    }*/

    private String csrfToken, googleJwt, which;

    private AccessToken fbJwt;

    private Activity a;

    private Intent intent;

    public void setActivity(@NotNull Activity a) throws InvalidObjectException {
        if(a == null) {
            throw new InvalidObjectException("The Activity cannot be set to a null value.");
        }
        this.a = a;
    }

    public void setCsrfToken(@NotNull String token) throws InvalidObjectException {
        if(token == null || token.equals("")) {
            throw new InvalidObjectException("The CSRF token cannot be null or an empty string.");
        }

        csrfToken = token;
    }

    public void setUserToken(@Nullable String token, @Nullable AccessToken fbToken) throws InvalidObjectException {
        if(which == null || (token == null && fbToken == null) || (token != null && token.equals("")) ||
                (fbToken != null && fbToken.getToken().equals(""))) {
            throw new InvalidObjectException("The signer of the JWT token was not already specified or " +
                    "the JWT token was set to null or to an empty string.");
        }

        if(which.equals("google")) {
            googleJwt = token;
        } else {
            if(which.equals("facebook")) {
                fbJwt = fbToken;
            }
        }
    }

    public void setIntent(@NotNull Intent i) throws InvalidObjectException {
        if(i == null) {
            throw new InvalidObjectException("The Intent argument cannot be null.");
        }

        intent = i;
    }

    public void setWhich(@NotNull String w) throws InvalidObjectException {
        if(w == null || w.equals("")) {
            throw new InvalidObjectException("The signer of the JWT token was not specified.");
        }

        which = w;
    }

    public void run() {
        try {
            if(csrfToken == null || which == null || which.equals("")) {
                throw new InvalidObjectException("Either the CSRF is null or the signer of the JWT token " +
                        "was not specified.");
            }
            if(a == null) {
                throw new InvalidObjectException("The Activity object was not set.");
            }
            if(intent == null) {
                throw new InvalidObjectException("The Intent argument cannot be null.");
            }

            AuthObject authObj;
            if(which.equals("google")) {
                if(googleJwt == null || googleJwt.equals("")) {
                    Log.i("gJwtNull", "Il token JWT di Google non può essere una stringa vuota");
                    return;
                }
                //auth = authentication.authentication(new AuthObject(csrfToken, googleJwt, null));
                authObj = new AuthObject(csrfToken, googleJwt, null);
            } else {
                if(fbJwt == null || fbJwt.equals("")) {
                    Log.i("fbJwtNull", "L'Access Token di Facebook non può essere una stringa vuota");
                    return;
                }

                Log.i("fbJwt", fbJwt.getToken());
                authObj = new AuthObject(csrfToken, fbJwt.getToken(), fbJwt.getUserId());
                //auth = authentication.fbAuth(new AuthObject(csrfToken, fbJwt.getToken(), fbJwt.getUserId()));
            }

            FormBody.Builder fbodyBuilder = new FormBody.Builder()
                    .add("csrfToken", csrfToken)
                    .add("googleJwt", authObj.getJwt());
            if(authObj.getUserId() != null && !authObj.getUserId().equals("")) {
                fbodyBuilder.add("userId", authObj.getUserId());
            }
            Request request = getNetworkRequest().getPostRequest(fbodyBuilder.build(), null,
                    getBaseUrl() + "/api/v2/authentications");
            getNetworkRequest().enqueue(request, new Callback() {

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
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    Log.i("response", String.valueOf(response));

                    SharedPrefs prefs = new SharedPrefs(
                            "it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.AccTok", a);

                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            LoggedInUser info = new LoggedInUser();
                            info = info.parseJSON(parseBody(response.body()));

                            prefs.setString("accessToken", info.getToken());
                            prefs.setString("userId", info.getId());

                            final String email = info.getEmail(), profilePic = info.getProfilePic(),
                                    name = info.getName();
                            if(a instanceof NavigationDrawerActivity) {
                                a.runOnUiThread(() ->
                                        ((NavigationDrawerActivity)a)
                                                .updateUI("login", email, name, profilePic, true));
                                ((NavigationDrawerActivity)a).getViewModel().setToken(info.getToken());
                            } else {
                                if(a instanceof LoginActivity) {
                                    if(intent != null && which.equals("google")) {
                                        intent.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fEmail",
                                                email);
                                        intent.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fPicture",
                                                profilePic);
                                    }
                                    a.setResult(Activity.RESULT_OK, intent);
                                    a.finish();
                                }
                            }
                        } catch(IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        Log.i("null1", "Unsuccessful or null response");

                        prefs.setString("accessToken", "");
                        prefs.setString("userId", "");

                        if (response.code() == 401) {
                            a.runOnUiThread(() -> {
                                AlertDialog dialog = new AlertDialog.Builder(a).create();
                                dialog.setTitle(R.string.user_not_logged_in);
                                dialog.setMessage(a.getString(R.string.user_not_logged_in_message));
                                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                                dialog.show();
                            });
                        }
                        if(a instanceof NavigationDrawerActivity) {
                            a.runOnUiThread(() ->
                                    ((NavigationDrawerActivity)a).updateUI("logout", null,
                                            null, null, false));
                            ((NavigationDrawerActivity)a).getViewModel().setToken("");
                        } else {
                            if(a instanceof LoginActivity) {
                                a.setResult(Activity.RESULT_CANCELED, intent);
                                a.finish();
                            }
                        }
                    }
                    prefs.apply();
                }

                /**
                 * Invoked when a network exception occurred talking to the server or when an unexpected exception
                 * occurred creating the request or processing the response.
                 *
                 * @param call
                 * @param t
                 */
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException t) {
                    try {
                        throw t;
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch(InvalidObjectException ex) {
            ex.printStackTrace();
        }
        /*auth.enqueue(new Callback<>() {

            /**
             * Invoked for a received HTTP response.
             *
             * <p>Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call {@link Response#isSuccessful()} to determine if the response indicates success.
             *
             * @param call
             * @param response
             */
            /*@Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                Log.i("response", String.valueOf(response));

                SharedPrefs prefs = new SharedPrefs(
                        "it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.AccTok", a);

                if (response.isSuccessful() && response.body() != null) {
                    LoggedInUser info = new LoggedInUser();
                    info = info.parseJSON(response.body());

                    prefs.setString("accessToken", info.getToken());
                    prefs.setString("userId", info.getId());

                    final String email = info.getEmail(), profilePic = info.getProfilePic(), name = info.getName();
                    if(a instanceof NavigationDrawerActivity) {
                        a.runOnUiThread(() ->
                                ((NavigationDrawerActivity)a)
                                        .updateUI("login", email, name, profilePic, true));
                        ((NavigationDrawerActivity)a).getViewModel().setToken(info.getToken());
                    } else {
                        if(a instanceof LoginActivity) {
                            if(i != null && which.equals("google")) {
                                i.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fEmail",
                                        email);
                                i.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fPicture",
                                        profilePic);
                            }
                            a.setResult(Activity.RESULT_OK, i);
                            a.finish();
                        }
                    }
                } else {
                    Log.i("null1", "Unsuccessful or null response");

                    prefs.setString("accessToken", "");
                    prefs.setString("userId", "");

                    if (response.code() == 401) {
                        a.runOnUiThread(() -> {
                            AlertDialog dialog = new AlertDialog.Builder(a).create();
                            dialog.setTitle(R.string.user_not_logged_in);
                            dialog.setMessage(a.getString(R.string.user_not_logged_in_message));
                            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                            dialog.show();
                        });
                    }
                    if(a instanceof NavigationDrawerActivity) {
                        a.runOnUiThread(() ->
                                ((NavigationDrawerActivity)a).updateUI("logout", null,
                                        null, null, false));
                        ((NavigationDrawerActivity)a).getViewModel().setToken("");
                    } else {
                        if(a instanceof LoginActivity) {
                            a.setResult(Activity.RESULT_CANCELED, i);
                            a.finish();
                        }
                    }
                }
                prefs.apply();
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected exception
             * occurred creating the request or processing the response.
             *
             * @param call
             * @param t
             */
            /*@Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                try {
                    throw t;
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });*/
    }
}
