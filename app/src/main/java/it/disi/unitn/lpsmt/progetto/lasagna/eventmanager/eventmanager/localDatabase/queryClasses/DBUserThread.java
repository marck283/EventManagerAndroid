package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication.Authentication;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.csrfToken.CsrfToken;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.DAOs.UserDAO;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.serverTokenExchange.TokenExchange;

public class DBUserThread extends DBThread {
    private UserDAO user;
    private GoogleSignInAccount account;
    private Activity a;
    private boolean gotResult = false;

    private void threadWait(String authCode, @NonNull String email) {
        while(authCode == null || authCode.equals("")) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            authCode = user.getGToken(email);
        }
    }

    public DBUserThread(Activity a, GoogleSignInAccount account) {
        super(a);
        user = db.getUserDAO();
        this.account = account;
        this.a = a;
    }

    @Override
    public void run() {
        synchronized(this) {
            String email = account.getEmail();
            String authCode = account.getIdToken();

            CsrfToken csrf = new CsrfToken();
            csrf.getCsrfToken(a, new Authentication(), authCode);

            /*if(authCode == null || authCode.equals("")) {
                Log.i("nullOrEmpty", "authCode is null or empty");
            } else {
                TokenExchange exchange = new TokenExchange();
                exchange.getAccessToken(authCode, a, email);
            }*/
        }
    }

    public boolean result() {
        return gotResult;
    }
}
