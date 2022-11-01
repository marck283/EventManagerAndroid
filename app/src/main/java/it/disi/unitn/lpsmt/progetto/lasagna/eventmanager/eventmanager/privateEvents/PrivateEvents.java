package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.privateEvents;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.JsonCallback;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrivateEvents {
    private final PrivateEventsInterface privEv;
    private final RecyclerView mRecyclerView;

    public PrivateEvents(@NonNull View layout) {
        Retrofit retro = new Retrofit.Builder()
                .baseUrl("https://eventmanagerzlf.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        privEv = retro.create(PrivateEventsInterface.class);

        //Qui imposta la RecyclerView, il LayoutManager e l'Adapter
        mRecyclerView = layout.findViewById(R.id.personal_recycler_view);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(layout.getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(lm);
        PrivEvAdapter p1 = new PrivEvAdapter(new EventCallback());
        mRecyclerView.setAdapter(p1);
    }

    public void getPersonalEvents(String authToken, String data) {
        Call<JsonObject> json = privEv.privEv(authToken, data);
        json.enqueue(new JsonCallback("priv", mRecyclerView));
    }
}
