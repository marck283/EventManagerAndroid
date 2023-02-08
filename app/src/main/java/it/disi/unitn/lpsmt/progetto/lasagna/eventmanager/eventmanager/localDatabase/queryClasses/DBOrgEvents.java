package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses;

import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.organizedEvent.OrganizedEvent;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.Event;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventAdapter;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.DAOs.OrgEvDAO;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.entities.OrgEvent;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.organizedEvents.OrgEvAdapter;

public class DBOrgEvents extends DBThread {

    private final Fragment f;

    private final OrgEvDAO orgEvDAO;

    private final String action, eventName;

    private final List<Event> events; //Lista degli eventi organizzati da un utente

    private final RecyclerView recView;

    public DBOrgEvents(@NonNull Fragment f, @NonNull String action, @NonNull List<Event> events,
                       @NonNull RecyclerView recView) {
        super(f.requireActivity());
        this.f = f;
        this.action = action;
        orgEvDAO = db.getOrgEvDAO();
        this.events = events;
        this.recView = recView;
        eventName = null;
    }

    public DBOrgEvents(@NonNull Fragment f, @NonNull String action, @NonNull RecyclerView recView) {
        super(f.requireActivity());
        orgEvDAO = db.getOrgEvDAO();
        this.f = f;
        this.action = action;
        this.recView = recView;
        events = null;

        RecyclerView.LayoutManager lm = new LinearLayoutManager(f.requireContext(), LinearLayoutManager.VERTICAL,
                false);
        this.recView.setLayoutManager(lm);
        eventName = null;
    }

    public DBOrgEvents(@NonNull Fragment f, @NonNull String action, @NonNull String eventName,
                       @NonNull RecyclerView recView) {
        super(f.requireActivity());
        orgEvDAO = db.getOrgEvDAO();
        this.f = f;
        this.action = action;
        this.recView = recView;
        events = null;

        RecyclerView.LayoutManager lm = new LinearLayoutManager(f.requireContext(), LinearLayoutManager.VERTICAL,
                false);
        this.recView.setLayoutManager(lm);
        this.eventName = eventName;
    }

    private void updateAll() {
        orgEvDAO.deleteAll();

        for(Event e: events) {
            OrgEvent event = new OrgEvent(e.getEventType(), e.getId(),
                    e.getSelf(), e.getEventName(),
                    e.getCategory(), e.getEventPic(),
                    e.getOrgName(), e.getLuogoEv(), e.getDurata());
            orgEvDAO.insert(event);
        }
    }

    private void getAll() {
        List<OrgEvent> evList = orgEvDAO.getAllOrgEvents();

        if(evList.size() == 0) {
            f.requireActivity().runOnUiThread(() -> {
                AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
                dialog.setTitle(R.string.no_org_event);
                dialog.setMessage(f.getString(R.string.no_org_event_message));
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                dialog.show();
            });
            return;
        }

        //Mostra tutti gli eventi nella tabella degli eventi organizzati dall'utente
        List<Event> helpList = new ArrayList<>();
        for(OrgEvent o: evList) {
            helpList.add(new OrganizedEvent(o.getEventType(), o.getIdevent(), o.getSelf(), o.getName(),
                    o.getCategory(), o.getEventPic(), o.getOrgName(), o.getLuogoEv(), o.getDurata()));
        }
        f.requireActivity().runOnUiThread(() -> {
            recView.invalidate();
            EventAdapter p1 = new OrgEvAdapter(f, new EventCallback(), helpList);
            p1.submitList(helpList);
            recView.setAdapter(p1);
        });
    }

    private void getEventsByName() {
        //Mostra tutti gli eventi ottenuti cercando per nome
        List<OrgEvent> orgEvList = orgEvDAO.getOrgEventsByName(eventName);

        if(orgEvList.size() == 0) {
            f.requireActivity().runOnUiThread(() -> {
                AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
                dialog.setTitle(R.string.no_org_event);
                dialog.setMessage(f.getString(R.string.no_org_event_message));
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                dialog.show();
            });
            return;
        }

        List<Event> helpList = new ArrayList<>();
        for(OrgEvent o: orgEvList) {
            helpList.add(new OrganizedEvent(o.getEventType(), o.getIdevent(), o.getSelf(), o.getName(),
                    o.getCategory(), o.getEventPic(), o.getOrgName(), o.getLuogoEv(), o.getDurata()));
        }
        f.requireActivity().runOnUiThread(() -> {
            EventAdapter p1 = new OrgEvAdapter(f, new EventCallback(), helpList);
            p1.submitList(helpList);
            recView.setAdapter(p1);
        });
    }

    public void run() {
        //Azioni da implementare:
        //1) eliminare evento al suo annullamento.

        switch(action) {
            case "updateAll": {
                updateAll();
                break;
            }
            case "getAll": {
                getAll();
                break;
            }
            case "getEventsByName": {
                getEventsByName();
                break;
            }
        }
        close();
    }
}
