package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.JsonCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.networkOps.ServerOperation;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PublicEvents/* extends ServerOperation*/ {
    private final PublicEventsInterface pubEv;
    private final RecyclerView mRecyclerView;

    private final Fragment f;

    private final ExecutorService executor;

    /**
     * Costruisce l'oggetto PublicEvents.
     * @param layout L'istanza di View a cui il costruttore si appoggia per trovare la RecyclerView
     *               a cui agganciare gli eventi ricevuti. Non può essere null.
     */
    public PublicEvents(@NonNull Fragment f, @NonNull View layout) {
        //Limito il numero di thread a disposizione per non inondare il server di richieste
        executor = Executors.newFixedThreadPool(5);
        Dispatcher dispatcher = new Dispatcher(executor);
        dispatcher.setMaxRequests(5);
        dispatcher.setMaxRequestsPerHost(5);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .dispatcher(dispatcher) //Imposto il dispatcher delle richieste
                .build();
        Retrofit retro = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://eventmanagerzlf.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        pubEv = retro.create(PublicEventsInterface.class);
        this.f = f;

        //Imposto la RecyclerView
        mRecyclerView = layout.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(layout.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        PubEvAdapter l1 = new PubEvAdapter(f, new EventCallback());
        mRecyclerView.setAdapter(l1);
    }

    /**
     * Esegue la chiamata all'API remota e ottiene gli eventi pubblici presenti.
     * I parametri indicati di seguito, quando non diversamente indicato, non possono essere null, ma
     * possono essere utilizzati per filtrare gli eventi in base alle preferenze dell'utente.
     * Si noti che, per eseguire tale azione, è richiesto che l'utente sia in possesso di un token di
     * autorizzazione (fornito ad ogni accesso al sistema).
     * @param token Il token di accesso dell'utente
     * @param nomeAtt Il nome dell'attivit&agrave; cercata
     * @param categoria La categoria di eventi cercati
     * @param durata La durata degli eventi cercati. Questo valore deve rappresentare un intero.
     * @param indirizzo L'indirizzo a cui si tengono gli eventi cercati
     * @param citta La città in cui si tengono gli eventi cercati.
     */
    public void getEvents(@Nullable String token, @Nullable String nomeAtt,
                          @Nullable String categoria, @Nullable String durata,
                          @Nullable String indirizzo, @Nullable String citta, @Nullable String orgName) {
        Call<JsonObject> call = pubEv.pubEv(token, nomeAtt, categoria, durata, indirizzo, citta, orgName);
        call.enqueue(new JsonCallback(f, "pub", mRecyclerView, null, executor));
    }
}
