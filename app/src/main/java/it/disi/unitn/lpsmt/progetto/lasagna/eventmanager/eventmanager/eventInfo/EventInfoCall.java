package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.listeners.SpinnerOnItemSelectedListener;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.EventDetailsFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventInfoCall {
    private EventInfoInterface evInterface;

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
                if(response.body() != null && response.isSuccessful()) {
                    EventInfo ei = new EventInfo();
                    Log.i("responseBody", String.valueOf(response.body()));
                    final EventInfo ei1 = ei.parseJSON(response.body());

                    //Ora imposta il layout in base alla schermata visualizzata
                    ImageView imgView = v.findViewById(R.id.eventPicture);
                    imgView.setImageBitmap(ei1.decodeBase64());

                    TextView title = v.findViewById(R.id.title);
                    title.setText(ei1.getNomeAtt());

                    TextView organizerName = v.findViewById(R.id.organizerName);
                    organizerName.setText(ei1.getOrgName());

                    TextView durata = v.findViewById(R.id.duration);
                    durata.setText(String.valueOf(ei1.getDurata()));

                    ArrayList<String> dateArr = ei1.getLuoghi();
                    ArrayAdapter<CharSequence> ad = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_dropdown_item);
                    ad.addAll(dateArr);

                    Spinner spinner = v.findViewById(R.id.spinner), spinner1 = v.findViewById(R.id.dateArray);
                    spinner.setAdapter(ad);

                    SpinnerOnItemSelectedListener l = new SpinnerOnItemSelectedListener(), l1 = new SpinnerOnItemSelectedListener();
                    spinner.setOnItemSelectedListener(l);
                    l.getItem().observe(f.getViewLifecycleOwner(), o -> {
                        ArrayList<String> orariArr = ei1.getOrari((String) o);
                        ArrayAdapter<CharSequence> ad1 = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_dropdown_item);
                        ad1.addAll(orariArr);
                        spinner1.setAdapter(ad1);
                        spinner1.setOnItemSelectedListener(l1);
                        l1.getItem().observe(f.getViewLifecycleOwner(), o1 -> {
                            LuogoEvento le = ei1.getLuogo((String) o, (String) o1);
                            TextView indirizzo = v.findViewById(R.id.event_address);
                            indirizzo.setText(le.toString());
                            indirizzo.setOnClickListener(c -> {
                                //Questo dovrebbe aprire Google Maps (vedi report progetto per sapere come fare)
                            });

                            try {
                                SimpleDateFormat sdformat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
                                boolean over = false;
                                Date toCheck = sdformat.parse(le.getData());

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    LocalDateTime ldt = LocalDateTime.now();
                                    if(toCheck != null && sdformat.format(ldt).compareTo(sdformat.format(toCheck)) > 0) {
                                        over = true;
                                    }
                                } else {
                                    Date d = new Date();
                                    String formattedDate = sdformat.format(d);
                                    if(toCheck != null && formattedDate.compareTo(sdformat.format(toCheck)) > 0) {
                                        over = true;
                                    }
                                }

                                if(over || le.getPostiRimanenti() == 0) {
                                    Button b = v.findViewById(R.id.sign_up_button);
                                    b.setEnabled(false);
                                    b.setText(f.getString(R.string.registrations_closed));
                                }
                            } catch(ParseException ex) {
                                Log.i("ParseException", "ParseException");
                            }
                        });
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
