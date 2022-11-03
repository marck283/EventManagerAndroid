package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication.Authentication;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.csrfToken.CsrfToken;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.ActivityNavigationDrawerBinding;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.gSignIn.GSignIn;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses.DBProfileImage;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses.DBThread;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.NavigationSharedViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.EventCreationActivity;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.menu_settings.MenuSettingsFragment;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;

public class NavigationDrawerActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private GSignIn account;
    private NavigationView navView;
    private static final int REQ_SIGN_IN = 2, REQ_SIGN_IN_EV_CREATION = 3;
    private DBThread t1;
    private NavigationSharedViewModel vm;

    private void setAlertDialog() {
        AlertDialog d = new AlertDialog.Builder(this).create();
        d.setTitle(getString(R.string.no_session_title));
        d.setMessage(getString(R.string.no_session_content));
        d.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> account.signIn(this, REQ_SIGN_IN_EV_CREATION));
        d.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", (dialog1, which) -> dialog1.dismiss());
        d.setOnDismissListener(d1 -> {
            account.setAccount(null);
            updateUI();
        });
        d.setOnCancelListener(d1 -> {
            account.setAccount(null);
            updateUI();
        });
        d.setCanceledOnTouchOutside(true);
        d.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityNavigationDrawerBinding binding = ActivityNavigationDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNavigationDrawer.toolbar);
        binding.appBarNavigationDrawer.fab.setOnClickListener(view -> {
            if(account.getAccount() == null) {
                setAlertDialog();
            }
        });

        vm = new ViewModelProvider(this).get(NavigationSharedViewModel.class);

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
            try {
                account.setAccount(s.getResult(ApiException.class));
                updateUI();
            } catch(ApiException ex) {
                Log.i("Exception", "An exception was thrown. Error code: " + ex.getStatus());
            }
        }, e -> setAlertDialog());
    }

    public NavigationSharedViewModel getViewModel() {
        return vm;
    }

    public void showSettings() {
        //Da rivedere perché sarebbe di competenza dell'Activity NavigationDrawerActivity
        MenuSettingsFragment f = new MenuSettingsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.constraintLayout, f)
                .addToBackStack("settingsFragment")
                .commit();
    }

    private Bitmap getImageBitmap(String uri) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(uri);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("IOException", "Error getting bitmap", e);
        }
        return bm;
    }

    private void updateUI() {
        navView.getMenu().clear();

        LinearLayout l = (LinearLayout) navView.getHeaderView(0);
        TextView username = l.findViewById(R.id.profile_name);
        TextView email = l.findViewById(R.id.profile_email);
        Log.i("account", String.valueOf(account.getAccount()));
        if(account.getAccount() == null) {
            navView.inflateMenu(R.menu.navmenu_not_logged_in);
            username.setText("");
            email.setText("");
            Log.i("info", "account null");
        } else {
            //L'utente è autenticato; ottieni il token di accesso al server e mostra la UI aggiornata.
            navView.inflateMenu(R.menu.activity_navigation_drawer_drawer);

            GoogleSignInAccount acc = account.getAccount();
            String authCode = acc.getIdToken();
            CsrfToken csrf = new CsrfToken();
            csrf.getCsrfToken(this, new Authentication(), authCode);

            username.setText(getString(R.string.profileName, acc.getDisplayName()));
            email.setText(getString(R.string.email, acc.getEmail()));

            t1 = new DBProfileImage(this, acc.getEmail());
            t1.start();

            ((DBProfileImage)t1).getProfilePic().observe(this, o -> {
                Log.i("valueChanged", o);
                Bitmap bm = getImageBitmap(o);
                ImageView v = l.findViewById(R.id.imageView);
                v.setImageBitmap(bm);
            });

            t1.close();
        }
    }

    public void revokeAccess(MenuItem item) {
        //Prima di eseguire la disconnessione da Google dovrei eseguire la disconnessione dell'utente dal mio server... o no?
        Task<Void> t = account.signOut();
        t.addOnFailureListener(f -> Log.i("logout", "Logout failed"));
        t.addOnCompleteListener(c -> {
            account.setAccount(null);
            vm.setToken("");
            updateUI();
        });
    }

    /**
     * Metodo chiamato quando si cerca di accedere al sistema.
     * @param item Il MenuItem da cui far partire l'Activity di accesso.
     */
    public void startLogin(MenuItem item) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, REQ_SIGN_IN);
    }

    private void signInCheck(int resultCode, @NonNull Intent data) {
        switch(resultCode) {
            case Activity.RESULT_OK: {
                //Autenticato con successo a Google, ora autentica al server e
                //mostra i dati del profilo richiesti

                if(data.getParcelableExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.gAccount") != null) {
                    account.setAccount(data.getParcelableExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.gAccount"));
                } else {
                    account.setAccount(GoogleSignIn.getLastSignedInAccount(this));
                }
                updateUI();
                break;
            }
            case Activity.RESULT_CANCELED: {
                account.setAccount(null);
                updateUI();
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_SIGN_IN) {
            signInCheck(resultCode, data);
        } else {
            if(requestCode == REQ_SIGN_IN_EV_CREATION) {
                signInCheck(resultCode, data);
                if(resultCode == Activity.RESULT_OK) {
                    Intent i = new Intent(this, EventCreationActivity.class);
                    i.putExtra("accessToken", account.getAccount().getIdToken());
                    startActivity(i);
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
        t1 = null;
    }
}