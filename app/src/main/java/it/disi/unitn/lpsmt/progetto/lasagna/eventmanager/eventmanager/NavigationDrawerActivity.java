package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.ActivityNavigationDrawerBinding;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.data.googleSignInClient.GSignInClient;

public class NavigationDrawerActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavigationDrawerBinding binding;
    private GoogleSignInAccount account;
    private View root;
    private GSignInClient gsi;
    private NavigationView navigationView;

    private SignInClient oneTapClient;
    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private static final int REQ_SIGN_IN = 3;
    private boolean showOneTapUI = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavigationDrawerBinding.inflate(getLayoutInflater());
        root = binding.getRoot();
        setContentView(root);

        setSupportActionBar(binding.appBarNavigationDrawer.toolbar);
        binding.appBarNavigationDrawer.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_event_list, R.id.nav_user_calendar, R.id.nav_user_settings,
                R.id.nav_user_profile, R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();

        //Reimplementare l'intero meccanismo di Google Sign In e aggiornamento dell'interfaccia utente
        //facendo in modo che, al termine del metodo "onActivityResult()" il controllo sia ritornato
        //all'Activity precedente (esclusa quella di login) e che il suo stato sia salvato. Per fare
        //ciò, dovrò salvare lo stato dell'Activity nel Bundle della stessa (ripassare il ciclo di
        //vita delle Activity prima di fare ciò).
        gsi = new GSignInClient(this);
        account = gsi.getAccount(this);

        NavHostFragment nhf = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_navigation_drawer);
        if(nhf != null) {
            NavController navController = nhf.getNavController();
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(account == null) {
            gsi.silentSignIn(this);
        }

        //Chiamo il metodo di aggiornamento dell'interfaccia qui perché l'Activity
        //ha già raggiunto lo stato di CREATED.
        updateUI(account); //Qui c'è un errore perché silentSignIn() contiene una callback
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_ONE_TAP: {
                handleOneTapSignInResult(data);
                break;
            }
            case REQ_SIGN_IN: {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
                break;
            }
        }
    }

    public void handleSignInResult(@NonNull Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);

            //Signed in successfully, authenticate to web server
            gsi.serverLogin(account);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("fail", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    public void updateUI(GoogleSignInAccount account) {
        if(account == null) {
            //account is null
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.navmenu_not_logged_in);
        } else {
            Log.i("account", account.getDisplayName());
        }
    }

    public void handleOneTapSignInResult(@Nullable Intent data) {
        try {
            SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
            String idToken = credential.getGoogleIdToken();
            if (idToken !=  null) {
                // Got an ID token from Google. Use it to authenticate
                // with Firebase.
                Log.d("idToken", "Got ID token.");
            }
        } catch (ApiException e) {
            Log.d("exception", e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation_drawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}