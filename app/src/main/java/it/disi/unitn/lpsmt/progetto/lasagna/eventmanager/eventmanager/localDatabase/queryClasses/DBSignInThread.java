package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.gSignIn.GSignIn;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.AppDatabase;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.DAOs.UserDAO;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.entities.User;

public class DBSignInThread extends DBThread {
    private UserDAO user;
    private GSignIn signIn;

    public DBSignInThread(@NonNull Activity a, GSignIn signIn) {
        super(a);
        user = db.getUserDAO();
        this.signIn = signIn;
    }

    @Override
    public void run() {
        GoogleSignInAccount account = signIn.getAccount();
        if(user.getUserEmail(account.getEmail()) != null) {
            //Se esiste l'utente con l'email cercata
            //aggiorno gServerAuthCode e token Google
            //user.updateUserServerAuthCode(account.getServerAuthCode(), account.getEmail());
        } else {
            //Se l'utente cercato non esiste, aggiungilo al database (ancora da aggiungere: collegamento a People API)
            User u = new User();
            u.setEmail(account.getEmail());
            u.setNome(account.getGivenName());
            //u.setGServerAuthCode(account.getServerAuthCode());
            u.setGToken(account.getIdToken());
            u.setEventiCreati(new ArrayList<>());
            u.setEventiIscritto(new ArrayList<>());
            u.setNumEvOrg(0);
            u.setValutazioneMedia(0.0);
            user.insert(u);
            db.close(); //Chiude la connessione al database
        }
    }
}
