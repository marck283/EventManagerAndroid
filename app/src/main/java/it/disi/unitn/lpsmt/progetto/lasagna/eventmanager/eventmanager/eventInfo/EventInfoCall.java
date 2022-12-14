package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.spinnerImplementation.SpinnerItemList;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.EventDetailsFragment;
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
                    organizerName.setText(f.getString(R.string.organizer, ei1.getOrgName()));

                    TextView durata = v.findViewById(R.id.duration);
                    durata.setText(f.getString(R.string.duration, ei1.getDurata()));

                    f.setEventId(ei1.getId());

                    ArrayList<CharSequence> dateArr = new ArrayList<>();
                    dateArr.add("---");
                    dateArr.addAll(ei1.getLuoghi());

                    SpinnerItemList spinner = v.findViewById(R.id.spinner), spinner1 = v.findViewById(R.id.dateArray);

                    //Verificare se queste chiamate a setAdapter() siano corrette e, nel caso, cambiarle con l'altra versione
                    //di setAdapter().
                    spinner.setAdapter(f,
                            android.R.layout.simple_spinner_dropdown_item, R.id.spinner, dateArr);

                    spinner.getListener().getItem().observe(f.getViewLifecycleOwner(), o -> {
                        if(o instanceof String && !o.equals("---")) {
                            f.setDay((String) o);
                            f.setTime("");
                            ArrayList<CharSequence> orariArr = new ArrayList<>();
                            orariArr.add("---");
                            orariArr.addAll(ei1.getOrari((String) o));
                            spinner1.setAdapter(f, android.R.layout.simple_spinner_dropdown_item, R.id.dateArray, orariArr);
                            spinner1.getListener().getItem().observe(f.getViewLifecycleOwner(), o1 -> {
                                if(o1 instanceof String && !o1.equals("---")) {
                                    f.setTime((String) o1);
                                    LuogoEvento le = ei1.getLuogo((String) o, (String) o1);
                                    TextView indirizzo = v.findViewById(R.id.event_address);
                                    indirizzo.setText(f.getString(R.string.event_address, le.toString()));
                                    indirizzo.setOnClickListener(c -> {
                                        //Questo dovrebbe aprire Google Maps (vedi report progetto per sapere come fare)
                                    });

                                    try {
                                        SimpleDateFormat sdformat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
                                        boolean over = false;
                                        Date toCheck = sdformat.parse(le.getData());
                                        Date d = new Date();
                                        if(toCheck != null && sdformat.format(d).compareTo(sdformat.format(toCheck)) > 0) {
                                            over = true;
                                        }

                                        if(over || le.getPostiRimanenti() == 0) {
                                            Button b = v.findViewById(R.id.sign_up_button);
                                            b.setEnabled(false);
                                            b.setText(f.getString(R.string.registrations_closed));
                                        }
                                    } catch(ParseException ex) {
                                        Log.i("ParseException", "ParseException");
                                    }
                                }
                            });
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
