package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.data.googleSignInClient.GSignInClient;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.data.model.LoggedInUser;

public class LoginViewModel extends ViewModel {
    private LoggedInUser liuser; //Questo servirà quando dovrò eseguire il parsing dei dati che saranno restituiti dal server
    private GSignInClient gsi;
    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private static final int REQ_SIGN_IN = 3;

    public LoginViewModel() {
        liuser = null;
        gsi = null;
    }

    public void init(@NonNull Fragment f) {
        gsi = new GSignInClient(f.requireActivity());
        gsi.oneTapSignIn(f.requireActivity(), REQ_ONE_TAP, REQ_SIGN_IN);
    }

    public void registerLoginClickListener(@NonNull Fragment f) {
        f.requireActivity().findViewById(R.id.sign_in_button).setOnClickListener(v -> {
            if(v.getId() == R.id.sign_in_button) {
                gsi.signIn(f.requireActivity(), REQ_SIGN_IN);
            }
        });
    }
}