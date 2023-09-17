package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents;

import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.JsonCallback;
import it.disi.unitn.lpsmt.lasagna.network.NetworkRequest;
import it.disi.unitn.lpsmt.lasagna.network.networkOps.ServerOperation;
import okhttp3.Request;

public class PublicEvents extends ServerOperation {

    private static boolean receivedResult = false;

    private final RecyclerView mRecyclerView;

    private final Fragment f;

    private final String token, nomeAtt, categoria, durata, indirizzo, citta, orgName;

    /**
     * Imposta il valore del campo che verifica se i risultati siano stati ritornati al client a false.
     */
    public void negateResult() {
        receivedResult = false;
    }

    /**
     * Costruisce l'oggetto PublicEvents applicando i filtri specificati. Si noti che, per ottenere
     * gli eventi filtrati, è richiesto che l'utente sia in possesso di un token di autorizzazione
     * (fornito ad ogni accesso al sistema).
     * @param f Il Fragment su cui mostrare gli eventi restituiti come risultato della richiesta.
     *          Questo parametro non pu&ograve; essere null.
     * @param layout L'istanza di View a cui il costruttore si appoggia per trovare la RecyclerView
     *               a cui agganciare gli eventi ricevuti. Questo parametro non pu&ograve; essere null.
     * @param token Il token dell'utente restituito dal sistema in fase di autenticazione. Questo
     *              parametro pu&ograve; essere null.
     * @param nomeAtt Il nome dell'attività su cui filtrare gli eventi. Questo parametro pu&ograve; essere null.
     * @param categoria La categoria (a scelta tra Sport, Spettacolo, Manifestazione, Viaggio e Altro)
     *                  su cui filtrare gli eventi cercati. Questo parametro pu&ograve; essere null.
     * @param durata La durata su cui filtrare gli eventi. Il formato di questo parametro deve essere
     *               "giorni:ore:minuti". Questo parametro pu&ograve; essere null.
     * @param indirizzo L'indirizzo del luogo di un evento su cui filtrare il risultato. Questo parametro
     *                  pu&ograve; essere null.
     * @param citta La citt&agrave; o il comune su cui filtrare il risultato. Questo parametro pu&ograve;
     *              essere null.
     * @param orgName Il nome dell'utente organizzatore su cui filtrare il risultato. Questo parametro
     *                pu&ograve; essere null.
     */
    public PublicEvents(@NonNull Fragment f, @NonNull View layout, @Nullable String token, @Nullable String nomeAtt,
                        @Nullable String categoria, @Nullable String durata,
                        @Nullable String indirizzo, @Nullable String citta, @Nullable String orgName) {
        //Limito il numero di thread a disposizione per non inondare il server di richieste
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
     * Esegue la chiamata all'API remota e ottiene gli eventi pubblici presenti secondo i filtri
     * applicati dall'utente, cacellando il thread se i risultati sono già stati ricevuti.
     */
    public void run() {
        if(receivedResult) {
            if(!isInterrupted()) {
                interrupt();
            }
        } else {
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
            receivedResult = true;
        }
    }
}
