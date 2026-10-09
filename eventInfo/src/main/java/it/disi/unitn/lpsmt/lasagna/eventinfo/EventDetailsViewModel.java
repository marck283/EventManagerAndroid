package it.disi.unitn.lpsmt.lasagna.eventinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.journeyapps.barcodescanner.ScanOptions;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.FutureTask;

import it.disi.unitn.lpsmt.lasagna.checkqrcode.CheckQRCode;
import it.disi.unitn.lpsmt.lasagna.checkqrcode.QRCodeCallback;
import it.disi.unitn.lpsmt.lasagna.eventinfo.callbacks.OrganizedEventCallback;
import it.disi.unitn.lpsmt.lasagna.eventinfo.callbacks.TerminatorCallback;
import it.disi.unitn.lpsmt.lasagna.eventinfo.interfaces.OrgEvInterface;
import it.disi.unitn.lpsmt.lasagna.eventinfo.publicEvent.EventInfoCallback;
import it.disi.unitn.lpsmt.lasagna.eventinfo.registeredEvent.RegisteredEventCallback;
import it.disi.unitn.lpsmt.lasagna.network.NetworkCallback;
import it.disi.unitn.lpsmt.lasagna.network.client.RetrofitClient;
import it.disi.unitn.lpsmt.lasagna.network.repository.EventRepository;
import it.disi.unitn.lpsmt.lasagna.network.repository.OrganizedEventRepository;
import it.disi.unitn.lpsmt.lasagna.network.repository.TicketRepository;
import it.disi.unitn.lpsmt.lasagna.user_event_registration.UserEventRegistrationCallback;

public class EventDetailsViewModel extends ViewModel {
    private NetworkCallback callback;

