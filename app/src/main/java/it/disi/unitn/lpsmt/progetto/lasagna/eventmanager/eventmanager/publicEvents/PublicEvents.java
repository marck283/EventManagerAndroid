package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents;

import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.JsonCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.NetworkRequest;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.networkOps.ServerOperation;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PublicEvents extends ServerOperation {
    //private final PublicEventsInterface pubEv;

    private final RecyclerView mRecyclerView;

    private final Fragment f;

    private final ExecutorService executor;

    private final String token, nomeAtt, categoria, durata, indirizzo, citta, orgName;

    /**
     * Costruisce l'oggetto PublicEvents.
     * @param layout L'istanza di View a cui il costruttore si appoggia per trovare la RecyclerView
     *               a cui agganciare gli eventi ricevuti. Non può essere null.
     */
    public PublicEvents(@NonNull Fragment f, @NonNull View layout, @Nullable String token, @Nullable String nomeAtt,
                        @Nullable String categoria, @Nullable String durata,
                        @Nullable String indirizzo, @Nullable String citta, @Nullable String orgName) {
        //Limito il numero di thread a disposizione per non inondare il server di richieste
        executor = Executors.newFixedThreadPool(5);
        Dispatcher dispatcher = new Dispatcher(executor);
        dispatcher.setMaxRequests(5);
        dispatcher.setMaxRequestsPerHost(5);

        super.createOperation(dispatcher);

        /*OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .dispatcher(dispatcher) //Imposto il dispatcher delle richieste
                .build();
        Retrofit retro = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://eventmanagerzlf.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        pubEv = retro.create(PublicEventsInterface.class);*/
        this.f = f;

        //Imposto la RecyclerView
        mRecyclerView = layout.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(layout.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        PubEvAdapter l1 = new PubEvAdapter(f, new EventCallback());
        mRecyclerView.setAdapter(l1);

        this.token = token;
        this.nomeAtt = nomeAtt;
        this.durata = durata;
        this.categoria = categoria;
        this.indirizzo = indirizzo;
        this.citta = citta;
        this.orgName = orgName;
    }

    /**
     * Esegue la chiamata all'API remota e ottiene gli eventi pubblici presenti.
     * I parametri indicati di seguito, quando non diversamente indicato, non possono essere null, ma
     * possono essere utilizzati per filtrare gli eventi in base alle preferenze dell'utente.
     * Si noti che, per eseguire tale azione, è richiesto che l'utente sia in possesso di un token di
     * autorizzazione (fornito ad ogni accesso al sistema).
     */
    public void run() {
        //Implement request with token header
        Pair<String, String> gtoken = new Pair<>("x-access-token", token), nomeAct = new Pair<>("nomeAtt", nomeAtt),
                category = new Pair<>("categoria", categoria), duration = new Pair<>("durata", durata),
                address = new Pair<>("indirizzo", indirizzo), city = new Pair<>("citta", citta),
                orgNome = new Pair<>("orgName", orgName);
        List<Pair<String, String>> list = new ArrayList<>();
        list.add(gtoken);
        list.add(nomeAct);
        list.add(category);
        list.add(duration);
        list.add(address);
        list.add(city);
        list.add(orgNome);

        NetworkRequest nreq = getNetworkRequest();
        Request req = nreq.getRequest(list, getBaseUrl() + "/api/v2/eventiCalendarioPubblico");
        nreq.enqueue(req, new JsonCallback(f, "pub", mRecyclerView, null, executor));
        /*Call<JsonObject> call = pubEv.pubEv(token, nomeAtt, categoria, durata, indirizzo, citta, orgName);
        call.enqueue(new JsonCallback(f, "pub", mRecyclerView, null, executor));*/
    }
}
