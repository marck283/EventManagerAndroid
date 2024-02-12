package it.disi.unitn.lpsmt.lasagna.eventinfo.registeredEvent;

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
import androidx.annotation.IdRes;
import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
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
import java.util.concurrent.FutureTask;

import it.disi.unitn.lasagna.eventcreation.helpers.LuogoEv;
import it.disi.unitn.lasagna.eventmanager.geocoder.GeocoderExt;
import it.disi.unitn.lasagna.eventmanager.ui_extra.special_buttons.ListenerButton;
import it.disi.unitn.lpsmt.lasagna.eventinfo.EventDetailsViewModel;
import it.disi.unitn.lpsmt.lasagna.eventinfo.qr_code_scan.QRCodeRenderingFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisteredEventCallback implements Callback {
    private final Fragment f;

    private final View v;

    private final String userJwt, eventId;

    private final ActivityResultLauncher<Intent> loginLauncher;

    private final EventDetailsViewModel eventVM;

    private final Class<? extends Activity> c;

    private final int noconn, noconnmsg, eventPicture, title, organizer, textView16, textView11, day_not_selectable;

    private final int textView20, time_not_selectable, textView39, duration, textView42, event_address, button9;

    private final int button10, action_eventDetailsFragment_to_reviewWriting, button11,
            malformed_request, malformed_request_message, no_event, no_event_message;

    private final FutureTask<Void> task;

    public RegisteredEventCallback(@NotNull Fragment f, @NotNull View v, @NotNull String ujwt,
                                   @NotNull String eid, @NotNull ActivityResultLauncher<Intent> loginLauncher,
                                   @NotNull EventDetailsViewModel eventVM, @StringRes int noconn,
                                   @StringRes int noconnmsg, @NotNull Class<? extends Activity> c,
                                   @IdRes int eventPicture, @StringRes int title, @StringRes int organizer,
                                   @IdRes int textView16, @IdRes int textView11,
                                   @StringRes int day_not_selectable,
                                   @IdRes int textView20, @StringRes int time_not_selectable,
                                   @IdRes int textView39, @StringRes int duration, @IdRes int textView42,
                                   @StringRes int event_address, @IdRes int button9, @IdRes int button10,
                                   @NavigationRes int action_eventDetailsFragment_to_reviewWriting,
                                   @IdRes int button11, @StringRes int malformed_request,
                                   @StringRes int malformed_request_message, @StringRes int no_event,
                                   @StringRes int no_event_message, @NotNull FutureTask<Void> task) {
        this.f = f;
        this.v = v;
        userJwt = ujwt;
        eventId = eid;
        this.loginLauncher = loginLauncher;
        this.eventVM = eventVM;
        this.c = c;
        this.noconn = noconn;
        this.noconnmsg = noconnmsg;
        this.eventPicture = eventPicture;
        this.title = title;
        this.organizer = organizer;
        this.textView16 = textView16;
        this.textView11 = textView11;
        this.day_not_selectable = day_not_selectable;
        this.textView20 = textView20;
        this.time_not_selectable = time_not_selectable;
        this.textView39 = textView39;
        this.duration = duration;
        this.textView42 = textView42;
        this.event_address = event_address;
        this.button9 = button9;
        this.button10 = button10;
        this.action_eventDetailsFragment_to_reviewWriting = action_eventDetailsFragment_to_reviewWriting;
        this.button11 = button11;
        this.malformed_request = malformed_request;
        this.malformed_request_message = malformed_request_message;
        this.no_event = no_event;
        this.no_event_message = no_event_message;
        this.task = task;
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
                        ImageView image = v.findViewById(eventPicture);
                        Glide.with(v).load(event.decodeBase64()).into(image);


                        TextView title1 = v.findViewById(title);
                        title1.setText(event.getEventName());

                        TextView organizzatore = v.findViewById(textView16);
                        organizzatore.setText(f.getString(organizer, event.getOrgName()));

                        TextView giorno = v.findViewById(textView11);
                        String[] dataArr = event.getLuogoEv().getData().split("-");
                        giorno.setText(f.getString(day_not_selectable,
                                "\n" + dataArr[1] + "/" + dataArr[0] + "/" + dataArr[2]));

                        TextView ora = v.findViewById(textView20);
                        String oraS = event.getLuogoEv().getOra();
                        ora.setText(f.getString(time_not_selectable,
                                "\n" + oraS));

                        String[] sDurata = event.getDurata().split(":");
                        TextView durata = v.findViewById(textView39);
                        durata.setText(f.getString(duration, sDurata[0], sDurata[1], sDurata[2]));

                        TextView address = v.findViewById(textView42);
                        address.setText(f.getString(event_address, event.getLuogoEv().toString()));
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
                            address.setText(f.getString(event_address, le.getAddress()));
                        }

                        ListenerButton qrCodeRender = v.findViewById(button9);
                        qrCodeRender.setOnClickListener(c -> {
                            Bundle b = new Bundle();
                            b.putString("eventId", event.getIdEvent());
                            b.putString("userId", userJwt);
                            b.putString("data", event.getLuogoEv().getData());
                            b.putString("ora", event.getLuogoEv().getOra());

                            task.run();
                        });

                        ListenerButton writeReview = v.findViewById(button10);
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
                            Navigation.findNavController(v).navigate(action_eventDetailsFragment_to_reviewWriting, b);
                        });

                        ListenerButton deleteTicket = v.findViewById(button11);
                        deleteTicket.setOnClickListener(c ->
                                eventVM.deleteTicket(userJwt, event.getTicketId(),
                                        event.getIdEvent(), f, event.getLuogoEv().getData(),
                                        event.getLuogoEv().getOra(), noconn, noconnmsg));
                    });
                }
                response.body().close();
            }
            case 400 -> setAlertDialog(malformed_request, malformed_request_message);
            case 401 -> {
                //Utente non autenticato. Esegui un launcher con Intent riferito a LoginActivity.class e,
                //in caso di Activity.RESULT_OK, ripeti la richiesta.
                Intent loginIntent = new Intent(f.requireActivity(), c);
                loginLauncher.launch(loginIntent);
            }
            case 404 -> setAlertDialog(no_event, no_event_message);
        }
    }
}