    private void setNoConnectionDialog(@NonNull Activity f, @StringRes int noconn, @StringRes int noconnmsg) {
        AlertDialog dialog = new AlertDialog.Builder(f).create();
        dialog.setTitle(noconn);
        dialog.setMessage(f.getString(noconnmsg));
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which1) -> dialog1.dismiss());
        dialog.show();
    }

    public void terminateEvent(@NonNull String accessToken, @NonNull Fragment f,
                               @NonNull String eventId, @NonNull String data, @NonNull String ora,
                               @NonNull View v, @NonNull ActivityResultLauncher<Intent> loginLauncher,
                               @NonNull Intent loginIntent, @StringRes int noconn, @StringRes int noconnmsg,
                               @StringRes int malformed_req,
                               @StringRes int malf_req_msg, @StringRes int no_session_title,
                               @StringRes int no_session_content, @StringRes int attempt_ok,
                               @StringRes int attempt_ok_msg, @IdRes int but8, @IdRes int but12,
                               @StringRes int internal_server_error) {
        Activity activity = f.getActivity();
        if (activity != null && f.isAdded()) {
            callback = new NetworkCallback(f.requireActivity());
            if (callback.isOnline(f.requireActivity())) {
                RetrofitClient.getInstance().setAccessToken(accessToken);

                TerminatorCallback termCallback = new TerminatorCallback(f, loginLauncher, loginIntent, v,
                        malformed_req, malf_req_msg, no_session_title, no_session_content, attempt_ok,
                        attempt_ok_msg, but8, but12, internal_server_error);

                OrganizedEventRepository repo = new OrganizedEventRepository(new OrganizedEventRepository.ActionCallback() {
                    @Override
                    public void onSuccess() {
                        termCallback.handleResponseCode(200);
                    }

                    @Override
                    public void onError(int statusCode, String errorMessage) {
                        termCallback.handleResponseCode(statusCode);
                    }
                });
                repo.terminateEvent(eventId, data, ora);
            } else {
                setNoConnectionDialog(activity, noconn, noconnmsg);
            }
        }
    }

    public void deleteEvent(@NonNull String accessToken, @NonNull String eventId, @NonNull Fragment f,
                            @StringRes int noconn, @StringRes int noconnmsg) {
        Activity activity = f.getActivity();
        if (activity != null && f.isAdded()) {
            if (callback.isOnline(f.requireActivity())) {
                RetrofitClient.getInstance().setAccessToken(accessToken);

                OrganizedEventRepository repo = new OrganizedEventRepository(new OrganizedEventRepository.ActionCallback() {
                    @Override
                    public void onSuccess() {
                        Activity a = f.getActivity();
                        if (a instanceof OrgEvInterface oei && !a.isFinishing() && !a.isDestroyed()) {
                            oei.showRes(200);
                        }
                    }

                    @Override
                    public void onError(int statusCode, String errorMessage) {
                        Activity a = f.getActivity();
                        if (a instanceof OrgEvInterface oei && !a.isFinishing() && !a.isDestroyed()) {
                            oei.showRes(statusCode);
                        }
                    }
                });
                repo.cancelEvent(eventId);
            } else {
                setNoConnectionDialog(activity, noconn, noconnmsg);
            }
        }
    }

    private void requestEvInfoIscr(@NonNull View view,
                                   @NonNull Fragment f, @Nullable String userJwt,
                                   @Nullable String data, @NonNull String eventId,
                                   @Nullable ActivityResultLauncher<Intent> loginLauncher,
                                   @StringRes int noconn, @StringRes int noconnmsg,
                                   @NotNull Class<? extends Activity> c, @IdRes int eventPicture,
                                   @StringRes int title, @StringRes int organizer,
                                   @IdRes int textView16, @IdRes int textView11,
                                   @StringRes int day_not_selectable,
                                   @IdRes int textView20, @StringRes int time_not_selectable,
                                   @IdRes int textView39, @IdRes int textView42,
                                   @IdRes int button9, @IdRes int button10,
                                   @IdRes int action_eventDetailsFragment_to_reviewWriting,
                                   @IdRes int button11, @StringRes int malformed_request,
                                   @StringRes int malformed_request_message, @StringRes int no_event,
                                   @StringRes int no_event_message, @StringRes int event_address,
                                   @StringRes int duration, @NotNull FutureTask<Void> task) {
        if (userJwt != null && data != null && loginLauncher != null) {
            RetrofitClient.getInstance().setAccessToken(userJwt);
            RegisteredEventCallback callback = new RegisteredEventCallback(f, view, userJwt, eventId,
                    loginLauncher, this, noconn, noconnmsg, c, eventPicture, title, organizer, textView16,
                    textView11, day_not_selectable, textView20, time_not_selectable, textView39, duration,
                    textView42, event_address, button9, button10, action_eventDetailsFragment_to_reviewWriting,
                    button11, malformed_request, malformed_request_message, no_event, no_event_message, task);

            EventRepository repo = new EventRepository();
            repo.getPublicEventInfo(eventId, new EventRepository.EventDataCallback() {
                @Override
                public void onSuccess(JsonObject data) {
                    callback.handleInfoSuccess(data);
                }

                @Override
                public void onError(int statusCode, String errorMessage) {
                    callback.handleInfoError(statusCode);
                }
            });
        }
    }

    private void requestEventInfo(@NonNull String which, @NonNull String eventId, @NonNull View view,
                                  @NonNull Fragment f, @Nullable String userJwt,
                                  @Nullable String data, @Nullable ActivityResultLauncher<ScanOptions> launcher,
                                  @Nullable ActivityResultLauncher<Intent> loginLauncher,
                                  @StringRes int regClosed,
                                  @NotNull Class<? extends Activity> c, @IdRes int iv3,
                                  @IdRes int tv6, @StringRes int info_on_event, @IdRes int spinner2,
                                  @IdRes int orgDateTextView, @IdRes int tv15, @IdRes int spinner,
                                  @IdRes int orgHourTextView, @LayoutRes int list_item, @StringRes int event_address,
                                  @IdRes int bt8, @IdRes int bt12, @IdRes int tv12, @StringRes int duration,
                                  @StringRes int no_org_event, @StringRes int no_org_event_message) {
        switch (which) {
            case "pub" -> {
                EventInfoCallback infoCallback = new EventInfoCallback(f, view, regClosed);
                EventRepository repo = new EventRepository();
                repo.getPublicEventInfo(eventId, new EventRepository.EventDataCallback() {
                    @Override
                    public void onSuccess(JsonObject data) {
                        infoCallback.handleInfoSuccess(data);
                    }

                    @Override
                    public void onError(int statusCode, String errorMessage) { }
                });
            }
            case "org" -> {
                if (userJwt != null) {
                    RetrofitClient.getInstance().setAccessToken(userJwt);
                    OrganizedEventCallback callback = new OrganizedEventCallback(view, f, loginLauncher, data, c, iv3, tv6, info_on_event,
                            spinner2, orgDateTextView, tv15, spinner, orgHourTextView, list_item, event_address,
                            bt8, bt12, tv12, duration,
                            no_org_event, no_org_event_message);

                    OrganizedEventRepository repo = new OrganizedEventRepository();
                    repo.getOrganizedEventInfo(eventId, new OrganizedEventRepository.EventInfoCallback() {
                        @Override
                        public void onSuccess(JsonObject eventInfo) {
                            callback.handleInfoSuccess(eventInfo);
                        }

                        @Override
                        public void onError(int statusCode, String errorMessage) {
                            callback.handleInfoError(statusCode);
                        }
                    });
                }
            }
        }
    }

    public void getEventInfoIscr(@NotNull View v, @NonNull Fragment f, @Nullable String userJwt,
                                 @StringRes int noconn, @StringRes int noconnmsg,
                                 @Nullable String data, @NonNull String eventId,
                                 @Nullable ActivityResultLauncher<Intent> loginLauncher,
                                 @NotNull Class<? extends Activity> c,
                                 @IdRes int eventPicture,
                                 @StringRes int title, @StringRes int organizer, @StringRes int event_address,
                                 @StringRes int duration,
                                 @IdRes int textView16, @IdRes int textView11,
                                 @StringRes int day_not_selectable,
                                 @IdRes int textView20, @StringRes int time_not_selectable,
                                 @IdRes int textView39, @IdRes int textView42,
                                 @IdRes int button9, @IdRes int button10,
                                 @IdRes int action_eventDetailsFragment_to_reviewWriting,
                                 @IdRes int button11, @StringRes int malformed_request,
                                 @StringRes int malformed_request_message, @StringRes int no_event,
                                 @StringRes int no_event_message, @NotNull FutureTask<Void> task) {
        Activity activity = f.getActivity();
        if(activity != null && f.isAdded()) {
            callback = new NetworkCallback(f.requireActivity());
            if(callback.isOnline(f.requireActivity())) {
                requestEvInfoIscr(v, f, userJwt, data, eventId, loginLauncher, noconn, noconnmsg,
                        c, eventPicture, title, organizer, textView16, textView11, day_not_selectable,
                        textView20, time_not_selectable, textView39, textView42, button9, button10,
                        action_eventDetailsFragment_to_reviewWriting, button11, malformed_request,
                        malformed_request_message, no_event, no_event_message, event_address,
                        duration, task);
            } else {
                //Aggiungi un listener per cercare le informazioni sull'evento quando sarà tornata la connessione ad Internet.
                callback.registerNetworkCallback();
                callback.addDefaultNetworkActiveListener(() ->
                        requestEvInfoIscr(v, f, userJwt, data, eventId, loginLauncher, noconn, noconnmsg,
                                c, eventPicture, title, organizer, textView16, textView11, day_not_selectable,
                                textView20, time_not_selectable, textView39, textView42, button9, button10,
                                action_eventDetailsFragment_to_reviewWriting, button11, malformed_request,
                                malformed_request_message, no_event, no_event_message, event_address,
                                duration, task));
                callback.unregisterNetworkCallback();
                setNoConnectionDialog(activity, noconn, noconnmsg);
            }
        }
    }

    public Void getEventInfo(@NonNull String which, @NonNull String eventId, @NonNull View view,
                               @NonNull Fragment f, @Nullable String userJwt,
                               @Nullable String data, @Nullable ActivityResultLauncher<ScanOptions> launcher,
                               @Nullable ActivityResultLauncher<Intent> loginLauncher,
                             @StringRes int regClosed, @StringRes int noconn, @StringRes int noconnmsg,
                             @NotNull Class<? extends Activity> c, @IdRes int iv3,
                             @IdRes int tv6, @StringRes int info_on_event, @IdRes int spinner2,
                             @IdRes int orgDateTextView, @IdRes int tv15, @IdRes int spinner,
                             @IdRes int orgHourTextView, @LayoutRes int list_item, @StringRes int event_address,
                             @IdRes int bt8, @IdRes int bt12, @IdRes int tv12, @StringRes int duration,
                             @StringRes int no_org_event, @StringRes int no_org_event_message) {
        Activity activity = f.getActivity();
        if(activity != null && f.isAdded()) {
            callback = new NetworkCallback(f.requireActivity());
            if(callback.isOnline(f.requireActivity())) {
                requestEventInfo(which, eventId, view, f, userJwt, data, launcher, loginLauncher,
                        regClosed, c, iv3, tv6, info_on_event,
                        spinner2, orgDateTextView, tv15, spinner, orgHourTextView, list_item, event_address,
                        bt8, bt12, tv12, duration, no_org_event,
                        no_org_event_message);
            } else {
                //Aggiungi un listener per cercare le informazioni sull'evento quando sarà tornata la connessione ad Internet.
                callback.registerNetworkCallback();
                callback.addDefaultNetworkActiveListener(() ->
                        requestEventInfo(which, eventId, view, f, userJwt, data, launcher,
                                loginLauncher, regClosed, c, iv3, tv6, info_on_event,
                                spinner2, orgDateTextView, tv15, spinner, orgHourTextView, list_item, event_address,
                                bt8, bt12, tv12, duration, no_org_event,
                                no_org_event_message));
                callback.unregisterNetworkCallback();
                setNoConnectionDialog(activity, noconn, noconnmsg);
            }
        }
        return null;
    }

    public void registerUser(@NonNull String accessToken, @NonNull String eventId, @NonNull Fragment f,
                             @NonNull String day, @NonNull String time,
                             @Nullable ActivityResultLauncher<Intent> launcher,
                             @StringRes int noconn, @StringRes int noconnmsg, @NotNull Class<? extends Activity> c) {
        Activity activity = f.getActivity();
        if (activity != null && f.isAdded()) {
            callback = new NetworkCallback(f.requireActivity());
            if (callback.isOnline(f.requireActivity())) {
                RetrofitClient.getInstance().setAccessToken(accessToken);
                TicketRepository repo = getTicketRepository(f, launcher, c);
                repo.registerForPublicEvent(eventId, day, time);
            } else {
                setNoConnectionDialog(activity, noconn, noconnmsg);
            }
        }
    }

    @NonNull
    private static TicketRepository getTicketRepository(@NonNull Fragment f, @Nullable ActivityResultLauncher<Intent> launcher, @NonNull Class<? extends Activity> c) {
        UserEventRegistrationCallback uerCallback = new UserEventRegistrationCallback(f, launcher, c);

        return new TicketRepository(new TicketRepository.TicketActionCallback() {
            @Override
            public void onSuccess(int statusCode) {
                uerCallback.handleResponseCode(statusCode);
            }

            @Override
            public void onError(int statusCode, String errorMessage) {
                uerCallback.handleResponseCode(statusCode);
            }
        });
    }

    public void deleteTicket(@NonNull String accessToken, @NonNull String ticketId,
                             @NonNull String eventId, @NonNull Fragment f,
                             @NonNull String data, @NonNull String ora,
                             @StringRes int noconn, @StringRes int noconnmsg) {
        Activity activity = f.getActivity();
        if (activity != null && f.isAdded()) {
            callback = new NetworkCallback(f.requireActivity());
            if (callback.isOnline(f.requireActivity())) {
                RetrofitClient.getInstance().setAccessToken(accessToken);

                TicketRepository repo = new TicketRepository(new TicketRepository.TicketActionCallback() {
                    @Override
                    public void onSuccess(int statusCode) {
                        Activity a = f.getActivity();
                        if (a != null && !a.isFinishing() && !a.isDestroyed() && f.isAdded()) {
                            a.runOnUiThread(() -> {
                                AlertDialog dialog = new AlertDialog.Builder(a).create();
                                dialog.setTitle(R.string.deletion_successful);
                                dialog.setMessage(f.getString(R.string.deletion_successful_message));
                                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (d, w) -> d.dismiss());
                                dialog.show();
                            });
                        }
                    }

                    @Override
                    public void onError(int statusCode, String errorMessage) {
                        Activity a = f.getActivity();
                        if (a != null && !a.isFinishing() && !a.isDestroyed() && f.isAdded()) {
                            a.runOnUiThread(() -> {
                                AlertDialog dialog = new AlertDialog.Builder(a).create();
                                dialog.setTitle(R.string.internal_server_error);
                                dialog.setMessage(f.getString(R.string.internal_server_error));
                                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (d, w) -> d.dismiss());
                                dialog.show();
                            });
                        }
                    }
                });
                repo.deleteTicket(eventId, ticketId, data, ora);
            } else {
                setNoConnectionDialog(activity, noconn, noconnmsg);
            }
        }
    }

    public Void checkQR(@NonNull String qrCode, @NonNull String eventId,
                        @NonNull String day, @NonNull String hour, @NonNull Fragment f,
                        @StringRes int validQRCT, @StringRes int validQRCMsg,
                        @StringRes int noconn, @StringRes int noconnmsg, @StringRes int invalid_qr_code,
                        @StringRes int invalid_qr_code_message, @StringRes int malformed_request,
                        @StringRes int malformed_request_message, @StringRes int no_session_title,
                        @StringRes int no_session_message) {
        Activity activity = f.getActivity();
        if(activity != null && f.isAdded()) {
            callback = new NetworkCallback(f.requireActivity());
            if(callback.isOnline(f.requireActivity())) {
                String[] dataArr = day.split("/");
                day = dataArr[1] + "-" + dataArr[0] + "-" + dataArr[2];
                try {
                    QRCodeCallback callback = new QRCodeCallback(f, validQRCT, validQRCMsg, invalid_qr_code,
                            invalid_qr_code_message, malformed_request, malformed_request_message,
                            no_session_title, no_session_message);
                    CheckQRCode check = new CheckQRCode();
                    check.checkQRCode(qrCode, eventId, day, hour, callback);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                setNoConnectionDialog(activity, noconn, noconnmsg);
            }
        }

        return null;
    }
}