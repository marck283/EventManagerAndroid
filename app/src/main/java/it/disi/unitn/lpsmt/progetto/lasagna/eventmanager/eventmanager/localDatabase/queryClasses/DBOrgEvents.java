package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.GeocoderExt;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.organizedEvent.OrganizedEvent;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.Event;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventAdapter;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.LuogoEv;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.daos.OrgEvDAO;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.entities.OrgEvent;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.organizedEvents.OrgEvAdapter;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.spinnerImplementation.SpinnerArrayAdapter;

public class DBOrgEvents extends DBThread {

    private final Fragment f;

    private final OrgEvDAO orgEvDAO;

    private final String action, eventName, eventId;

    private final List<Event> events; //Lista degli eventi organizzati da un utente

    private final RecyclerView recView;

    private final View v;

    public DBOrgEvents(@NonNull Fragment f, @NonNull String action, @NonNull List<Event> events,
                       @NonNull RecyclerView recView) {
        super(f.requireActivity());
        this.f = f;
        this.action = action;
        orgEvDAO = db.getOrgEvDAO();
        this.events = events;
        this.recView = recView;
        eventName = null;
        eventId = null;
        v = null;
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
        eventId = null;
        v = null;
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
        eventId = null;
        v = null;
    }

    public DBOrgEvents(@NonNull Fragment f, @NonNull String eventId, @NonNull View v) {
        super(f.requireActivity());
        orgEvDAO = db.getOrgEvDAO();
        this.f = f;
        action = "getEventById";
        recView = null;
        events = null;
        eventName = null;
        this.eventId = eventId;
        this.v = v;
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
            EventAdapter p1 = new OrgEvAdapter(new EventCallback(), helpList);
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
                dialog.setMessage(f.getString(R.string.no_org_event_with_this_name));
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
            EventAdapter p1 = new OrgEvAdapter(new EventCallback(), helpList);
            p1.submitList(helpList);
            recView.setAdapter(p1);
        });
    }

    private void getEventById() {
        OrgEvent event = orgEvDAO.getOrgEventById(eventId);

        Activity activity = f.getActivity();
        if (activity != null && f.isAdded()) {
            f.requireActivity().runOnUiThread(() -> {
                ImageView iView = v.findViewById(R.id.imageView3);
                Bitmap bm = event.decodeBase64();
                if (bm != null) {
                    Glide.with(v).load(bm).into(iView);
                }

                TextView evName = v.findViewById(R.id.textView6);
                evName.setText(f.getString(R.string.info_on_event, event.getName()));

                TextInputLayout evDay = v.findViewById(R.id.spinner2);
                MaterialAutoCompleteTextView dayText = evDay.findViewById(R.id.orgDateTextView);

                ArrayList<CharSequence> dayArr = new ArrayList<>();
                dayArr.add("---");
                for (LuogoEv l : event.getLuogoEv()) {
                    String[] dateArr = l.getData().split("-");
                    dayArr.add(dateArr[1] + "/" + dateArr[0] + "/" + dateArr[2]);
                }

                dayText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (evDay.getEditText() != null &&
                                !evDay.getEditText().getText().toString().equals("---")) {
                            TextInputLayout spinner = v.findViewById(R.id.spinner);
                            MaterialAutoCompleteTextView hourTextView = spinner.findViewById(R.id.orgHourTextView);

                            ArrayList<CharSequence> hourArr = new ArrayList<>();
                            hourArr.add("---");

                            String[] dayArr = evDay.getEditText().getText().toString().split("/");
                            String day = dayArr[1] + "-" + dayArr[0] + "-" + dayArr[2];
                            for (LuogoEv l : event.getOrari(day)) {
                                hourArr.add(l.getOra());
                            }

                            hourTextView.setText("");

                            hourTextView.setAdapter(new SpinnerArrayAdapter(f.requireContext(),
                                    R.layout.list_item, hourArr));

                            hourTextView.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    TextView address = v.findViewById(R.id.textView15);
                                    if(hourTextView.getText() != null && !hourTextView.getText().toString().equals("") &&
                                    !hourTextView.getText().toString().equals("---")) {
                                        Button qrCodeScan = v.findViewById(R.id.button8),
                                                terminaEvento = v.findViewById(R.id.button12);
                                        String[] day1 = dayText.getText().toString().split("/");

                                        if (day1.length > 1) {
                                            String day2 = day1[1] + "-" + day1[0] + "-" + day1[2];

                                            LuogoEv luogo = event.getLuogo(day2,
                                                    hourTextView.getText().toString());
                                            if(luogo == null) {
                                                f.requireActivity().runOnUiThread(() -> {
                                                    AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
                                                    dialog.setTitle(R.string.error);
                                                    dialog.setMessage(f.getString(R.string.no_org_event_at_this_time));
                                                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                                            (dialog1, which) -> dialog1.dismiss());
                                                    dialog.show();
                                                });
                                                return;
                                            }

                                            qrCodeScan.setEnabled(false);
                                            terminaEvento.setEnabled(false);

                                            address.setText(f.getString(R.string.event_address,
                                                    luogo.getAddress()));
                                            address.setOnClickListener(c -> {
                                                GeocoderExt geocoder = new GeocoderExt(f, address);
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                                    geocoder.fromLocationName(address.getText().toString(), 5);
                                                } else {
                                                    geocoder.fromLocationNameThread(address.getText().toString(), 5);
                                                }
                                            });
                                            address.setPaintFlags(address.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                        }
                                    } else {
                                        address.setText(f.getString(R.string.event_address, ""));
                                    }
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                        } else {
                            TextInputLayout hour = v.findViewById(R.id.spinner);
                            MaterialAutoCompleteTextView hourTextView = hour.findViewById(R.id.orgHourTextView);
                            hourTextView.setAdapter(new SpinnerArrayAdapter(f.requireContext(),
                                    R.layout.list_item, new ArrayList<>()));
                            hourTextView.setText("");

                            TextView address = v.findViewById(R.id.textView15);
                            address.setText(f.getString(R.string.event_address, ""));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                dayText.setAdapter(new SpinnerArrayAdapter(f.requireContext(), R.layout.list_item, dayArr));

                TextView duration = v.findViewById(R.id.textView12);
                String[] durata = event.getDurata().split(":");
                duration.setText(f.getString(R.string.duration, durata[0], durata[1], durata[2]));
            });
        }
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
            case "getEventById": {
                getEventById();
                break;
            }
        }
        close();
    }
}
