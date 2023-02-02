package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.GraphRequest;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication.Authentication;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.authentication.accountIntegration.AccountIntegration;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.csrfToken.CsrfToken;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.ActivityNavigationDrawerBinding;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.gSignIn.GSignIn;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.NavigationSharedViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.EventCreationActivity;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_search.EventSearchViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.menu_settings.MenuSettingsViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;

public class NavigationDrawerActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private GSignIn account;
    private NavigationView navView;
    private static final int REQ_SIGN_IN = 2, REQ_SIGN_IN_EV_CREATION = 3;
    private NavigationSharedViewModel vm;
    private MenuSettingsViewModel ms;
    private AccessToken accessToken;
    private Profile profile;
    private AccessTokenTracker tracker;
    private boolean prompt = true;
    private EventSearchViewModel esvm;

    private int ivwidth, ivheight;

    private GoogleMap map;

    private void setAlertDialog(boolean eventCreation) {
        prompt = false;
        AlertDialog d = new AlertDialog.Builder(this).create();
        d.setTitle(getString(R.string.no_session_title));
        d.setMessage(getString(R.string.no_session_content));
        d.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> {
            if(eventCreation) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, REQ_SIGN_IN_EV_CREATION);
            } else {
                startLogin(null);
            }
        });
        d.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", (dialog1, which) -> dialog1.dismiss());
        d.setOnDismissListener(d1 -> {
            account.setAccount(null);
            updateUI("logout", null, null, true);
        });
        d.setCanceledOnTouchOutside(true);
        d.show();
    }

    private void showCreaEvento() {
        Intent i = new Intent(this, EventCreationActivity.class);
        if(account != null && account.getAccount() != null) {
            //L'utente è autenticato con Google
            i.putExtra("accessToken", account.getAccount().getIdToken());
        } else {
            //L'utente è autenticato con Facebook
            i.putExtra("accessToken", accessToken.getToken());
        }
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.ActivityNavigationDrawerBinding binding = ActivityNavigationDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNavigationDrawer.toolbar);
        tracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(@Nullable AccessToken accessToken2, @Nullable AccessToken accessToken1) {
                accessToken = accessToken1;
                if(accessToken != null) {
                    vm.setToken(accessToken.getToken());
                } else {
                    vm.setToken("");
                }
            }
        };
        tracker.startTracking();
        binding.appBarNavigationDrawer.fab.setOnClickListener(view -> {
            if(account.getAccount() == null && profile == null) {
                setAlertDialog(true);
            } else {
                showCreaEvento();
            }
        });

        vm = new ViewModelProvider(this).get(NavigationSharedViewModel.class);
        ms = new ViewModelProvider(this).get(MenuSettingsViewModel.class);
        esvm = new ViewModelProvider(this).get(EventSearchViewModel.class);

        accessToken = AccessToken.getCurrentAccessToken();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_event_list, R.id.nav_user_calendar, R.id.nav_user_settings,
                R.id.nav_user_profile, R.id.nav_logout)
                .setOpenableLayout(binding.drawerLayout)
                .build();

        navView = binding.navView;

        NavHostFragment nhf = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_navigation_drawer);
        if(nhf != null) {
            NavController navController = nhf.getNavController();
            navController.setGraph(R.navigation.mobile_navigation);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(binding.navView, navController);
        }
    }

    public void checkMapPermissions(@NonNull GoogleMap map) {
        this.map = map;
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation();
            return;
        }

        // 2. Otherwise, request location permissions from the user.
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 23);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 23);
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    public void enableMyLocation() {
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            return;
        }

        // 2. Otherwise, request location permissions from the user.
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 23);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 23);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != 23) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            AlertDialog missingPermission = new AlertDialog.Builder(this).create();
            missingPermission.setTitle(R.string.missing_location_permission);
            missingPermission.setMessage(getString(R.string.missing_location_permission_message));
        }
    }

    public void onStart() {
        super.onStart();

        ivwidth = navView.getHeaderView(0).getLayoutParams().width;
        ivheight = navView.getHeaderView(0).getLayoutParams().height;

        account = new GSignIn(this);
        vm.init(this);

        /*SharedPreferences prefs = getSharedPreferences("AccTok", MODE_PRIVATE);
        String accessToken = prefs.getString("accessToken", "");
        if(accessToken.equals("")) {
            updateUI("logout", null, null, false);
            if(prompt) {
                setAlertDialog(false);
                prompt = false;
            }
        } else {
            CsrfToken token = new CsrfToken();
            token.getCsrfToken(this, new Authentication(), accessToken, null, "google", null);
        }*/
        profile = Profile.getCurrentProfile();
        if(profile == null) {
            GoogleSignInAccount userAccount = account.getAccount();
            if(userAccount == null) {
                try {
                    userAccount = account.silentSignIn();
                    account.setAccount(userAccount);
                } catch (ApiException ex) {
                    Log.i("Exception", "An exception was thrown. Error code: " + ex.getStatus());
                    updateUI("logout", null, null, false);
                }
            }

            if(userAccount != null) {
                CsrfToken token = new CsrfToken();
                token.getCsrfToken(this, new Authentication(), userAccount.getIdToken(), null, "google", null);
            } else {
                updateUI("logout", null, null, false);
                if(prompt) {
                    setAlertDialog(false);
                }
            }
        } else {
            if(accessToken != null && accessToken.isExpired()) {
                LoginManager.getInstance().logInWithReadPermissions(this, List.of("public_profile", "email"));
            } else {
                accessToken = AccessToken.getCurrentAccessToken();
            }
            makeEmailRequestAndUpdate();
        }
    }

    private void makeEmailRequestAndUpdate() {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,picture,email");
        GraphRequest req = GraphRequest.newMeRequest(accessToken, (jsonObject, graphResponse) -> {
            if(jsonObject != null) {
                CsrfToken token = new CsrfToken();
                token.getCsrfToken(this, new Authentication(), null, accessToken, "facebook", null);
            } else {
                updateUI("logout", null, null, false);
            }
        });
        req.setParameters(parameters);
        req.executeAsync();
    }

    public NavigationSharedViewModel getViewModel() {
        return vm;
    }

    private void navigate(int resId) {
        //Per navigare tra i Fragment di una stessa Activity, in realtà, basta dire all'applicazione
        //di spostare ogni volta il NavController sul Fragment di destinazione...
        Navigation.findNavController(findViewById(R.id.nav_host_fragment_content_navigation_drawer)).navigate(resId);
    }

    public void hideAllFragments(@NonNull MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        NavHostFragment helper = (NavHostFragment) fm.findFragmentById(R.id.nav_host_fragment_content_navigation_drawer);

        int itemId = item.getItemId();
        DrawerLayout d = findViewById(R.id.drawer_layout);

        if(helper != null) {
            if(itemId == R.id.nav_user_settings || itemId == R.id.action_settings) {
                navigate(R.id.nav_user_settings);
            } else {
                navigate(itemId);
            }
        } else {
            Log.i("noFragment", "no fragment with that id");
        }

        d.closeDrawers();
    }

    private void showNotLoggedIn(@NonNull TextView username, @NonNull TextView email) {
        navView.inflateMenu(R.menu.navmenu_not_logged_in);
        username.setText("");
        email.setText("");
    }

    public void updateUI(@NonNull String request, @Nullable String emailF, String pictureF, boolean reauth) {
        navView.getMenu().clear();

        LinearLayout l = (LinearLayout) navView.getHeaderView(0);
        TextView username = l.findViewById(R.id.profile_name);
        TextView email = l.findViewById(R.id.profile_email);

        if(request.equals("logout") && !reauth) {
            showNotLoggedIn(username, email);
            return;
        }

        if(account.getAccount() == null) {
            if(Profile.getCurrentProfile() == null || accessToken.isExpired()) {
                showNotLoggedIn(username, email);
            } else {
                if(request.equals("logout")) {
                    LoginManager.getInstance().logOut();
                    showNotLoggedIn(username, email);
                } else {
                    //L'utente è autenticato con Google; ottieni il token di accesso al server e mostra la UI aggiornata.
                    navView.inflateMenu(R.menu.activity_navigation_drawer_drawer);

                    //Profile non è null, quindi l'utente è autenticato con Facebook. Ottieni il token di accesso e mostra la UI aggiornata.
                    Log.i("id", profile.getId());
                    username.setText(getString(R.string.profileName, profile.getName()));
                    if(emailF != null && !emailF.equals("")) {
                        email.setText(getString(R.string.email, emailF));
                    }

                    Glide.with(l.getContext()).load(pictureF).apply(new RequestOptions().override(ivwidth, ivheight))
                            .optionalCircleCrop().into((ImageView) l.findViewById(R.id.imageView));
                }
            }
        } else {
            //L'utente è autenticato con Google; ottieni il token di accesso al server e mostra la UI aggiornata.
            navView.inflateMenu(R.menu.activity_navigation_drawer_drawer);

            GoogleSignInAccount acc = account.getAccount();
            username.setText(getString(R.string.profileName, acc.getDisplayName()));
            email.setText(getString(R.string.email, acc.getEmail()));
            Glide.with(l.getContext()).load(acc.getPhotoUrl()).apply(new RequestOptions().override(ivwidth, ivheight))
                    .optionalCircleCrop().into((ImageView) l.findViewById(R.id.imageView));
        }
    }

    public void revokeAccess(MenuItem item) {
        if(account != null) {
            Task<Void> t = account.signOut();
            t.addOnFailureListener(f -> Log.i("logout", "Logout failed"));
            t.addOnCompleteListener(c -> {
                account.setAccount(null);
                vm.setToken("");
                updateUI("logout", null, null, false);
            });
        } else {
            accessToken = null;
            LoginManager.getInstance().logOut();
            vm.setToken("");
        }

        navigate(R.id.nav_event_list);
    }

    /**
     * Metodo chiamato quando si cerca di accedere al sistema.
     * @param item Il MenuItem da cui far partire l'Activity di accesso.
     */
    public void startLogin(MenuItem item) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, REQ_SIGN_IN);
    }

    private void signInCheck(int resultCode, Intent data, @NonNull String which, int requestCode) {
        SharedPreferences prefs = getSharedPreferences("AccTok", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        switch(resultCode) {
            case Activity.RESULT_OK: {
                //Autenticato con successo a Google o Facebook, ora autentica al server e
                //mostra i dati del profilo richiesti

                if(which.equals("google")) {
                    //Google login
                    GoogleSignInAccount gSignIn = data.getParcelableExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.gAccount");
                    if(gSignIn != null) {
                        account.setAccount(gSignIn);
                    } else {
                        account.setAccount(GoogleSignIn.getLastSignedInAccount(this));
                    }
                    vm.setToken(account.getAccount().getIdToken());

                    editor.putString("accessToken", vm.getToken().getValue());
                    editor.apply();

                    if(requestCode == 4) {
                        AccountIntegration integration = new AccountIntegration();
                        if(account != null && account.getAccount() != null && account.getAccount().getIdToken() != null) {
                            integration.googleIntegrate(account.getAccount().getIdToken());
                        } else {
                            Log.i("gToken", "Nessun authToken Google valido");
                        }
                    } else {
                        updateUI("login", account.getAccount().getEmail(),
                                account.getAccount().getPhotoUrl().toString(), true);
                    }
                } else {
                    //Facebook login
                    String email, picture = null;
                    if(data != null) {
                        accessToken = data.getParcelableExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fAccessToken");
                        email = data.getStringExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fEmail");

                        //Prima modifica questa istruzione per usare il solo URL al posto di tutti i dettagli dell'immagine
                        //Poi carica l'immagine nell'ImageView del NavigationDrawer usando Glide
                        picture = data.getStringExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fPicture");
                    } else {
                        accessToken = null;
                        email = null;
                    }
                    if(accessToken != null) {
                        vm.setToken(prefs.getString("accessToken", ""));
                        profile = data.getParcelableExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fAccount");
                    }
                    updateUI("login", email, picture, true);
                }
                break;
            }
            case Activity.RESULT_CANCELED: {
                account.setAccount(null);
                updateUI("logout", null, null, false);

                AlertDialog dialog = new AlertDialog.Builder(this).create();
                dialog.setTitle(R.string.login_error_title);
                dialog.setMessage(getString(R.string.login_error));
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which1) -> dialog1.dismiss());
                dialog.show();

                prompt = false;
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_SIGN_IN || requestCode == REQ_SIGN_IN_EV_CREATION || requestCode == 4) {
            if(data != null && data.getParcelableExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.gAccount") != null) {
                signInCheck(resultCode, data, "google", requestCode);
            } else {
                signInCheck(resultCode, data, "facebook", requestCode);
            }
        }
        if(requestCode == REQ_SIGN_IN_EV_CREATION && resultCode == Activity.RESULT_OK) {
            showCreaEvento();
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

        //Salva il token di accesso nelle SharedPreferences per utilizzarlo al successivo accesso all'app.
        Log.i("exitToken", vm.getToken().getValue());
        SharedPreferences prefs = getSharedPreferences("AccTok", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("accessToken", vm.getToken().getValue());
        editor.apply();
        vm = null;
        account = null;
        accessToken = null;
        profile = null;
        tracker.stopTracking();
    }
}