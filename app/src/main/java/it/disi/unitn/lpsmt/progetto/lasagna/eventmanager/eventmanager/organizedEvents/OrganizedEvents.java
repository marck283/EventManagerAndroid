package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.organizedEvents;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.JsonCallback;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrganizedEvents {
    private final OrganizedEventsInterface orgEv;
    private final RecyclerView mRecyclerView;

    private final Fragment f;

    private final ActivityResultLauncher<Intent> launcher;

    public OrganizedEvents(@IdRes int recyclerViewId, @NonNull Fragment f, @NonNull View layout, @Nullable ActivityResultLauncher<Intent> launcher) {
        Retrofit retro = new Retrofit.Builder()
                .baseUrl("https://eventmanagerzlf.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.f = f;
        orgEv = retro.create(OrganizedEventsInterface.class);
        mRecyclerView = layout.findViewById(recyclerViewId);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(layout.getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(lm);
        OrgEvAdapter p1 = new OrgEvAdapter(new EventCallback());
        mRecyclerView.setAdapter(p1);
        this.launcher = launcher;
    }

    public void getOrgEvents(@NonNull String authToken, @Nullable String data) {
        Call<JsonObject> call;
        if(data != null) {
            call = orgEv.orgEv(authToken, data);
            call.enqueue(new JsonCallback(f, "org", mRecyclerView, data, launcher));
        } else {
            call = orgEv.orgEv(authToken);
            call.enqueue(new JsonCallback(f, "org", mRecyclerView, launcher));
        }
    }
}
