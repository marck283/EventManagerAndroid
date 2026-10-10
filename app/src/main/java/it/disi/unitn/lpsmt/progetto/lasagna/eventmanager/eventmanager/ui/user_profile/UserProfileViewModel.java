package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_profile;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import it.disi.unitn.lasagna.eventmanager.userinfo.UserProfileCallback;
import it.disi.unitn.lpsmt.lasagna.localdatabase.queryClasses.DBUser;
import it.disi.unitn.lpsmt.lasagna.network.NetworkCallback;
import it.disi.unitn.lpsmt.lasagna.network.client.RetrofitClient;
import it.disi.unitn.lpsmt.lasagna.network.repository.UserRepository;
import it.disi.unitn.lpsmt.lasagna.sharedprefs.sharedpreferences.SharedPrefs;

public class UserProfileViewModel extends ViewModel {

    public void getUserInfo(@NonNull Fragment f, @NonNull String accessToken, @NonNull ConstraintLayout l) {
        //Ottiene le informazioni personali dell'utente e le memorizza in un database, oppure, se la connessione Internet non
        //è disponibile, cerca tali informazioni nel database stesso.
        Activity activity1 = f.getActivity();
        if(activity1 != null && f.isAdded()) {
            NetworkCallback nc = new NetworkCallback(f.requireActivity());
            if (nc.isOnline(f.requireActivity())) {
                RetrofitClient.getInstance().setAccessToken(accessToken);

                UserRepository userRepo = new UserRepository();
                userRepo.getUserProfile(new UserRepository.UserProfileCallback() {
                    @Override
                    public void onSuccess(JsonObject userProfile) {
                        // Pass JSON directly to UserProfileCallback
                        UserProfileCallback callback = new UserProfileCallback(f, l);
                        callback.handleProfileSuccess(userProfile);
                    }

                    @Override
                    public void onError(int statusCode, String errorMessage) {
                        // Fallback to local DB on failure
                        loadFromLocalDb(f, l);
                    }
                });
            } else {
                loadFromLocalDb(f, l);
            }
        }
    }

    private void loadFromLocalDb(@NonNull Fragment f, @NonNull ConstraintLayout l) {
        SharedPrefs prefs = new SharedPrefs(f.requireActivity().getApplicationContext());
        DBUser dbUser = new DBUser(prefs.getString("userId"), "getAll", l, f);
        dbUser.start();
    }

}