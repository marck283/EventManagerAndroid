package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.organizedEvents;

import android.content.Intent;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.JsonCallback;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrganizedEvents {
    private final OrganizedEventsInterface orgEv;

    private final OrgEvInterfaceWithData orgEvWD;

    private final RecyclerView mRecyclerView;

    private final Fragment f;

    private final ActivityResultLauncher<Intent> launcher;

    private final String evName;

    public OrganizedEvents(@IdRes int recyclerViewId, @NonNull Fragment f, @NonNull View layout,
                           @Nullable ActivityResultLauncher<Intent> launcher) {
        Retrofit retro = new Retrofit.Builder()
                .baseUrl("https://eventmanagerzlf.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.f = f;
        orgEv = null;
        orgEvWD = retro.create(OrgEvInterfaceWithData.class);
        mRecyclerView = layout.findViewById(recyclerViewId);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(layout.getContext(), LinearLayoutManager.VERTICAL,
                false);
        mRecyclerView.setLayoutManager(lm);
        OrgEvAdapter p1 = new OrgEvAdapter(f, new EventCallback());
        mRecyclerView.setAdapter(p1);
        this.launcher = launcher;
        evName = null;
    }

    public OrganizedEvents(@IdRes int recyclerViewId, @NonNull Fragment f, @NonNull View layout,
                           @Nullable String evName, @Nullable ActivityResultLauncher<Intent> launcher) {
        Retrofit retro = new Retrofit.Builder()
                .baseUrl("https://eventmanagerzlf.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.f = f;
        orgEvWD = null;
        orgEv = retro.create(OrganizedEventsInterface.class);
        mRecyclerView = layout.findViewById(recyclerViewId);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(layout.getContext(), LinearLayoutManager.VERTICAL,
                false);
        mRecyclerView.setLayoutManager(lm);
        OrgEvAdapter p1 = new OrgEvAdapter(f, new EventCallback());
        mRecyclerView.setAdapter(p1);
        this.launcher = launcher;
        this.evName = evName;
    }

    //Perché la chiamata a questa funzione non restituisce nulla quando cerco di ottenere
    //gli eventi organizzati da un certo utente con almeno un evento organizzato?

    //Perché questa chiamata permette di visualizzare solo un evento organizzato?
    public void getOrgEvents(@NonNull String authToken, @Nullable String data) {
        Call<JsonObject> call;
        if(data != null) {
            call = orgEvWD.orgEv(authToken, data);
            call.enqueue(new JsonCallback(f, "org", mRecyclerView, data, launcher));
        } else {
            call = orgEvWD.orgEv(authToken);
            call.enqueue(new JsonCallback(f, "org", mRecyclerView, launcher));
        }
    }

    public void getOrgEventsWithName(@NonNull String authToken) {
        Call<JsonObject> call;
        if(evName != null) {
            call = orgEv.orgEv(authToken, evName);
            call.enqueue(new JsonCallback(f, "org", evName, mRecyclerView));
        }
    }
}
