package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
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
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses.DBUserThread;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;

public class NavigationDrawerActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private GoogleSignInAccount account;
    private NavigationView navView;
    private static final int REQ_SIGN_IN = 2;
    private SharedPreferences prefs;

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

        NavHostFragment nhf = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_navigation_drawer);
        if(nhf != null) {
            NavController navController = nhf.getNavController();
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(binding.navView, navController);
        }
    }

    public void onStart() {
        super.onStart();
        updateUI();
    }

    private void updateUI() {
        navView.getMenu().clear();
        if(account == null) {
            navView.inflateMenu(R.menu.navmenu_not_logged_in);
            Log.i("info", "account null");
        } else {
            //L'utente è autenticato; ottieni il token di accesso al server e mostra la UI aggiornata.
            navView.inflateMenu(R.menu.activity_navigation_drawer_drawer);
            LinearLayout l = (LinearLayout) navView.getHeaderView(0);

            //Questo è null perché l'accesso a Google appena l'app si avvia non è stato configurato per restituire un token di accesso.
            //Pertanto sarò forzato ad implementare un meccanismo per salvare il token (magari criptato) su un database locale all'applicazione.
            //Questo mi permetterebbe di ritrovarlo in modo molto semplice tramite una query apposita.
            DBUserThread ut = new DBUserThread(this, account);
            new Thread(ut).start();

            //Questa riga di codice restituisce 1 perché vi è un solo header a disposizione della
            //NavigationView: il LinearLayout. Questo è il motivo per cui, nelle righe di codice
            //precedenti, si è cercato di creare un'istanza di LinearLayout.
            //Log.i("count", String.valueOf(navView.getHeaderCount()));

            TextView username = l.findViewById(R.id.profile_name);
            username.setText(getString(R.string.profileName, account.getDisplayName()));

            TextView email = l.findViewById(R.id.profile_email);
            email.setText(getString(R.string.email, account.getEmail()));
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
                    account = data.getParcelableExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.gAccount");
                    updateUI();
                    break;
                }
                case Activity.RESULT_CANCELED: {
                    account = null;
                    updateUI();
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