package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents.publicEvent.PubEvList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PublicEvents {
    private final PublicEventsInterface pubEv;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PubEvAdapter l1;

    /**
     * Costruisce l'oggetto PublicEvents.
     * @param layout L'istanza di View a cui il costruttore si appoggia per trovare la RecyclerView
     *               a cui agganciare gli eventi ricevuti. Non può essere null.
     */
    public PublicEvents(@NonNull View layout) {
        Retrofit retro = new Retrofit.Builder()
                .baseUrl("https://eventmanagerzlf.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        pubEv = retro.create(PublicEventsInterface.class);

        //Imposto la RecyclerView
        mRecyclerView = layout.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(layout.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        l1 = new PubEvAdapter();
        mRecyclerView.setAdapter(l1);
    }

    /**
     * Esegue la chiamata all'API remota e ottiene gli eventi pubblici presenti.
     * I parametri indicati di seguito, quando non diversamente indicato, non possono essere null, ma
     * possono essere utilizzati per filtrare gli eventi in base alle preferenze dell'utente.
     * Si noti che, per eseguire tale azione, è richiesto che l'utente sia in possesso di un token di
     * autorizzazione (fornito ad ogni accesso al sistema).
     * @param l Il ConstraintLayout a cui legare gli eventi nella schermata dell'utente. Non pu&ograve; essere null.
     * @param token Il token di accesso dell'utente
     * @param nomeAtt Il nome dell'attivit&agrave; cercata
     * @param categoria La categoria di eventi cercati
     * @param durata La durata degli eventi cercati. Questo valore deve rappresentare un intero.
     * @param indirizzo L'indirizzo a cui si tengono gli eventi cercati
     * @param citta La città in cui si tengono gli eventi cercati.
     */
    public void getEvents(ConstraintLayout l,
                          @Nullable String token, @Nullable String nomeAtt,
                          @Nullable String categoria, @Nullable String durata,
                          @Nullable String indirizzo, @Nullable String citta) {
        Call<JsonObject> call = pubEv.pubEv(token, nomeAtt, categoria, durata, indirizzo, citta);
        call.enqueue(new Callback<JsonObject>() {
            /**
             * Invoked for a received HTTP response.
             *
             * <p>Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call {@link Response#isSuccessful()} to determine if the response indicates success.
             *
             * @param call
             * @param response
             */
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                PubEvList l2 = new PubEvList();
                if (response.body() != null) {
                    if(response.isSuccessful()) {
                        l2 = l2.parseJSON(response.body());
                        if(l2 != null) {
                            //Poiché i risultati vengono ricevuti su un thread secondario, non posso
                            //aggiornare l'interfaccia utente all'interno di questo metodo.
                            l1 = new PubEvAdapter(l2.getList(), l.getContext());
                            mRecyclerView.setAdapter(l1);
                        } else {
                            Log.e("null", "Public event list is null");
                        }
                    } else {
                        Log.i("success", "Unsuccessful operation");
                    }
                } else {
                    Log.d("null", "response is null");
                }
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected exception
             * occurred creating the request or processing the response.
             *
             * @param call
             * @param t
             */
            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                try {
                    throw t;
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
