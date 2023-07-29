package it.disi.unitn.lpsmt.lasagna.login;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import it.disi.unitn.lpsmt.lasagna.sharedprefs.sharedpreferences.SharedPrefs;
import it.disi.unitn.lpsmt.lasagna.login.model.LoggedInUser;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AuthenticationCallback implements Callback {
    private final Authentication auth;

    private final Activity a;

    private final Intent intent;

    public AuthenticationCallback(@NotNull Authentication auth, @NotNull Activity a, @NotNull Intent i) {
        this.auth = auth;
        this.a = a;
        intent = i;
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException t) {
        try {
            throw t;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        SharedPrefs prefs = new SharedPrefs(
                "it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.AccTok", a);

        if (response.isSuccessful()) {
            try {
                //notify();
                LoggedInUser info = new LoggedInUser();
                info = info.parseJSON(auth.parseBody(response.body()));

                prefs.setString("accessToken", info.getToken());
                prefs.setString("userId", info.getId());

                ((AuthenticationInterface)a).shareData(info, intent);
            } catch(IOException ex) {
                ex.printStackTrace();
            }
            response.body().close();
        } else {
            Log.i("null1", "Unsuccessful or null response");

            prefs.setString("accessToken", "");
            prefs.setString("userId", "");

            if (response.code() == 401) {
                ((AuthenticationInterface)a).showNotLoggedInMsg();
            }
            ((AuthenticationInterface)a).logout(intent);
        }
        prefs.apply();
    }
}
