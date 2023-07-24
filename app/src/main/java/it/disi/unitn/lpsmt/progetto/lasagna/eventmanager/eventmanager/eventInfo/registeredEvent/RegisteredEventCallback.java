package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.registeredEvent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.GeocoderExt;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.LuogoEv;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.EventDetailsFragment;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.qr_code_scan.QRCodeRenderingFragment;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.special_buttons.ListenerButton;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisteredEventCallback implements Callback {
    private final EventDetailsFragment f;

    private final View v;

    private final String userJwt, eventId;

    private final ActivityResultLauncher<Intent> loginLauncher;

    public RegisteredEventCallback(@NotNull EventDetailsFragment f, @NotNull View v, @NotNull String ujwt,
                                   @NotNull String eid, @NotNull ActivityResultLauncher<Intent> loginLauncher) {
        this.f = f;
        this.v = v;
        userJwt = ujwt;
        eventId = eid;
        this.loginLauncher = loginLauncher;
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
        switch (response.code()) {
            case 200 -> {
                Gson gson = new GsonBuilder().create();

                String body = response.body().string();
                RegisteredEvent event = RegisteredEvent.parseJSON(gson.fromJson(body, JsonObject.class));

                Activity activity = f.getActivity();
                if (activity != null && f.isAdded()) {
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
                        if (!address.hasOnClickListeners()) {
                            address.setOnClickListener(c -> {
                                GeocoderExt geocoder = new GeocoderExt(f, address);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    geocoder.fromLocationName(address.getText().toString(), 5);
                                } else {
                                    geocoder.fromLocationNameThread(address.getText().toString(), 5);
                                }
                            });
                        }

                        LuogoEv le = event.getLuogoEv();
                        if (le != null) {
                            address.setPaintFlags(address.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                            address.setText(f.getString(R.string.event_address, le.getAddress()));
                        }

                        ListenerButton qrCodeRender = v.findViewById(R.id.button9);
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

                        ListenerButton writeReview = v.findViewById(R.id.button10);
                        String dateTime = dataArr[2]
                                + "-" + dataArr[0] + "-" + dataArr[1] + "T" + oraS + ":00";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            LocalDateTime now = LocalDateTime.now(), eventDateTime = LocalDateTime.parse(dateTime);
                            Log.i("boolean", String.valueOf(now.isBefore(eventDateTime)));
                            writeReview.setEnabled(!now.isBefore(eventDateTime) || event.getLuogoEv().getTerminato());
                        } else {
                            try {
                                SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
                                Date now = new Date(), eventDateTime = df.parse(dateTime);
                                writeReview.setEnabled(!now.before(eventDateTime) || event.getLuogoEv().getTerminato());
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }
                        }

                        writeReview.setOnClickListener(c -> {
                            Bundle b = new Bundle();
                            b.putString("userId", userJwt);
                            b.putString("eventId", eventId);
                            Navigation.findNavController(v).navigate(R.id.action_eventDetailsFragment_to_reviewWriting, b);
                        });

                        ListenerButton deleteTicket = v.findViewById(R.id.button11);
                        deleteTicket.setOnClickListener(c ->
                                f.getViewModel().deleteTicket(userJwt, event.getTicketId(),
                                        event.getIdEvent(), f, event.getLuogoEv().getData(),
                                        event.getLuogoEv().getOra()));
                    });
                }
                response.body().close();
            }
            case 400 -> setAlertDialog(R.string.malformed_request, R.string.malformed_request_message);
            case 401 -> {
                //Utente non autenticato. Esegui un launcher con Intent riferito a LoginActivity.class e,
                //in caso di Activity.RESULT_OK, ripeti la richiesta.
                Intent loginIntent = new Intent(f.requireActivity(), LoginActivity.class);
                loginLauncher.launch(loginIntent);
            }
            case 404 -> setAlertDialog(R.string.no_event, R.string.no_event_message);
        }
    }
}
