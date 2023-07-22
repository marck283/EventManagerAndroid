package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.facebook.AccessToken;

import org.jetbrains.annotations.NotNull;

import java.io.InvalidObjectException;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.networkOps.ServerOperation;
import okhttp3.FormBody;
import okhttp3.Request;

public class Authentication extends ServerOperation {

    private String csrfToken, googleJwt, which;

    private AccessToken fbJwt;

    private Activity a;

    private Intent intent;

    public void setActivity(@NotNull Activity a) {
        this.a = a;
    }

    public void setCsrfToken(@NotNull String token) throws InvalidObjectException {
        if(token.equals("")) {
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

    public void setIntent(@NotNull Intent i) {
        intent = i;
    }

    public void setWhich(@NotNull String w) throws InvalidObjectException {
        if(w.equals("")) {
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
            FormBody.Builder fbodyBuilder;
            Request request;
            if(which.equals("google")) {
                if(googleJwt == null || googleJwt.equals("")) {
                    Log.i("gJwtNull", "Il token JWT di Google non può essere una stringa vuota");
                    return;
                }
                authObj = new AuthObject(csrfToken, googleJwt, null);
                fbodyBuilder = new FormBody.Builder()
                        .add("csrfToken", csrfToken)
                        .add("googleJwt", authObj.getJwt());
                if(authObj.getUserId() != null && !authObj.getUserId().equals("")) {
                    fbodyBuilder.add("userId", authObj.getUserId());
                }
                request = getNetworkRequest().getPostRequest(fbodyBuilder.build(), null,
                        getBaseUrl() + "/api/v2/authentications");
            } else {
                if(fbJwt == null || fbJwt.getToken().equals("")) {
                    Log.i("fbJwtNull", "L'Access Token di Facebook non può essere una stringa vuota");
                    return;
                }

                Log.i("fbJwt", fbJwt.getToken());
                authObj = new AuthObject(csrfToken, fbJwt.getToken(), fbJwt.getUserId());
                fbodyBuilder = new FormBody.Builder()
                        .add("csrfToken", csrfToken)
                        .add("googleJwt", authObj.getJwt());
                if(authObj.getUserId() != null && !authObj.getUserId().equals("")) {
                    fbodyBuilder.add("userId", authObj.getUserId());
                }
                request = getNetworkRequest().getPostRequest(fbodyBuilder.build(), null,
                        getBaseUrl() + "/api/v2/authentications/facebookLogin");
            }

            getNetworkRequest().enqueue(request, new AuthenticationCallback(this, a, intent));
        } catch(InvalidObjectException ex) {
            ex.printStackTrace();
        }
    }
}
