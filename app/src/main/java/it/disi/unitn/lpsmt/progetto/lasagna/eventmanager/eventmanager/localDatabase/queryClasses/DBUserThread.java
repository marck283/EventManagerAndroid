package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses;

import android.app.Activity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication.Authentication;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.csrfToken.CsrfToken;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.DAOs.UserDAO;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.serverTokenExchange.TokenExchange;

public class DBUserThread extends DBThread {
    private UserDAO user;
    private GoogleSignInAccount account;
    private Activity a;

    private void threadWait() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        String email = account.getEmail();
        String authCode = user.getGToken(email);

        if(authCode == null) {
            TokenExchange exchange = new TokenExchange();
            exchange.getAccessToken(user.getServerAuthCode(email), a, email);
            threadWait();
            authCode = user.getGToken(email); //C'è un problema di sincronizzazione con questa istruzione. Perché non riesco mai a prendere il token Google?
        }

        //NOTA: da qui in poi il codice cerca di ottenere una nuova lista di eventi dal server e di aggiornare l'UI
        // senza, però, riuscirci.

        CsrfToken token1 = new CsrfToken();

        //Ottiene il token CSRF necessario per l'autenticazione e autentica l'utente al server.
        synchronized(this) {
            token1.getCsrfToken(a, new Authentication(), authCode);
            threadWait();
        }
        db.close();
    }
}
