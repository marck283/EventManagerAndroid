package it.disi.unitn.lpsmt.lasagna.eventinfo.organizedEvent;

import android.app.Activity;
import android.content.Intent;
import android.util.Pair;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.lasagna.eventinfo.callbacks.OrganizedEventCallback;
import it.disi.unitn.lpsmt.lasagna.network.NetworkRequest;
import it.disi.unitn.lpsmt.lasagna.network.networkOps.ServerOperation;

import okhttp3.Request;

public class OrganizedEventInfo extends ServerOperation {

    private final NetworkRequest request;

    private final String userJwt, eventId;

    private final View v;

    private final Fragment f;

    private final String day;

    private final ActivityResultLauncher<Intent> loginLauncher;

    private final Class<? extends Activity> c;

    private final int iv3, tv6, info_on_event, spinner2, orgDateTextView, tv15, spinner, orgHourTextView,
            list_item, event_address, bt8, bt12, tv12, duration, user_not_logged_in, user_not_logged_in_message,
            no_org_event, no_org_event_message;

    public OrganizedEventInfo(@NonNull View v, @NonNull Fragment f, @NonNull String userJwt,
                              @NonNull String evId, @NonNull String day, @NotNull Class<? extends Activity> c,
                              @IdRes int iv3,
                              @IdRes int tv6, @StringRes int info_on_event, @IdRes int spinner2,
                              @IdRes int orgDateTextView, @IdRes int tv15, @IdRes int spinner,
                              @IdRes int orgHourTextView, @LayoutRes int list_item, @StringRes int event_address,
                              @IdRes int bt8, @IdRes int bt12, @IdRes int tv12, @StringRes int duration,
                              @StringRes int user_not_logged_in, @StringRes int user_not_logged_in_message,
                              @StringRes int no_org_event, @StringRes int no_org_event_message) {
        request = getNetworkRequest();
        this.userJwt = userJwt;
        eventId = evId;
        this.v = v;
        this.f = f;
        this.day = day;
        this.loginLauncher = null;
        this.c = c;
        this.iv3 = iv3;
        this.tv6 = tv6;
        this.info_on_event = info_on_event;
        this.spinner2 = spinner2;
        this.orgDateTextView = orgDateTextView;
        this.tv15 = tv15;
        this.spinner = spinner;
        this.orgHourTextView = orgHourTextView;
        this.list_item = list_item;
        this.event_address = event_address;
        this.bt8 = bt8;
        this.bt12 = bt12;
        this.tv12 = tv12;
        this.duration = duration;
        this.user_not_logged_in = user_not_logged_in;
        this.user_not_logged_in_message = user_not_logged_in_message;
        this.no_org_event = no_org_event;
        this.no_org_event_message = no_org_event_message;
    }

    public OrganizedEventInfo(@NonNull View v, @NonNull Fragment f, @NonNull String userJwt,
                              @NonNull String evId,
                              @NonNull ActivityResultLauncher<Intent> loginLauncher,
                              @NotNull Class<? extends Activity> c, @IdRes int iv3,
                              @IdRes int tv6, @StringRes int info_on_event, @IdRes int spinner2,
                              @IdRes int orgDateTextView, @IdRes int tv15, @IdRes int spinner,
                              @IdRes int orgHourTextView, @LayoutRes int list_item, @StringRes int event_address,
                              @IdRes int bt8, @IdRes int bt12, @IdRes int tv12, @StringRes int duration,
                              @StringRes int user_not_logged_in, @StringRes int user_not_logged_in_message,
                              @StringRes int no_org_event, @StringRes int no_org_event_message) {
        request = getNetworkRequest();
        this.userJwt = userJwt;
        eventId = evId;
        this.v = v;
        this.f = f;
        this.day = null;
        this.loginLauncher = loginLauncher;
        this.c = c;
        this.iv3 = iv3;
        this.tv6 = tv6;
        this.info_on_event = info_on_event;
        this.spinner2 = spinner2;
        this.orgDateTextView = orgDateTextView;
        this.tv15 = tv15;
        this.spinner = spinner;
        this.orgHourTextView = orgHourTextView;
        this.list_item = list_item;
        this.event_address = event_address;
        this.bt8 = bt8;
        this.bt12 = bt12;
        this.tv12 = tv12;
        this.duration = duration;
        this.user_not_logged_in = user_not_logged_in;
        this.user_not_logged_in_message = user_not_logged_in_message;
        this.no_org_event = no_org_event;
        this.no_org_event_message = no_org_event_message;
    }

    public OrganizedEventInfo(@NonNull View v, @NonNull Fragment f, @NonNull String userJwt,
                              @NonNull String evId, @NotNull Class<? extends Activity> c, @IdRes int iv3,
                              @IdRes int tv6, @StringRes int info_on_event, @IdRes int spinner2,
                              @IdRes int orgDateTextView, @IdRes int tv15, @IdRes int spinner,
                              @IdRes int orgHourTextView, @LayoutRes int list_item, @StringRes int event_address,
                              @IdRes int bt8, @IdRes int bt12, @IdRes int tv12, @StringRes int duration,
                              @StringRes int user_not_logged_in, @StringRes int user_not_logged_in_message,
                              @StringRes int no_org_event, @StringRes int no_org_event_message) {
        request = getNetworkRequest();
        this.userJwt = userJwt;
        eventId = evId;
        this.v = v;
        this.f = f;
        this.day = null;
        this.loginLauncher = null;
        this.c = c;
        this.iv3 = iv3;
        this.tv6 = tv6;
        this.info_on_event = info_on_event;
        this.spinner2 = spinner2;
        this.orgDateTextView = orgDateTextView;
        this.tv15 = tv15;
        this.spinner = spinner;
        this.orgHourTextView = orgHourTextView;
        this.list_item = list_item;
        this.event_address = event_address;
        this.bt8 = bt8;
        this.bt12 = bt12;
        this.tv12 = tv12;
        this.duration = duration;
        this.user_not_logged_in = user_not_logged_in;
        this.user_not_logged_in_message = user_not_logged_in_message;
        this.no_org_event = no_org_event;
        this.no_org_event_message = no_org_event_message;
    }

    public void run() {
        Request req;
        List<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<>("x-access-token", userJwt));
        if (day != null) {
            headers.add(new Pair<>("date", day));
        }
        req = request.getRequest(headers, getBaseUrl() + "/api/v2/InfoEventoOrg/" + eventId);
        request.enqueue(req, new OrganizedEventCallback(v, f, loginLauncher, day, c, iv3, tv6, info_on_event,
                spinner2, orgDateTextView, tv15, spinner, orgHourTextView, list_item, event_address,
                bt8, bt12, tv12, duration, user_not_logged_in, user_not_logged_in_message, no_org_event,
                no_org_event_message));
    }
}