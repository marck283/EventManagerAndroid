package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.data.model.LoggedInUser;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private LoggedInUser liuser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this.requireContext());
        if(account != null) {
            liuser = new LoggedInUser(account.getId(), account.getDisplayName(), account.getEmail());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.init(this);
        loginViewModel.registerLoginClickListener(this);
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
}