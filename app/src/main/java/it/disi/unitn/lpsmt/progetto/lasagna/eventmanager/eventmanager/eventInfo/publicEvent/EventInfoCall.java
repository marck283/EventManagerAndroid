package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.publicEvent;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.GeocoderExt;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.LuogoEv;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.networkRequests.NetworkRequest;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.spinnerImplementation.SpinnerArrayAdapter;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.EventDetailsFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class EventInfoCall extends Thread {

    private final NetworkRequest request;

    private final String eventId;

    private final View v;

    private final EventDetailsFragment f;

    public EventInfoCall(@NonNull String eventId, @NonNull View v, @NonNull EventDetailsFragment f) {
        request = new NetworkRequest();
        this.eventId = eventId;
        this.v = v;
        this.f = f;
    }

    public void run() {
        Request req = request.getRequest(new ArrayList<>(),
                "https://eventmanagerzlf.herokuapp.com/api/v2/EventiPubblici/" + eventId);
        request.enqueue(req, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                try {
                    throw e;
                } catch(Throwable e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() != null && response.isSuccessful()) {
                    EventInfo ei = new EventInfo();

                    Gson gson = new GsonBuilder().create();
                    final EventInfo ei1 = ei.parseJSON(gson.fromJson(response.body().string(), JsonObject.class));

                    Activity activity = f.getActivity();

                    //E riportate qui
                    if(activity != null && f.isAdded()) {
                        f.requireActivity().runOnUiThread(() -> {
                            //Ora imposta il layout in base alla schermata visualizzata
                            ImageView imgView = v.findViewById(R.id.eventPicture);
                            imgView.setImageBitmap(ei1.decodeBase64());

                            TextView title = v.findViewById(R.id.title);
                            title.setText(ei1.getNomeAtt());

                            TextView organizerName = v.findViewById(R.id.organizerName);
                            organizerName.setText(f.getString(R.string.organizer, ei1.getOrgName()));

                            TextView durata = v.findViewById(R.id.duration);
                            String[] durataArr = ei1.getDurata().split(":");
                            durata.setText(f.getString(R.string.duration, durataArr[0], durataArr[1], durataArr[2]));

                            f.setEventId(ei1.getId());

                            ArrayList<CharSequence> dateArr = new ArrayList<>();
                            dateArr.add("---");
                            dateArr.addAll(ei1.getLuoghi());

                            TextInputLayout spinner = v.findViewById(R.id.spinner), spinner1 = v.findViewById(R.id.dateArray);

                            MaterialAutoCompleteTextView textView = spinner.findViewById(R.id.actv),
                                    textView1 = spinner1.findViewById(R.id.actv1);

                            textView.setAdapter(new SpinnerArrayAdapter(f.requireContext(), R.layout.list_item, dateArr));

                            textView.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    //Nothing
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    TextView indirizzo = v.findViewById(R.id.event_address);

                                    if (!s.toString().equals("---")) {
                                        Log.i("item", s.toString());
                                        f.setDay(s.toString());
                                        f.setTime("");

                                        indirizzo.setText(f.getString(R.string.event_address, ""));

                                        ArrayList<CharSequence> orariArr = new ArrayList<>();
                                        orariArr.add("---");

                                        String[] dataArr = s.toString().split("/");
                                        String data = String.join("-", dataArr[1], dataArr[0], dataArr[2]);
                                        orariArr.addAll(ei1.getOrari(data));
                                        textView1.setAdapter(new SpinnerArrayAdapter(f.requireContext(), R.layout.list_item, orariArr));
                                        textView1.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                                //Nothing
                                            }

                                            @Override
                                            public void onTextChanged(CharSequence s1, int start, int before, int count) {
                                                if (!s.toString().equals("---")) {
                                                    f.setTime(s1.toString());

                                                    indirizzo.setOnClickListener(c -> {
                                                        GeocoderExt geocoder = new GeocoderExt(f, indirizzo);
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                                            geocoder.fromLocationName(indirizzo.getText().toString(), 5);
                                                        } else {
                                                            geocoder.fromLocationNameThread(indirizzo.getText().toString(), 5);
                                                        }
                                                    });

                                                    LuogoEv le = ei1.getLuogo(data, s1.toString());
                                                    if (le != null) {
                                                        indirizzo.setPaintFlags(indirizzo.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                                        indirizzo.setText(f.getString(R.string.event_address, le.getAddress()));
                                                    }

                                                    try {
                                                        SimpleDateFormat sdformat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
                                                        Button b = v.findViewById(R.id.sign_up_button);

                                                        if (le != null) {
                                                            Date toCheck = sdformat.parse(le.getData());
                                                            Date d = new Date();
                                                            if (le.getTerminato() || (toCheck != null &&
                                                                    sdformat.format(d).compareTo(sdformat.format(toCheck)) > 0)
                                                                    || le.getPostiRimanenti() == 0) {
                                                                b.setEnabled(false);
                                                                b.setText(f.getString(R.string.registrations_closed));
                                                            } else {
                                                                b.setEnabled(true);
                                                            }
                                                        } else {
                                                            b.setEnabled(false);
                                                        }
                                                    } catch (ParseException ex) {
                                                        Log.i("ParseException", "ParseException");
                                                    }
                                                }
                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {
                                                //Nothing
                                            }
                                        });
                                    } else {
                                        textView1.setAdapter(new SpinnerArrayAdapter(f.requireContext(), R.layout.list_item, new ArrayList<>()));
                                        textView1.setText("");
                                        indirizzo.setText(f.getString(R.string.event_address, ""));
                                    }
                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    //Nothing
                                }
                            });
                        });
                    }
                }
            }
        });
    }
}
