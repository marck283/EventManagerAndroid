package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import it.disi.unitn.lpsmt.lasagna.login.AuthenticationInterface;
import it.disi.unitn.lpsmt.lasagna.login.GoogleLogin;
import it.disi.unitn.lpsmt.lasagna.login.model.LoggedInUser;
import it.disi.unitn.lpsmt.lasagna.network.NetworkCallbackInterface;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements AuthenticationInterface, NetworkCallbackInterface {

    private GoogleLogin gLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gLogin = new GoogleLogin(this, R.id.sign_in_button, R.string.no_connection, R.string.no_connection_message,
                R.string.server_client_id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void shareData(@NonNull LoggedInUser data, @Nullable Intent intent) {
        if(intent == null) {
            Log.i("login", "Intent null");
            setResult(Activity.RESULT_CANCELED);
        } else {
            intent.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fToken",
                    data.getToken());
            intent.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fEmail",
                    data.getEmail());
            intent.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fPicture",
                    data.getProfilePic());
            intent.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fName",
                    data.getName());
            intent.putExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fUserId",
                    data.getId());
            setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }

    public void logout(@Nullable Intent intent) {
        Log.i("login", "logged out");
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    public void showNotLoggedInMsg() {
        if (isFinishing() || isDestroyed()) {
            return;
        }
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(R.string.user_not_logged_in);
        dialog.setMessage(getString(R.string.user_not_logged_in_message));
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> {
            dialog1.dismiss();
            logout(null);
        });
        dialog.show();
    }

    @Override
    public void showOnLostMsg() {
        if (isFinishing() || isDestroyed()) {
            return;
        }
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle(R.string.no_connection);
        alert.setMessage(getString(R.string.no_connection_message_short));
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
        alert.show();
    }

    @Override
    public void showOnUnavailableMsg() {
        if (isFinishing() || isDestroyed()) {
            return;
        }
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle(R.string.no_connection);
        alert.setMessage(getString(R.string.no_connection_message_short));
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
        alert.show();
    }
}