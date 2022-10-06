package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.ActivityNavigationDrawerBinding;

public class NavigationDrawerActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavigationDrawerBinding binding;
    private GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavigationDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNavigationDrawer.toolbar);
        binding.appBarNavigationDrawer.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        account = GoogleSignIn.getLastSignedInAccount(this);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_event_list, R.id.nav_user_calendar, R.id.nav_user_settings,
                R.id.nav_user_profile, R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();

        updateUI(navigationView);

        NavHostFragment nhf = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_navigation_drawer);
        if(nhf != null) {
            NavController navController = nhf.getNavController();
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
        }
    }

    /**
     * Aggiorna l'interfaccia utente in base al fatto che l'utente sia ancora autenticato.
     * @param navigationView L'istanza di View necessaria per impostare il layout corretto
     */
    private void updateUI(NavigationView navigationView) {
        if(account == null) {
            //Poiché il menù viene caricato dal file di layout activity_navigation_drawer_drawer.xml
            //devo rimuovere da codice tutti gi elementi che contiene e poi inserire quelli del
            //menù dato in navmenu_not_logged_in.xml.
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.navmenu_not_logged_in);
        } else {
            //Ottieni l'intestazione del menù di navigazione standard per inserire username e email
            //dell'utente. Si noti che nessuno dei seguenti valori è garantito per ogni utente.
            String name = account.getGivenName();
            String email = account.getEmail();
            Uri photoUrl = account.getPhotoUrl();
            byte[] imgBytes = readBytes(photoUrl);

            //Next: set up the ImageView, then set up the two text views.
            View view = navigationView.getHeaderView(R.id.nav_view);
            Bitmap imgBitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
            ImageView imgView = view.findViewById(R.id.imageView);
            imgView.setImageBitmap(imgBitmap);

            TextView un = view.findViewById(R.id.profile_name), email1 = view.findViewById(R.id.profile_email);
            un.setText(name);
            email1.setText(email);
        }
    }

    /**
     * Ritorna un array di byte che rappresenta la risorsa fornita sotto forma di Uri.
     * @param uri La risorsa fornita sotto forma di Uri
     * @return Un array di byte che rappresenta la risorsa fornita
     */
    private byte[] readBytes(Uri uri) {
        ContentResolver cr = getBaseContext().getContentResolver();
        InputStream stream;
        ByteArrayOutputStream byteArrayStream;
        byte[] bytes = null;
        try {
            stream = cr.openInputStream(uri);
            byteArrayStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int i;
            while ((i = stream.read(buffer, 0, buffer.length)) > 0) {
                byteArrayStream.write(buffer, 0, i);
            }
            stream.close();
            bytes = byteArrayStream.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
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