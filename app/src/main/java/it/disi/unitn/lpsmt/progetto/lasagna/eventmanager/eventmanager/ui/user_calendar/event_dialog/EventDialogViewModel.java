package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_calendar.event_dialog;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.Contract;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.JsonCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.organizedEvents.OrgEvAdapter;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.privateEvents.PrivEvAdapter;
import it.disi.unitn.lpsmt.lasagna.network.client.RetrofitClient;
import it.disi.unitn.lpsmt.lasagna.network.repository.EventRepository;
import it.disi.unitn.lpsmt.lasagna.network.repository.OrganizedEventRepository;

public class EventDialogViewModel extends ViewModel {

    @NonNull
    @Contract(pure = true)
    private String padStart(@NonNull String s) {
        if (s.length() < 2) {
            return "0" + s;
        }
        return s;
    }

    public void getEvents(@NonNull Fragment f, String authToken, int d, int m, int y, ConstraintLayout l) {
        String data = padStart(String.valueOf(m)) + "-" + padStart(String.valueOf(y)) + "-" + d;

        if (authToken != null && !authToken.isEmpty()) {
            RetrofitClient.getInstance().setAccessToken(authToken);
        }

        // 1. Organized Events
        RecyclerView orgRv = l.findViewById(R.id.organizer_recycler_view);
        orgRv.setLayoutManager(new LinearLayoutManager(l.getContext(), LinearLayoutManager.VERTICAL, false));
        orgRv.setAdapter(new OrgEvAdapter(new EventCallback()));

        OrganizedEventRepository orgRepo = getOrgRepo(f, orgRv, data);
        orgRepo.getOrganizedEventsByDate(data);

        // 2. Personal Calendar Private Events
        RecyclerView privRv = l.findViewById(R.id.personal_recycler_view);
        privRv.setLayoutManager(new LinearLayoutManager(l.getContext(), LinearLayoutManager.VERTICAL, false));
        privRv.setAdapter(new PrivEvAdapter(new EventCallback()));

        JsonCallback privCallback = new JsonCallback(f, "priv", privRv, data);
        EventRepository eventRepo = new EventRepository();
        eventRepo.getPersonalCalendarEvents(data, new EventRepository.EventDataCallback() {
            @Override
            public void onSuccess(JsonObject json) {
                privCallback.handleJsonSuccess(json);
            }

            @Override
            public void onError(int statusCode, String errorMessage) {
                privCallback.handleJsonError(statusCode);
            }
        });
    }

    @NonNull
    private static OrganizedEventRepository getOrgRepo(@NonNull Fragment f, RecyclerView orgRv, String data) {
        JsonCallback orgCallback = new JsonCallback(f, "org", orgRv, data);
        OrganizedEventRepository orgRepo = new OrganizedEventRepository(new OrganizedEventRepository.EventListCallback() {
            @Override
            public void onSuccess(JsonObject eventsJson) {
                orgCallback.handleJsonSuccess(eventsJson);
            }

            @Override
            public void onError(int statusCode, String errorMessage) {
                orgCallback.handleJsonError(statusCode);
            }
        });
        return orgRepo;
    }
}