package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.privateEvents;

import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.JsonCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.NetworkRequest;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.networkOps.ServerOperation;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrivateEvents extends ServerOperation {
    //private final PrivateEventsInterface privEv;
    private final RecyclerView mRecyclerView;

    private final String authToken, data;

    public PrivateEvents(@NonNull View layout, String authToken, String data) {
        super();
        /*Retrofit retro = new Retrofit.Builder()
                .baseUrl("https://eventmanagerzlf.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        privEv = retro.create(PrivateEventsInterface.class);*/

        //Qui imposta la RecyclerView, il LayoutManager e l'Adapter
        mRecyclerView = layout.findViewById(R.id.personal_recycler_view);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(layout.getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(lm);
        PrivEvAdapter p1 = new PrivEvAdapter(new EventCallback());
        mRecyclerView.setAdapter(p1);

        this.authToken = authToken;
        this.data = data;
    }

    public void run() {
        /*Call<JsonObject> json = privEv.privEv(authToken, data);
        json.enqueue(new JsonCallback(null, "priv", mRecyclerView, data));*/

        Pair<String, String> atoken = new Pair<>("x-access-token", authToken);

        List<Pair<String, String>> list = new ArrayList<>();
        list.add(atoken);

        NetworkRequest nreq = getNetworkRequest();
        Request req = nreq.getRequest(list, getBaseUrl() + "/api/v2/eventiCalendarioPersonale/" + data);

        nreq.enqueue(req, new JsonCallback(null, "priv", mRecyclerView, data));
    }
}
