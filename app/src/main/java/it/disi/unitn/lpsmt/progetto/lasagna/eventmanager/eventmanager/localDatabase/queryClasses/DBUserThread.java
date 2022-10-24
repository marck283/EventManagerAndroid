package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.room.Room;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.NavigationDrawerActivity;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.csrfToken.ApiCSRFClass;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.csrfToken.CsrfToken;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.gSignIn.GSignIn;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.AppDatabase;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.DAOs.UserDAO;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.serverTokenExchange.AccessToken;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.serverTokenExchange.JsonParser;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.serverTokenExchange.TokenExchange;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_list.EventListFragment;

public class DBUserThread extends DBThread {
    private UserDAO user;
    private GoogleSignInAccount account;
    private NavigationDrawerActivity a;

    public DBUserThread(Activity a, GoogleSignInAccount account) {
        super(a);
        user = db.getUserDAO();
        this.account = account;
        this.a = (NavigationDrawerActivity)a;
    }

    @Override
    public void run() {
        String authCode = user.getServerAuthCode(account.getEmail());

        TokenExchange ex = new TokenExchange();
        JsonParser j1 = new JsonParser();
        ex.getToken(authCode, j1);

        AccessToken accessToken = j1.getToken();
        //NOTA: da qui in poi il codice cerca di ottenere una nuova lista di eventi dal server e di aggiornare l'UI
        // senza, però, riuscirci.
        //Perché acquisire la lista di eventi solo quando si modifica il token di accesso?
        Log.i("token", accessToken.getValue());
        EventListFragment evl = (EventListFragment) a.getSupportFragmentManager().findFragmentById(R.id.nav_event_list);
        if (evl != null) {
            evl.getData(accessToken.getValue());
        } else {
            Log.i("null", "no fragment");
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(a.getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("gToken", accessToken.getValue());
        editor.apply();

        ApiCSRFClass token = new ApiCSRFClass();
        CsrfToken token1 = new CsrfToken();

        //Ottiene il token CSRF necessario per l'autenticazione e autentica l'utente al server.
        token1.getCsrfToken(token, prefs);
    }
}
