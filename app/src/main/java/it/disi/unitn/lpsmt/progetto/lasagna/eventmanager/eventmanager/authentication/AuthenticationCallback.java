package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.NavigationDrawerActivity;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.sharedpreferences.SharedPrefs;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.data.model.LoggedInUser;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;
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
        Log.i("response", String.valueOf(response));

        SharedPrefs prefs = new SharedPrefs(
                "it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.AccTok", a);

        if (response.isSuccessful() && response.body() != null) {
            try {
                LoggedInUser info = new LoggedInUser();
                info = info.parseJSON(auth.parseBody(response.body()));

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
                        if(intent != null) {
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
}
