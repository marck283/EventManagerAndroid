package it.disi.unitn.lpsmt.lasagna.eventinfo.registeredEvent;

import android.app.Activity;
import android.content.Intent;
import android.util.Pair;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

import it.disi.unitn.lpsmt.lasagna.eventinfo.EventDetailsViewModel;
import it.disi.unitn.lpsmt.lasagna.network.NetworkRequest;
import it.disi.unitn.lpsmt.lasagna.network.networkOps.ServerOperation;
import okhttp3.Request;

public class RegisteredEventInfo extends ServerOperation {
    private final NetworkRequest request;

    private final String userJwt, eventId, data;

    private final Fragment f;

    private final View v;

    private final ActivityResultLauncher<Intent> loginLauncher;

    private final EventDetailsViewModel eventVM;

    private final Class<? extends Activity> c;

    private final int noconn, noconnmsg, eventPicture, title, organizer, textView16, textView11, day_not_selectable;

    private final int textView20, time_not_selectable, textView39, duration, textView42, event_address, button9;

    private final int button10, action_eventDetailsFragment_to_reviewWriting, button11,
            malformed_request, malformed_request_message, no_event, no_event_message;

    private final FutureTask<Void> task;

    public RegisteredEventInfo(@NonNull String userJwt, @NonNull String eventId, @NonNull Fragment f,
                               @NonNull View v, @NonNull String data,
                               @NonNull ActivityResultLauncher<Intent> loginLauncher,
                               @NotNull EventDetailsViewModel eventVM, @StringRes int noconn,
                               @StringRes int noconnmsg,
                               @NotNull Class<? extends Activity> c, @IdRes int eventPicture,
                               @StringRes int title, @StringRes int organizer,
                               @IdRes int textView16, @IdRes int textView11,
                               @StringRes int day_not_selectable,
                               @IdRes int textView20, @StringRes int time_not_selectable,
                               @IdRes int textView39, @StringRes int duration, @IdRes int textView42,
                               @StringRes int event_address, @IdRes int button9, @IdRes int button10,
                               @IdRes int action_eventDetailsFragment_to_reviewWriting,
                               @IdRes int button11, @StringRes int malformed_request,
                               @StringRes int malformed_request_message, @StringRes int no_event,
                               @StringRes int no_event_message, @NotNull FutureTask<Void> task) {
        request = getNetworkRequest();
        this.userJwt = userJwt;
        this.eventId = eventId;
        this.f = f;
        this.v = v;
        this.data = data;
        this.loginLauncher = loginLauncher;
        this.eventVM = eventVM;
        this.noconn = noconn;
        this.noconnmsg = noconnmsg;
        this.c = c;
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

    public void run() {
        List<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<>("x-access-token", userJwt));
        headers.add(new Pair<>("data", data));
        Request req = request.getRequest(headers, getBaseUrl() + "/api/v2/InfoEventoIscr/" + eventId);
        request.enqueue(req, new RegisteredEventCallback(f, v, userJwt, eventId, loginLauncher,
                eventVM, noconn, noconnmsg, c, eventPicture, title, organizer, textView16, textView11,
                day_not_selectable, textView20, time_not_selectable, textView39, duration, textView42,
                event_address, button9, button10, action_eventDetailsFragment_to_reviewWriting,
                button11, malformed_request, malformed_request_message, no_event, no_event_message,
                task));
    }
}
