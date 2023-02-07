package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.queryClasses;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.organizedEvent.OrganizedEvent;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.Event;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventAdapter;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.LuogoEv;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.DAOs.OrgEvDAO;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.localDatabase.entities.OrgEvent;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.organizedEvents.OrgEvAdapter;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.EventDetailsFragment;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.spinnerImplementation.SpinnerArrayAdapter;

public class DBOrgEvents extends DBThread {

    private final Fragment f;

    private final OrgEvDAO orgEvDAO;

    private final String action, eventId;

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
        eventId = null;

        RecyclerView.LayoutManager lm = new LinearLayoutManager(f.requireContext(), LinearLayoutManager.VERTICAL,
                false);
        this.recView.setLayoutManager(lm);

        v = null;
    }

    public DBOrgEvents(@NonNull Fragment f, @NonNull View v, @NonNull String eventId) {
        super(f.requireActivity());
        orgEvDAO = db.getOrgEvDAO();
        this.f = f;
        this.action = "getEvent";
        this.recView = null;
        events = null;
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
            EventAdapter p1 = new OrgEvAdapter(f, new EventCallback(), helpList);
            p1.submitList(helpList);
            recView.setAdapter(p1);
        });
    }

    private void startGoogleMaps(@NonNull EventDetailsFragment f, @NonNull TextView indirizzo,
                                 @NonNull List<Address> addresses) {
        Address address = addresses.get(0);

        Uri gmURI = Uri.parse("geo:" + address.getLatitude() + "," + address.getLongitude()
                + "?q=" + indirizzo.getText().toString());
        Intent i = new Intent(Intent.ACTION_VIEW, gmURI);
        i.setPackage("com.google.android.apps.maps");

        f.requireActivity().startActivity(i);
    }

    private void noSuchAddressDialog(@NonNull Fragment f) {
        AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
        dialog.setTitle(R.string.no_such_address);
        dialog.setMessage(f.getString(R.string.address_not_registered));
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
        dialog.show();
    }

    private void getEventById() {
        OrgEvent event = orgEvDAO.getOrgEvent(eventId);

        f.requireActivity().runOnUiThread(() -> {
            ImageView iView = v.findViewById(R.id.imageView3);
            Bitmap bm = event.decodeBase64();
            if(bm != null) {
                Glide.with(v).load(bm).into(iView);
            }

            //Recuperare anche le altre informazioni sull'evento in questione
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
                    if(evDay.getEditText() != null &&
                            !evDay.getEditText().getText().toString().equals("---")) {
                        TextView address = v.findViewById(R.id.textView15);
                        final int pos = dayArr.indexOf(evDay.getEditText().getText().toString());

                        address.setText(f.getString(R.string.event_address, event.getLuogoEv()
                                .get(pos - 1).getAddress()));
                        address.setOnClickListener(c -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                Geocoder geocoder = new Geocoder(f.requireActivity());
                                geocoder.getFromLocationName(address.getText().toString(), 5, addresses -> {
                                    if (addresses.size() > 0) {
                                        if(f instanceof EventDetailsFragment) {
                                            startGoogleMaps((EventDetailsFragment) f, address, addresses);
                                        }
                                    } else {
                                        f.requireActivity().runOnUiThread(() -> noSuchAddressDialog(f));
                                    }
                                });
                            } else {
                                Thread t1 = new Thread() {
                                    public void run() {
                                        List<Address> addresses;
                                        try {
                                            Geocoder geocoder = new Geocoder(f.requireActivity());
                                            addresses = geocoder.getFromLocationName(address.getText().toString(), 5);
                                            if (addresses != null && addresses.size() > 0) {
                                                if(f instanceof EventDetailsFragment) {
                                                    startGoogleMaps((EventDetailsFragment) f, address, addresses);
                                                }
                                            } else {
                                                f.requireActivity().runOnUiThread(() -> noSuchAddressDialog(f));
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                t1.start();
                            }
                        });
                        address.setPaintFlags(address.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                        TextInputLayout spinner = v.findViewById(R.id.spinner);
                        MaterialAutoCompleteTextView hourTextView = spinner.findViewById(R.id.orgHourTextView);

                        ArrayList<CharSequence> hourArr = new ArrayList<>();
                        hourArr.add("---");
                        for (LuogoEv l : event.getLuogoEv()) {
                            hourArr.add(l.getOra());
                        }

                        hourTextView.setAdapter(new SpinnerArrayAdapter(f.requireContext(),
                                R.layout.list_item, hourArr));

                        Button qrCodeScan = v.findViewById(R.id.button8);
                        qrCodeScan.setEnabled(false);
                        AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
                        dialog.setTitle(R.string.no_connection);
                        dialog.setMessage(f.getString(R.string.no_connection_message));
                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                        dialog.show();
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
            case "getEvent": {
                getEventById();
                break;
            }
        }
        close();
    }
}
