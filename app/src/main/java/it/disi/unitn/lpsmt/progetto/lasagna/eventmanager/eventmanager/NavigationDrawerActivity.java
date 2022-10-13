package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.ActivityNavigationDrawerBinding;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;

public class NavigationDrawerActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private GoogleSignInAccount account;
    private NavigationView navView;
    private static final int REQ_SIGN_IN = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityNavigationDrawerBinding binding = ActivityNavigationDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNavigationDrawer.toolbar);
        binding.appBarNavigationDrawer.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_event_list, R.id.nav_user_calendar, R.id.nav_user_settings,
                R.id.nav_user_profile, R.id.nav_logout)
                .setOpenableLayout(binding.drawerLayout)
                .build();

        navView = binding.navView;
        account = GoogleSignIn.getLastSignedInAccount(this.getApplicationContext());
        updateUI(null);

        NavHostFragment nhf = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_navigation_drawer);
        if(nhf != null) {
            NavController navController = nhf.getNavController();
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(binding.navView, navController);
        }
    }

    private void updateUI(Intent data) {
        navView.getMenu().clear();
        if(account == null) {
            navView.inflateMenu(R.menu.navmenu_not_logged_in);
            Log.i("info", "account null");
        } else {
            //User is logged in, display authenticated UI.
            Log.i("profileName", data.getStringExtra("profileName"));
            Log.i("email", data.getStringExtra("email"));
            navView.inflateMenu(R.menu.activity_navigation_drawer_drawer);
            TextView username = (TextView) navView.getHeaderView(R.id.profile_name);

            //setText expects a resource name, but why does it interpret "profileName" and "email" as a null object reference?
            username.setText(getString(R.string.profileName, data.getStringExtra("profileName")));

            TextView email = (TextView) navView.getHeaderView(R.id.profile_email);
            email.setText(getString(R.string.email, data.getStringExtra("email")));
        }
    }

    /**
     * Metodo chiamato quando si cerca di accedere al sistema.
     * @param item Il MenuItem da cui far partire l'Activity di accesso.
     */
    public void startLogin(MenuItem item) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, REQ_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_SIGN_IN) {
            switch(resultCode) {
                case Activity.RESULT_OK: {
                    //Autenticato con successo a Google, ora autentica al server e
                    //mostra i dati del profilo richiesti
                    account = data.getParcelableExtra("gAccount");
                    updateUI(data);
                    break;
                }
                case Activity.RESULT_CANCELED: {
                    account = null;
                    updateUI(null);
                    break;
                }
            }
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