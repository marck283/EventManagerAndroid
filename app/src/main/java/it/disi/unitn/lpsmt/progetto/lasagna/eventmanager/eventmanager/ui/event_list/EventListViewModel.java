package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_list;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.JsonCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents.PubEvAdapter;
import it.disi.unitn.lpsmt.lasagna.network.client.RetrofitClient;
import it.disi.unitn.lpsmt.lasagna.network.repository.EventRepository;

public class EventListViewModel extends ViewModel {
    private final MutableLiveData<String> evName = new MutableLiveData<>(), orgName = new MutableLiveData<>(),
            category = new MutableLiveData<>(), duration = new MutableLiveData<>(), address = new MutableLiveData<>(),
            city = new MutableLiveData<>();

    public void setEvName(@NonNull String val) {
        evName.setValue(val);
    }

    public LiveData<String> getEvName() {
        return evName;
    }

    public void setCategory(@NotNull String val) {
        category.setValue(val);
    }

    public LiveData<String> getCategory() {
        return category;
    }

    public void setDuration(@NotNull String val) {
        duration.setValue(val);
    }

    public LiveData<String> getDuration() {
        return duration;
    }

    public void setAddress(@NotNull String val) {
        address.setValue(val);
    }

    public LiveData<String> getAddress() {
        return address;
    }

    public void setCity(@NotNull String val) {
        city.setValue(val);
    }

    public LiveData<String> getCity() {
        return city;
    }

    public void setOrgName(@NonNull String val) {
        orgName.setValue(val);
    }

    public LiveData<String> getOrgName() {
        return orgName;
    }

    public void getEvents(@NonNull Fragment f, @NonNull View layout, String accessToken) {
        RecyclerView mRecyclerView = layout.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(layout.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        PubEvAdapter l1 = new PubEvAdapter(f, new EventCallback());
        mRecyclerView.setAdapter(l1);

        if (accessToken != null && !accessToken.isEmpty()) {
            RetrofitClient.getInstance().setAccessToken(accessToken);
        }

        JsonCallback callback = new JsonCallback(f, "pub", mRecyclerView);
        EventRepository repo = new EventRepository();

        repo.getPublicCalendarEvents(new EventRepository.EventDataCallback() {
            @Override
            public void onSuccess(JsonObject data) {
                callback.handleJsonSuccess(data);
            }

            @Override
            public void onError(int statusCode, String errorMessage) {
                callback.handleJsonError(statusCode);
            }
        });
    }
}