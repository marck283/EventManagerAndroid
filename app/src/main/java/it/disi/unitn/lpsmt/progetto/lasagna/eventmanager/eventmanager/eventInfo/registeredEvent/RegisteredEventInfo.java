package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.registeredEvent;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.publicEvent.LuogoEvento;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.EventDetailsFragment;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.qr_code_scan.QRCodeRenderingFragment;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisteredEventInfo extends Thread {
    private final OkHttpClient client;

    private final String userJwt, eventId, data;

    private final EventDetailsFragment f;

    private final View v;

    private final ActivityResultLauncher<Intent> loginLauncher, loginLauncherTicket;

    public RegisteredEventInfo(@NonNull String userJwt, @NonNull String eventId, @NonNull EventDetailsFragment f,
                               @NonNull View v, @NonNull String data,
                               @NonNull ActivityResultLauncher<Intent> loginLauncher,
                               @NonNull ActivityResultLauncher<Intent> loginLauncherTicket) {
        client = new OkHttpClient();
        this.userJwt = userJwt;
        this.eventId = eventId;
        this.f = f;
        this.v = v;
        this.data = data;
        this.loginLauncher = loginLauncher;
        this.loginLauncherTicket = loginLauncherTicket;
    }

    private void setAlertDialog(@StringRes int title, @StringRes int message) {
        f.requireActivity().runOnUiThread(() -> {
            AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
            dialog.setTitle(title);
            dialog.setMessage(f.getString(message));
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
            dialog.show();
        });
    }

    private void noSuchAddressDialog(@NonNull Fragment f) {
        AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
        dialog.setTitle(R.string.no_such_address);
        dialog.setMessage(f.getString(R.string.address_not_registered));
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
        dialog.show();
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

    public void run() {
        Request request = new Request.Builder()
                .addHeader("x-access-token", userJwt)
                .addHeader("data", data)
                .url("https://eventmanagerzlf.herokuapp.com/api/v2/InfoEventoIscr/" + eventId)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                try {
                    throw e;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() != null) {
                    switch(response.code()) {
                        case 200: {
                            Gson gson = new GsonBuilder().create();

                            String body = response.body().string();
                            RegisteredEvent event = RegisteredEvent.parseJSON(gson.fromJson(body, JsonObject.class));

                            if (f.isAdded()) {
                                f.requireActivity().runOnUiThread(() -> {
                                    ImageView image = v.findViewById(R.id.eventPicture);
                                    Glide.with(v).load(event.decodeBase64()).into(image);


                                    TextView title = v.findViewById(R.id.title);
                                    title.setText(event.getEventName());

                                    TextView organizzatore = v.findViewById(R.id.textView16);
                                    organizzatore.setText(f.getString(R.string.organizer, event.getOrgName()));

                                    TextView giorno = v.findViewById(R.id.textView11);
                                    String[] dataArr = event.getLuogoEv().getData().split("-");
                                    giorno.setText(f.getString(R.string.day_not_selectable,
                                            "\n" + dataArr[1] + "/" + dataArr[0] + "/" + dataArr[2]));

                                    TextView ora = v.findViewById(R.id.textView20);
                                    String oraS = event.getLuogoEv().getOra();
                                    ora.setText(f.getString(R.string.time_not_selectable,
                                            "\n" + oraS));

                                    String[] sDurata = event.getDurata().split(":");
                                    TextView durata = v.findViewById(R.id.textView39);
                                    durata.setText(f.getString(R.string.duration, sDurata[0], sDurata[1], sDurata[2]));

                                    TextView address = v.findViewById(R.id.textView42);
                                    address.setText(f.getString(R.string.event_address, event.getLuogoEv().toString()));
                                    address.setOnClickListener(c -> {
                                        Geocoder geocoder = new Geocoder(f.requireActivity());
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                            geocoder.getFromLocationName(address.getText().toString(), 5, addresses -> {
                                                if (addresses.size() > 0) {
                                                    startGoogleMaps(f, address, addresses);
                                                } else {
                                                    noSuchAddressDialog(f);
                                                }
                                            });
                                        } else {
                                            Thread t1 = new Thread() {
                                                public void run() {
                                                    List<Address> addresses;
                                                    try {
                                                        addresses = geocoder.getFromLocationName(address.getText().toString(), 5);
                                                        if (addresses != null && addresses.size() > 0) {
                                                            startGoogleMaps(f, address, addresses);
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

                                    LuogoEvento le = event.getLuogoEv();
                                    if (le != null) {
                                        address.setPaintFlags(address.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                        address.setText(f.getString(R.string.event_address, le.toString()));
                                    }

                                    Button qrCodeRender = v.findViewById(R.id.button9);
                                    qrCodeRender.setOnClickListener(c -> {
                                        Bundle b = new Bundle();
                                        b.putString("eventId", event.getIdEvent());
                                        b.putString("userId", userJwt);
                                        b.putString("data", event.getLuogoEv().getData());
                                        b.putString("ora", event.getLuogoEv().getOra());

                                        QRCodeRenderingFragment destination = QRCodeRenderingFragment.newInstance(b);
                                        FragmentTransaction transaction = f.requireActivity().getSupportFragmentManager().beginTransaction();
                                        destination.show(transaction, "QRCodeRenderingFragment");
                                    });

                                    Button writeReview = v.findViewById(R.id.button10);
                                    String dateTime = dataArr[2]
                                            + "-" + dataArr[1] + "-" + dataArr[0] + "T" + oraS + ":00";
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        LocalDateTime now = LocalDateTime.now(), eventDateTime = LocalDateTime.parse(dateTime);
                                        Log.i("boolean", String.valueOf(now.isBefore(eventDateTime)));
                                        writeReview.setEnabled(!now.isBefore(eventDateTime));
                                    } else {
                                        try {
                                            DateFormat df = DateFormat.getDateInstance();
                                            Date now = new Date(), eventDateTime = df.parse(dateTime);
                                            writeReview.setEnabled(!now.before(eventDateTime));
                                        } catch(ParseException ex) {
                                            ex.printStackTrace();
                                        }
                                    }

                                    writeReview.setOnClickListener(c -> {
                                        Bundle b = new Bundle();
                                        b.putString("userId", userJwt);
                                        b.putString("eventId", eventId);
                                        Navigation.findNavController(v).navigate(R.id.action_eventDetailsFragment_to_reviewWriting, b);
                                    });

                                    Button deleteTicket = v.findViewById(R.id.button11);
                                    deleteTicket.setOnClickListener(c ->
                                            f.getViewModel().deleteTicket(userJwt, event.getTicketId(),
                                                    event.getIdEvent(), f, loginLauncherTicket));
                                });
                            }
                            response.body().close();
                            break;
                        }
                        case 400: {
                            setAlertDialog(R.string.malformed_request, R.string.malformed_request_message);
                            break;
                        }
                        case 401: {
                            //Utente non autenticato. Esegui un launcher con Intent riferito a LoginActivity.class e,
                            //in caso di Activity.RESULT_OK, ripeti la richiesta.
                            Intent loginIntent = new Intent(f.requireActivity(), LoginActivity.class);
                            loginLauncher.launch(loginIntent);
                            break;
                        }
                        case 404: {
                            setAlertDialog(R.string.no_event, R.string.no_event_message);
                            break;
                        }
                    }
                } else {
                    Log.i("noResponse", "Nessuna informazione ricevuta sull'evento richiesto");
                }
            }
        });
    }
}
