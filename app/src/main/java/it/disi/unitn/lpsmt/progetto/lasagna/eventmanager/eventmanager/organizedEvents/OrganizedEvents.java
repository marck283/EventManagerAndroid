package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.organizedEvents;

import android.content.Intent;
import android.util.Pair;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.JsonCallback;
import it.disi.unitn.lpsmt.lasagna.network.NetworkRequest;
import it.disi.unitn.lpsmt.lasagna.network.networkOps.ServerOperation;
import okhttp3.Request;

public class OrganizedEvents extends ServerOperation {

    private final RecyclerView mRecyclerView;

    private final Fragment f;

    private final ActivityResultLauncher<Intent> launcher;

    private final String evName, authToken, data;

    private final NetworkRequest nreq;

    private final List<Pair<String, String>> list;

    public OrganizedEvents(@IdRes int recyclerViewId, @NonNull Fragment f, @NonNull View layout,
                           @Nullable ActivityResultLauncher<Intent> launcher, @NonNull String authToken,
                           @Nullable String data) {
        super();

        this.f = f;
        mRecyclerView = layout.findViewById(recyclerViewId);
        f.requireActivity().runOnUiThread(() -> {
            RecyclerView.LayoutManager lm = new LinearLayoutManager(layout.getContext(), LinearLayoutManager.VERTICAL,
                    false);
            mRecyclerView.setLayoutManager(lm);
            OrgEvAdapter p1 = new OrgEvAdapter(new EventCallback());
            mRecyclerView.setAdapter(p1);
        });
        this.launcher = launcher;
        evName = null;
        nreq = getNetworkRequest();
        list = new ArrayList<>();
        this.authToken = authToken;
        this.data = data;
    }

    public OrganizedEvents(@IdRes int recyclerViewId, @NonNull Fragment f, @NonNull View layout,
                           @Nullable String evName, @Nullable ActivityResultLauncher<Intent> launcher,
                           @NonNull String authToken, @Nullable String data) {
        super();

        this.f = f;
        mRecyclerView = layout.findViewById(recyclerViewId);
        f.requireActivity().runOnUiThread(() -> {
            RecyclerView.LayoutManager lm = new LinearLayoutManager(layout.getContext(), LinearLayoutManager.VERTICAL,
                    false);
            mRecyclerView.setLayoutManager(lm);
            OrgEvAdapter p1 = new OrgEvAdapter(new EventCallback());
            mRecyclerView.setAdapter(p1);
        });
        this.launcher = launcher;
        this.evName = evName;
        nreq = getNetworkRequest();
        list = new ArrayList<>();
        this.authToken = authToken;
        this.data = data;
    }

    /*public void getOrgEvents(@NonNull String authToken, @Nullable String data) {
        Pair<String, String> token = new Pair<>("x-access-token", authToken);
        list.add(token);
        Request req;
        if(data != null) {
            req = nreq.getRequest(list, getBaseUrl() + "/api/v2/EventOrgList/" + data);
            nreq.enqueue(req, new JsonCallback(f, "org", mRecyclerView, data, launcher));
        } else {
            req = nreq.getRequest(list, getBaseUrl() + "/api/v2/EventOrgList");
            nreq.enqueue(req, new JsonCallback(f, "org", mRecyclerView, launcher));
        }
    }

    public void getOrgEventsWithName(@NonNull String authToken) {
        Pair<String, String> token = new Pair<>("x-access-token", authToken), ename = new Pair<>("name", evName);
        list.add(token);
        list.add(ename);

        if(evName != null) {
            Request req = nreq.getRequest(list, getBaseUrl() + "/api/v2/EventOrgList");
            nreq.enqueue(req, new JsonCallback(f, "org", mRecyclerView));
        }
    }*/

    public void run() {
        Pair<String, String> token = new Pair<>("x-access-token", authToken), ename = new Pair<>("name", evName);
        list.add(token);
        Request req;
        if(data != null) {
            req = nreq.getRequest(list, getBaseUrl() + "/api/v2/EventOrgList/" + data);
            nreq.enqueue(req, new JsonCallback(f, "org", mRecyclerView, data, launcher));
        } else {
            if(evName != null) {
                list.add(ename);
            }
            req = nreq.getRequest(list, getBaseUrl() + "/api/v2/EventOrgList");
            nreq.enqueue(req, new JsonCallback(f, "org", mRecyclerView, launcher));
        }
    }
}
