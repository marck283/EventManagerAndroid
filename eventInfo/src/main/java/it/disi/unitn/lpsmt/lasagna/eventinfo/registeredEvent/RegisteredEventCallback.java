package it.disi.unitn.lpsmt.lasagna.eventinfo.registeredEvent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import it.disi.unitn.lpsmt.lasagna.eventinfo.R;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

import it.disi.unitn.lpsmt.lasagna.eventinfo.EventDetailsViewModel;
import it.disi.unitn.lpsmt.lasagna.eventinfo.fields.Field;

public class RegisteredEventCallback {

    private final Fragment f;

    private final ActivityResultLauncher<Intent> loginLauncher;

    private final EventDetailsViewModel eventVM;

    private final Class<? extends Activity> c;

    public RegisteredEventCallback(@NotNull Fragment f,
                                   @NotNull ActivityResultLauncher<Intent> loginLauncher,
                                   @NotNull EventDetailsViewModel eventVM,
                                   @NotNull Class<? extends Activity> c) {
        this.f = f;
        this.loginLauncher = loginLauncher;
        this.eventVM = eventVM;
        this.c = c;
    }

    public void handleInfoSuccess(@NonNull JsonObject data) {
        RegisteredEvent event = RegisteredEvent.parseJSON(data);

        Activity activity = f.getActivity();
        if (activity != null && f.isAdded()) {
            eventVM.setTicketDetails(event.getTicketId(), event.getLuogoEv().getData(), event.getLuogoEv().getOra());

            Field eventPic = new Field();
            eventPic.setFieldId("eventPicture");
            eventPic.setFieldValue(event.getEventPic());
            eventVM.setField(eventPic);

            Field title1 = new Field();
            title1.setFieldId("title");
            title1.setFieldValue(event.getEventName());
            eventVM.setField(title1);

            Field organizer = new Field();
            organizer.setFieldId("organizer");
            organizer.setFieldValue(f.getString(R.string.organizer, event.getOrgName()));
            eventVM.setField(organizer);

            Field day = new Field();
            String[] dateArr = event.getLuogoEv().getData().split("-");
            day.setFieldId("day");
            day.setFieldValue(f.getString(R.string.day_not_selectable,
                    "\n" + String.join("/",
                            dateArr)));
            eventVM.setField(day);

            Field time = new Field();
            String sTime = event.getLuogoEv().getOra();
            time.setFieldId("time");
            time.setFieldValue(f.getString(R.string.time_not_selectable,
                    "\n" + sTime));
            eventVM.setField(time);

            Field duration = new Field();
            String[] sDuration = event.getDurata().split(":");
            duration.setFieldId("duration");
            duration.setFieldValue(f.getString(R.string.duration, sDuration[0], sDuration[1], sDuration[2]));
            eventVM.setField(duration);

            Field address = new Field();
            address.setFieldId("address");
            address.setFieldValue(f.getString(R.string.event_address, event.getLuogoEv().getAddress()));
            eventVM.setField(address);

            Field writeReview = new Field();
            writeReview.setFieldId("writeReview");
            String dateTime = dateArr[2]
                    + "-" + dateArr[0] + "-" + dateArr[1] + "T" + sTime + ":00";
            {
                LocalDateTime now = LocalDateTime.now(), eventDateTime = LocalDateTime.parse(dateTime);
                Log.i("boolean", String.valueOf(now.isBefore(eventDateTime)));
                writeReview.setFieldValue(String.valueOf(!now.isBefore(eventDateTime) || event.getLuogoEv().getTerminato()));
            }
            eventVM.setField(writeReview);
        }
    }

    public void handleInfoError(int statusCode) {
        switch(statusCode) {
            case 400 -> eventVM.showDialog(R.string.malformed_request, R.string.malformed_request_message);
            case 401 -> {
                Activity activity = f.getActivity();
                if (activity != null && !activity.isFinishing() && !activity.isDestroyed() && f.isAdded()) {
                    activity.runOnUiThread(() -> {
                        if (!activity.isFinishing() && !activity.isDestroyed()) {
                            Intent loginIntent = new Intent(activity, c);
                            loginLauncher.launch(loginIntent);
                        }
                    });
                }
            }
            case 404 -> eventVM.showDialog(R.string.no_event, R.string.no_event_message);
        }
    }
}
