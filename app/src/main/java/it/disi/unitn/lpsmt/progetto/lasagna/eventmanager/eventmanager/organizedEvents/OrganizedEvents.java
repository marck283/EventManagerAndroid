package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.organizedEvents;

import android.view.View;

import androidx.annotation.NonNull;
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

    public OrganizedEvents(@NonNull Fragment f, @NonNull View layout) {
        Retrofit retro = new Retrofit.Builder()
                .baseUrl("https://eventmanagerzlf.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.f = f;
        orgEv = retro.create(OrganizedEventsInterface.class);
        mRecyclerView = layout.findViewById(R.id.organizer_recycler_view);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(layout.getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(lm);
        OrgEvAdapter p1 = new OrgEvAdapter(f, new EventCallback());
        mRecyclerView.setAdapter(p1);
    }

    public void getOrgEvents(String authToken, String data) {
        Call<JsonObject> call = orgEv.orgEv(authToken, data);
        call.enqueue(new JsonCallback(f, "org", mRecyclerView, data));
    }
}
