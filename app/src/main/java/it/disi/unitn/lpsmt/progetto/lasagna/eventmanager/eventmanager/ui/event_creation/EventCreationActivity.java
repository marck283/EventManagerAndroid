package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Locale;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.ActivityEventCreationBinding;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.listeners.EventRecognitionListener;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.listeners.SpeechOnTouchListener;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.newDate.EventLocationViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.newDate.NewDateViewModel;

public class EventCreationActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private String idToken;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private EventViewModel evm;
    private NewDateViewModel nd;
    private EventLocationViewModel elvm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.databinding.ActivityEventCreationBinding binding = ActivityEventCreationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        idToken = getIntent().getStringExtra("access_token");

        nd = new ViewModelProvider(this).get(NewDateViewModel.class);

        NavHostFragment nhf = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_event_creation);
        if(nhf != null) {
            NavController navController = nhf.getNavController();
            NavigationUI.setupWithNavController(binding.toolbar, navController);
        }

        //Controllo di che siano stati garantiti i permessi necessari a registrare la voce dell'utente
        //Se i peressi non sono garantiti, allora chiamo checkPermission().
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

        //Crea un'istanza di SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new EventRecognitionListener(this));

        evm = new ViewModelProvider(this).get(EventViewModel.class);
        elvm = new ViewModelProvider(this).get(EventLocationViewModel.class);
    }

    public EventViewModel getViewModel() {
        return evm;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void onStart() {
        super.onStart();

        ImageButton button = findViewById(R.id.imageButton6);
        button.setOnTouchListener(new SpeechOnTouchListener(speechRecognizer, speechRecognizerIntent));

        CheckBox box = findViewById(R.id.checkBox);
        box.setOnCheckedChangeListener((buttonView, isChecked) -> evm.setPrivEvent(isChecked));
    }

    private void checkPermission() {
        //Richiedo i permessi per la registrazione della voce dell'utente
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 23);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_event_creation);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * Controlla il risultato della richiesta di permessi effettuata.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 23 && grantResults.length > 0 ) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
        speechRecognizerIntent = null;
        nd = null;
    }
}