package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import it.disi.lasagna.navigationsvm.NavigationSharedViewModel;
import it.disi.unitn.lasagna.eventmanager.userinfo.UserInfo;
import it.disi.unitn.lpsmt.lasagna.eventinfo.interfaces.OrgEvInterface;
import it.disi.unitn.lpsmt.lasagna.gSignIn.GSignIn;
import it.disi.unitn.lpsmt.lasagna.login.AuthenticationInterface;
import it.disi.unitn.lpsmt.lasagna.login.model.LoggedInUser;
import it.disi.unitn.lpsmt.lasagna.network.NetworkCallbackInterface;
import it.disi.unitn.lpsmt.lasagna.network.client.RetrofitClient;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.ActivityNavigationDrawerBinding;
import it.disi.unitn.lpsmt.lasagna.network.NetworkCallback;
import it.disi.unitn.lpsmt.lasagna.sharedprefs.sharedpreferences.SharedPrefs;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.EventCreationActivity;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.menu_settings.MenuSettingsViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;

public class NavigationDrawerActivity extends AppCompatActivity implements AuthenticationInterface,
        NetworkCallbackInterface, OrgEvInterface {

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

    private int ivwidth, ivheight;

    private ActivityResultLauncher<Intent> launcher, evLauncher;

    private void setAlertDialog(boolean eventCreation, @StringRes int title, @StringRes int message) {
        if (isFinishing() || isDestroyed()) {
            return;
        }
        prompt = false;
        AlertDialog d = new AlertDialog.Builder(this).create();
        d.setTitle(getString(title));
        d.setMessage(getString(message));
        d.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> {
            if(eventCreation) {
                Intent intent = new Intent(this, LoginActivity.class);
                evLauncher.launch(intent);
            } else {
                startLogin(null);
            }
        });
        d.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", (dialog1, which) -> dialog1.dismiss());
        d.setOnDismissListener(d1 -> {
            // account.setAccount(null);
            updateUI("logout", null, null, null, true);
            prompt = false;
        });
        d.setCanceledOnTouchOutside(true);
        d.show();
    }

    private void showCreaEvento() {
        NetworkCallback callback = new NetworkCallback(this);
        if(callback.isOnline(this)) {
            Intent i = new Intent(this, EventCreationActivity.class);
            SharedPrefs prefs = new SharedPrefs("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.AccTok", this);
            String token = prefs.getString("accessToken");
            if ((token == null || token.isEmpty()) && vm != null && vm.getToken() != null && vm.getToken().getValue() != null) {
                token = vm.getToken().getValue();
            }
            i.putExtra("access_token", token);
            startActivity(i);
        } else {
            if (isFinishing() || isDestroyed()) {
                return;
            }
            AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setTitle(R.string.no_connection);
            dialog.setMessage(getString(R.string.no_connection_message));
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
            dialog.show();
            updateUI("logout", null, null, null, false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.ActivityNavigationDrawerBinding binding = ActivityNavigationDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result ->
                        onActivityResult(REQ_SIGN_IN, result.getResultCode(), result.getData()));

        evLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->
                        onActivityResult(REQ_SIGN_IN_EV_CREATION, result.getResultCode(), result.getData()));

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
        FloatingActionButton fab = binding.appBarNavigationDrawer.fab;
        if(!fab.hasOnClickListeners()) {
            fab.setOnClickListener(view -> {
                SharedPrefs prefs = new SharedPrefs("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.AccTok", this);
                String token = prefs.getString("accessToken");
                if((token == null || token.isEmpty()) && profile == null) {
                    setAlertDialog(true, R.string.no_session_title, R.string.no_session_content);
                } else {
                    showCreaEvento();
                }
            });
        }

        vm = new ViewModelProvider(this).get(NavigationSharedViewModel.class);
        ms = new ViewModelProvider(this).get(MenuSettingsViewModel.class);

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

    private void checkAuthSetMenu() {
        SharedPrefs prefs = new SharedPrefs("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.AccTok", this);
        String accessToken = prefs.getString("accessToken");

        if (accessToken.isEmpty()) {
            updateUI("logout", null, null, null, false);
            if (prompt) {
                setAlertDialog(false, R.string.no_session_title, R.string.no_session_content);
                prompt = false;
            }
        } else {
            // Set token on RetrofitClient so AuthInterceptor automatically adds x-access-token header
            RetrofitClient.getInstance().setAccessToken(accessToken);

            RetrofitClient.getInstance().getUserApi().getUserProfile().enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        JsonObject body = response.body();
                        UserInfo userInfo = UserInfo.parseJSON(body);
                        updateUI("login", userInfo.getString("email"), userInfo.getString("nome"), userInfo.getString("profilePic"), true);
                        vm.setToken(accessToken);
                    } else {
                        prefs.setString("accessToken", "");
                        prefs.setString("userId", "");
                        prefs.apply();
                        updateUI("logout", null, null, null, false);
                    }
                }

                @Override
                public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                    updateUI("logout", null, null, null, false);
                }
            });
        }
    }

    public void onStart() {
        super.onStart();

        ivwidth = navView.getHeaderView(0).getLayoutParams().width;
        ivheight = navView.getHeaderView(0).getLayoutParams().height;

        account = new GSignIn(this, R.string.server_client_id);
        vm.init(this);

        //Soluzione al problema visivo del menù sbagliato quando la connessione ad Internet non è presente
        //all'apertura dell'applicazione
        NetworkCallback callback = new NetworkCallback(this);
        if(!callback.isOnline(this)) {
            updateUI("logout", null, null, null, false);
            callback.registerNetworkCallback();
            callback.addDefaultNetworkActiveListener(this::checkAuthSetMenu);
            callback.unregisterNetworkCallback();
        } else {
            checkAuthSetMenu();
        }
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

        ImageView userPic = navView.getHeaderView(0).findViewById(R.id.imageView);
        Drawable userImage = AppCompatResources.getDrawable(getApplicationContext(), R.mipmap.ic_launcher);
        if(userImage != null) {
            userPic.setImageDrawable(userImage);
        }
    }

    public void updateUI(@NonNull String request, @Nullable String emailF, String name, String pictureF, boolean reauth) {
        navView.getMenu().clear();

        LinearLayout l = (LinearLayout) navView.getHeaderView(0);
        TextView username = l.findViewById(R.id.profile_name);
        TextView email = l.findViewById(R.id.profile_email);

        NetworkCallback callback = new NetworkCallback(this);
        if((request.equals("logout") && !reauth) || !callback.isOnline(this)) {
            showNotLoggedIn(username, email);
            return;
        }

        if(request.equals("logout")) {
            LoginManager.getInstance().logOut();
            showNotLoggedIn(username, email);
        } else {
            //L'utente è autenticato con Google; ottieni il token di accesso al server e mostra la UI aggiornata.
            navView.inflateMenu(R.menu.activity_navigation_drawer_drawer);

            //Profile non è null, quindi l'utente è autenticato con Facebook. Ottieni il token di accesso e mostra la UI aggiornata.
            //Log.i("id", profile.getId());
            username.setText(getString(R.string.profileName, /*profile.getName()*/name));
            if(emailF != null && !emailF.isEmpty()) {
                email.setText(getString(R.string.email, emailF));
            }

            Glide.with(l.getContext()).load(pictureF).apply(new RequestOptions().override(ivwidth, ivheight))
                    .optionalCircleCrop().into((ImageView) l.findViewById(R.id.imageView));
        }

        DrawerLayout d = findViewById(R.id.drawer_layout);
        d.closeDrawers();
    }

    public void revokeAccess(MenuItem item) {
        logout(null);
        navigate(R.id.nav_event_list);
    }

    /**
     * Metodo chiamato quando si cerca di accedere al sistema.
     * @param item Il MenuItem da cui far partire l'Activity di accesso.
     */
    public void startLogin(MenuItem item) {
        Intent intent = new Intent(this, LoginActivity.class);
        launcher.launch(intent);
    }

    private void signInCheck(int resultCode, Intent data) {
        SharedPrefs prefs = new SharedPrefs(
                "it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.AccTok", this);

        switch (resultCode) {
            case Activity.RESULT_OK -> {
                //Autenticato con successo a Google o Facebook, ora autentica al server e
                //mostra i dati del profilo richiesti

                String email, picture;

                //Google login
                Log.i("login", "Google login");
                String token = data != null ? data.getStringExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fToken") : null;
                if (token == null || token.isEmpty()) {
                    token = prefs.getString("accessToken");
                }
                vm.setToken(token);
                email = data != null ? data.getStringExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fEmail") : null;
                picture = data != null ? data.getStringExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fPicture") : null;
                String displayName = data != null ? data.getStringExtra("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.fName") : "";
                updateUI("login", email, displayName, picture, false);
            }
            case Activity.RESULT_CANCELED -> {
                Log.i("login", "Login failed");
                updateUI("logout", null, null, null, false);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if((requestCode == REQ_SIGN_IN || requestCode == REQ_SIGN_IN_EV_CREATION) && data != null) {
            signInCheck(resultCode, data);
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
        SharedPrefs prefs = new SharedPrefs("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.AccTok",
                this);
        if(vm.getToken().getValue() == null) {
            prefs.setString("accessToken", "");
        } else {
            prefs.setString("accessToken", vm.getToken().getValue());
        }
        prefs.apply();
        vm = null;
        account = null;
        accessToken = null;
        profile = null;
        tracker.stopTracking();
    }

    @Override
    public void shareData(@NonNull LoggedInUser data, @Nullable Intent intent) {
        updateUI("login", data.getEmail(), data.getName(), data.getProfilePic(), true);
        getViewModel().postToken(data.getToken());
    }

    public void logout(@Nullable Intent intent) {
        SharedPrefs prefs = new SharedPrefs("it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.AccTok", this);
        prefs.setString("accessToken", "");
        prefs.setString("userId", "");
        prefs.apply();

        getViewModel().setToken("");
        it.disi.unitn.lpsmt.lasagna.network.client.RetrofitClient.getInstance().setAccessToken("");

        if (account != null) {
            account.signOut(this, () -> updateUI("logout", null, null, null, false));
        } else {
            updateUI("logout", null, null, null, false);
        }
    }

    public void showNotLoggedInMsg() {
        setAlertDialog(false, R.string.user_not_logged_in, R.string.user_not_logged_in_message);
    }

    @Override
    public void showOnLostMsg() {
        if (isFinishing() || isDestroyed()) {
            return;
        }
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle(R.string.no_connection);
        alert.setMessage(getString(R.string.no_connection_message_short));
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
        alert.show();
    }

    @Override
    public void showOnUnavailableMsg() {
        if (isFinishing() || isDestroyed()) {
            return;
        }
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle(R.string.no_connection);
        alert.setMessage(getString(R.string.no_connection_message_short));
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
        alert.show();
    }

    @Override
    public void showRes(int resCode) {
        if (isFinishing() || isDestroyed()) {
            return;
        }
        switch(resCode) {
            case 401 -> setAlertDialog(false, R.string.user_not_logged_in, R.string.user_not_logged_in_message);

            case 403 -> {
                AlertDialog dialog = new AlertDialog.Builder(this).create();
                dialog.setTitle(R.string.unauthorized_attempt);
                dialog.setMessage(getString(R.string.unauthorized_attempt_message));
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                dialog.show();
            }

            case 200 -> {
                AlertDialog dialog = new AlertDialog.Builder(this).create();
                dialog.setTitle(R.string.attempt_ok);
                dialog.setMessage(getString(R.string.attempt_ok_message));
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                dialog.show();
            }

            case 404 -> {
                AlertDialog dialog = new AlertDialog.Builder(this).create();
                dialog.setTitle(R.string.no_event);
                dialog.setMessage(getString(R.string.no_event_message));
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                dialog.show();
            }

            default -> {
                AlertDialog dialog = new AlertDialog.Builder(this).create();
                dialog.setTitle(R.string.unknown_error);
                dialog.setMessage(getString(R.string.unknown_error_message));
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                dialog.show();
            }
        }
    }
}