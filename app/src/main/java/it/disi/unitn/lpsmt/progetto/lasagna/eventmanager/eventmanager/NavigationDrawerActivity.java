package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.ActivityNavigationDrawerBinding;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.gSignIn.GSignIn;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses.DBProfileImage;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses.DBThread;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses.DBUserThread;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_list.EventListFragment;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;

public class NavigationDrawerActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private GSignIn account;
    private NavigationView navView;
    private static final int REQ_SIGN_IN = 2;
    private DBThread t1;

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
        account = new GSignIn(this);
        t1 = new DBThread(this);

        NavHostFragment nhf = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_navigation_drawer);
        if(nhf != null) {
            NavController navController = nhf.getNavController();
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(binding.navView, navController);
        }
    }

    public void onStart() {
        super.onStart();
        account.silentSignIn(s -> {
            account.setAccount(s);
            updateUI();
        }, e -> {
            AlertDialog d = new AlertDialog.Builder(this).create();
            d.setTitle(getString(R.string.no_session_title));
            d.setMessage(getString(R.string.no_session_content));
            d.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> account.signIn(this, REQ_SIGN_IN));
            d.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", (dialog1, which) -> dialog1.dismiss());
            d.show();
            updateUI();
        });
    }

    private void updateUI() {
        navView.getMenu().clear();
        if(account.getAccount() == null) {
            navView.inflateMenu(R.menu.navmenu_not_logged_in);
            Log.i("info", "account null");
        } else {
            //L'utente è autenticato; ottieni il token di accesso al server e mostra la UI aggiornata.
            navView.inflateMenu(R.menu.activity_navigation_drawer_drawer);
            LinearLayout l = (LinearLayout) navView.getHeaderView(0);

            t1 = new DBUserThread(this, account.getAccount());
            t1.start();

            synchronized(this) {
                //Questa riga di codice restituisce 1 perché vi è un solo header a disposizione della
                //NavigationView: il LinearLayout. Questo è il motivo per cui, nelle righe di codice
                //precedenti, si è cercato di creare un'istanza di LinearLayout.
                //Log.i("count", String.valueOf(navView.getHeaderCount()));

                GoogleSignInAccount acc = account.getAccount();

                //Perché non riesco a trovare un'istanza di EventListFragment?
                Fragment ef = getSupportFragmentManager().findFragmentById(R.id.nav_event_list);
                if(ef != null) {
                    ((EventListFragment)ef).getData(acc.getIdToken());
                } else {
                    Log.i("noFragment", "no fragment with that name");
                }

                TextView username = l.findViewById(R.id.profile_name);
                username.setText(getString(R.string.profileName, acc.getDisplayName()));

                TextView email = l.findViewById(R.id.profile_email);
                email.setText(getString(R.string.email, acc.getEmail()));

                while(((DBUserThread)t1).result()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                t1 = new DBProfileImage(this, acc.getEmail(), l);
                t1.start();
            }
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

                    //Riscrivere questa parte cercando di ottenere i dati da qualcos'altro, se possibile.
                    account.setAccount(data.getParcelableExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.gAccount"));
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

    public void onDestroy() {
        super.onDestroy();
        t1.close();
        t1 = null;
    }
}