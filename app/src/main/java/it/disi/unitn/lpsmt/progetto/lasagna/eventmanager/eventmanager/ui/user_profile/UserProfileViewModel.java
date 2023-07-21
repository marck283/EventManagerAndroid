package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_profile;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses.DBUser;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.NetworkCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.sharedpreferences.SharedPrefs;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.userinfo.OnlineUserInfo;

public class UserProfileViewModel extends ViewModel {

    public void getUserInfo(@NonNull Fragment f, @NonNull String accessToken, @NonNull ConstraintLayout l) {
        //Ottiene le informazioni personali dell'utente e le memorizza in un database, oppure, se la connessione Internet non
        //è disponibile, cerca tali informazioni nel database stesso.
        Activity activity1 = f.getActivity();
        if(activity1 != null && f.isAdded()) {
            NetworkCallback nc = new NetworkCallback(f.requireActivity());
            if(nc.isOnline(f.requireActivity())) {
                OnlineUserInfo onlineUserInfo = new OnlineUserInfo(accessToken, l, f);
                onlineUserInfo.start();
            } else {
                //Ottieni i dati dell'utente dal database, se disponibili
                SharedPrefs prefs = new SharedPrefs("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.AccTok",
                        f.requireActivity());
                DBUser dbUser = new DBUser(prefs.getString("userId"), "getAll", l, f);
                dbUser.start();
            }
        }
    }

}