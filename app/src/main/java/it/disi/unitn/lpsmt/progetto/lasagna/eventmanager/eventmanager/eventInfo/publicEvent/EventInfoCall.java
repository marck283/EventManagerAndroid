package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.publicEvent;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.spinnerImplementation.SpinnerArrayAdapter;
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

    private void startGoogleMaps(@NonNull EventDetailsFragment f, @NonNull TextView indirizzo,
                                 @NonNull List<Address> addresses, @NonNull View v) {
        Address address = addresses.get(0);

        Uri gmURI = Uri.parse("geo:" + address.getLatitude() + "," + address.getLongitude()
                + "?q=" + indirizzo.getText().toString());
        Intent i = new Intent(Intent.ACTION_VIEW, gmURI);
        i.setPackage("com.google.android.apps.maps");

        f.requireActivity().startActivity(i);

        /*Bundle b = new Bundle();
        b.putDouble("lat", addresses.get(0).getLatitude());
        b.putDouble("lng", addresses.get(0).getLongitude());
        f.requireActivity().runOnUiThread(() ->
        Navigation.findNavController(v).navigate(R.id.action_eventDetailsFragment_to_mapsFragment, b));*/
    }

    private void noSuchAddressDialog(@NonNull Fragment f) {
        AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
        dialog.setTitle(R.string.no_such_address);
        dialog.setMessage(f.getString(R.string.address_not_registered));
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
        dialog.show();
    }

    public void getEventInfo(@NonNull String which, @NonNull String eventId, @NonNull View v,
                             @NonNull EventDetailsFragment f, @Nullable String userJwt) {
        if ("pub".equals(which)) {
            Call<JsonObject> call = evInterface.getPubEventInfo(eventId);
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
                    if (response.body() != null && response.isSuccessful() && f.isAdded()) {
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
                                    orariArr.addAll(ei1.getOrari(s.toString()));
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
                                                    Geocoder geocoder = new Geocoder(f.requireActivity());
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                                        geocoder.getFromLocationName(indirizzo.getText().toString(), 5, addresses -> {
                                                            if (addresses.size() > 0) {
                                                                startGoogleMaps(f, indirizzo, addresses, v);
                                                            } else {
                                                                noSuchAddressDialog(f);
                                                            }
                                                        });
                                                    } else {
                                                        Thread t1 = new Thread() {
                                                            public void run() {
                                                                List<Address> addresses;
                                                                try {
                                                                    addresses = geocoder.getFromLocationName(indirizzo.getText().toString(), 5);
                                                                    if (addresses != null && addresses.size() > 0) {
                                                                        startGoogleMaps(f, indirizzo, addresses, v);
                                                                    } else {
                                                                        Looper.prepare();
                                                                        noSuchAddressDialog(f);
                                                                        Looper.loop();
                                                                        Looper.myLooper().quitSafely();
                                                                    }
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        };
                                                        t1.start();
                                                    }
                                                });

                                                LuogoEvento le = ei1.getLuogo(s.toString(), s1.toString());
                                                if (le != null) {
                                                    indirizzo.setPaintFlags(indirizzo.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                                    indirizzo.setText(f.getString(R.string.event_address, le.toString()));
                                                }

                                                try {
                                                    SimpleDateFormat sdformat = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
                                                    boolean over = false;
                                                    Button b = v.findViewById(R.id.sign_up_button);

                                                    if (le != null) {
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
}
