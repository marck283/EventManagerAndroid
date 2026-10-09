package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_management;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.JsonCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.organizedEvents.OrgEvAdapter;
import it.disi.unitn.lpsmt.lasagna.network.client.RetrofitClient;
import it.disi.unitn.lpsmt.lasagna.network.repository.OrganizedEventRepository;

public class EventManagementViewModel extends ViewModel {

    private final MutableLiveData<String> evName = new MutableLiveData<>();

    public void setEvName(@NonNull String val) {
        evName.setValue(val);
    }

    public LiveData<String> getEvName() {
        return evName;
    }

    public void getOrgEvents(@NonNull Fragment f, @NonNull View layout, @NonNull String userJwt,
                             @Nullable ActivityResultLauncher<Intent> launcher) {
        RecyclerView mRecyclerView = layout.findViewById(R.id.eventRecyclerView);
        Activity activity = f.getActivity();
        if (activity != null && f.isAdded()) {
            activity.runOnUiThread(() -> {
                RecyclerView.LayoutManager lm = new LinearLayoutManager(layout.getContext(), LinearLayoutManager.VERTICAL, false);
                mRecyclerView.setLayoutManager(lm);
                OrgEvAdapter p1 = new OrgEvAdapter(new EventCallback());
                mRecyclerView.setAdapter(p1);
            });
        }

        if (!userJwt.isEmpty()) {
            RetrofitClient.getInstance().setAccessToken(userJwt);
        }

        OrganizedEventRepository repo = getOrganizedEventRepository(f, launcher, mRecyclerView);

        repo.getOrganizedEvents();
    }

    @NonNull
    private static OrganizedEventRepository getOrganizedEventRepository(@NonNull Fragment f, @Nullable ActivityResultLauncher<Intent> launcher, RecyclerView mRecyclerView) {
        JsonCallback callback = new JsonCallback(f, "org", mRecyclerView, launcher);
        return new OrganizedEventRepository(new OrganizedEventRepository.EventListCallback() {
            @Override
            public void onSuccess(JsonObject eventsJson) {
                callback.handleJsonSuccess(eventsJson);
            }

            @Override
            public void onError(int statusCode, String errorMessage) {
                callback.handleJsonError(statusCode);
            }
        });
    }

    public void getOrgEvents(@NonNull Fragment f, @NonNull View layout, @NonNull String userJwt,
                             @NonNull String searchName, @Nullable ActivityResultLauncher<Intent> launcher) {
        RecyclerView mRecyclerView = layout.findViewById(R.id.eventRecyclerView);
        Activity activity = f.getActivity();
        if (activity != null && f.isAdded()) {
            activity.runOnUiThread(() -> {
                RecyclerView.LayoutManager lm = new LinearLayoutManager(layout.getContext(), LinearLayoutManager.VERTICAL, false);
                mRecyclerView.setLayoutManager(lm);
                OrgEvAdapter p1 = new OrgEvAdapter(new EventCallback());
                mRecyclerView.setAdapter(p1);
            });
        }

        if (!userJwt.isEmpty()) {
            RetrofitClient.getInstance().setAccessToken(userJwt);
        }

        OrganizedEventRepository repo = getOrganizedEventRepository(f, launcher, mRecyclerView);

        if (!searchName.isEmpty()) {
            repo.getOrganizedEventsByName(searchName);
        } else {
            repo.getOrganizedEvents();
        }
    }
}