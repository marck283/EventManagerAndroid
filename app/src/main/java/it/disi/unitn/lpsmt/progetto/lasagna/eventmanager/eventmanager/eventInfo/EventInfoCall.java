package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo;

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
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.spinnerImplementation.SpinnerArrayAdapter;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.EventDetailsFragment;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.spinnerImplementation.SpinnerOnItemSelectedListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventInfoCall {
    private final EventInfoInterface evInterface;

    public EventInfoCall() {
        Retrofit retro = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://eventmanagerzlf.herokuapp.com")
                .build();
        evInterface = retro.create(EventInfoInterface.class);
    }

    public void getEventInfo(@NonNull String which, @NonNull String eventId, @NonNull View v, @NonNull EventDetailsFragment f) {
        Call<JsonObject> call = evInterface.getEventInfo(eventId);
        call.enqueue(new Callback<>() {

            /**
             * Invoked for a received HTTP response.
             *
             * <p>Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call {@link Response#isSuccessful()} to determine if the response indicates success.
             *
             * @param call
             * @param response
             */
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null && response.isSuccessful()) {
                    EventInfo ei = new EventInfo();
                    Log.i("responseBody", String.valueOf(response.body()));
                    final EventInfo ei1 = ei.parseJSON(response.body());

                    //Ora imposta il layout in base alla schermata visualizzata
                    ImageView imgView = v.findViewById(R.id.eventPicture);
                    imgView.setImageBitmap(ei1.decodeBase64());

                    TextView title = v.findViewById(R.id.title);
                    title.setText(ei1.getNomeAtt());

                    TextView organizerName = v.findViewById(R.id.organizerName);
                    organizerName.setText(f.getString(R.string.organizer, ei1.getOrgName()));

                    TextView durata = v.findViewById(R.id.duration);
                    durata.setText(f.getString(R.string.duration, ei1.getDurata()));

                    f.setEventId(ei1.getId());

                    ArrayList<CharSequence> dateArr = new ArrayList<>();
                    dateArr.add("---");
                    dateArr.addAll(ei1.getLuoghi());

                    TextInputLayout spinner = v.findViewById(R.id.spinner), spinner1 = v.findViewById(R.id.dateArray);

                    MaterialAutoCompleteTextView textView = spinner.findViewById(R.id.actv),
                            textView1 = spinner1.findViewById(R.id.actv1);

                    SpinnerOnItemSelectedListener ois2 = new SpinnerOnItemSelectedListener();

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
                                orariArr.addAll(ei1.getOrari(s.toString()));
                                textView1.setAdapter(new SpinnerArrayAdapter(f.requireContext(), R.layout.list_item, orariArr));
                                textView1.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                        //Nothing
                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s1, int start, int before, int count) {
                                        if(!s.toString().equals("---")) {
                                            f.setTime(s1.toString());

                                            indirizzo.setOnClickListener(c -> {
                                                //Questo dovrebbe aprire Google Maps (vedi report progetto per sapere come fare)
                                            });

                                            LuogoEvento le = ei1.getLuogo(s.toString(), s1.toString());
                                            if(le != null) {
                                                indirizzo.setText(f.getString(R.string.event_address, le.toString()));
                                            }

                                            try {
                                                SimpleDateFormat sdformat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
                                                boolean over = false;
                                                Button b = v.findViewById(R.id.sign_up_button);

                                                if(le != null) {
                                                    Date toCheck = sdformat.parse(le.getData());
                                                    Date d = new Date();
                                                    if (toCheck != null && sdformat.format(d).compareTo(sdformat.format(toCheck)) > 0) {
                                                        over = true;
                                                    }
                                                    if (over || le.getPostiRimanenti() == 0) {
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
                }
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected exception
             * occurred creating the request or processing the response.
             *
             * @param call
             * @param t
             */
            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                try {
                    throw t;
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
