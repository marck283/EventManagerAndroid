package it.disi.unitn.lpsmt.lasagna.eventinfo;

import android.app.Activity;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import it.disi.unitn.lpsmt.lasagna.checkqrcode.CheckQRCode;
import it.disi.unitn.lpsmt.lasagna.checkqrcode.QRCodeCallback;
import it.disi.unitn.lpsmt.lasagna.eventinfo.callbacks.OrganizedEventCallback;
import it.disi.unitn.lpsmt.lasagna.eventinfo.callbacks.TerminatorCallback;
import it.disi.unitn.lpsmt.lasagna.eventinfo.fields.DialogState;
import it.disi.unitn.lpsmt.lasagna.eventinfo.fields.Field;
import it.disi.unitn.lpsmt.lasagna.eventinfo.interfaces.OrgEvInterface;
import it.disi.unitn.lpsmt.lasagna.eventinfo.organizedEvent.OrganizedEvent;
import it.disi.unitn.lpsmt.lasagna.eventinfo.publicEvent.EventInfo;
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

    private final MutableLiveData<Field> fieldList = new MutableLiveData<>();
    private final MutableLiveData<EventInfo> publicEventInfo = new MutableLiveData<>();
    private final MutableLiveData<OrganizedEvent> organizedEvent = new MutableLiveData<>();
    private final MutableLiveData<DialogState> dialogState = new MutableLiveData<>();

    private String ticketId = "";
    private String eventDate = "";
    private String eventTime = "";

    public void setField(@NonNull Field field) {
        fieldList.postValue(field);
    }

    public LiveData<Field> getFieldList() {
        return fieldList;
    }

    public void setPublicEventInfo(@NonNull EventInfo info) {
        publicEventInfo.postValue(info);
    }

    public LiveData<EventInfo> getPublicEventInfo() {
        return publicEventInfo;
    }

    public void setOrganizedEvent(@NonNull OrganizedEvent event) {
        organizedEvent.postValue(event);
    }

    public LiveData<OrganizedEvent> getOrganizedEvent() {
        return organizedEvent;
    }

    public void showDialog(@StringRes int titleRes, @StringRes int messageRes) {
        dialogState.postValue(new DialogState(titleRes, messageRes));
    }

    public LiveData<DialogState> getDialogState() {
        return dialogState;
    }

    public void setTicketDetails(String ticketId, String eventDate, String eventTime) {
        this.ticketId = ticketId != null ? ticketId : "";
        this.eventDate = eventDate != null ? eventDate : "";
        this.eventTime = eventTime != null ? eventTime : "";
    }

    public String getTicketId() { return ticketId; }
    public String getEventDate() { return eventDate; }
    public String getEventTime() { return eventTime; }

    private void setNoConnectionDialog(@StringRes int noconn, @StringRes int noconnmsg) {
        showDialog(noconn, noconnmsg);
    }

    public void terminateEvent(@NonNull String accessToken, @NonNull Fragment f,
                               @NonNull String eventId, @NonNull String data, @NonNull String ora,
                               @NonNull ActivityResultLauncher<Intent> loginLauncher,
                               @NonNull Intent loginIntent) {
        Activity activity = f.getActivity();
        if (activity != null && f.isAdded()) {
            callback = new NetworkCallback(f.requireActivity());
            if (callback.isOnline(f.requireActivity())) {
                RetrofitClient.getInstance().setAccessToken(accessToken);

                OrganizedEventRepository repo = getOrganizedEventRepository(f, loginLauncher, loginIntent);
                repo.terminateEvent(eventId, data, ora);
            } else {
                showDialog(R.string.no_connection, R.string.no_connection_message);
            }
        }
    }

    @NonNull
    private OrganizedEventRepository getOrganizedEventRepository(@NonNull Fragment f, @NonNull ActivityResultLauncher<Intent> loginLauncher, @NonNull Intent loginIntent) {
        TerminatorCallback termCallback = new TerminatorCallback(f, this, loginLauncher, loginIntent);

        return new OrganizedEventRepository(new OrganizedEventRepository.ActionCallback() {
            @Override
            public void onSuccess() {
                termCallback.handleResponseCode(200);
            }

            @Override
            public void onError(int statusCode, String errorMessage) {
                termCallback.handleResponseCode(statusCode);
            }
        });
    }

    public void deleteEvent(@NonNull String accessToken, @NonNull String eventId, @NonNull Fragment f,
                            @StringRes int noconn, @StringRes int noconnmsg) {
        Activity activity = f.getActivity();
        if (activity != null && f.isAdded()) {
            if (callback.isOnline(activity)) {
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
                setNoConnectionDialog(noconn, noconnmsg);
            }
        }
    }

    private void requestEvInfoIscr(@NonNull Fragment f, @Nullable String userJwt,
                                   @Nullable String data, @NonNull String eventId,
                                   @Nullable ActivityResultLauncher<Intent> loginLauncher,
                                   @NotNull Class<? extends Activity> c) {
        if (userJwt != null && data != null && loginLauncher != null) {
            RetrofitClient.getInstance().setAccessToken(userJwt);
            RegisteredEventCallback callback = new RegisteredEventCallback(f,
                    loginLauncher, this, c);

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

    private void requestEventInfo(@NonNull String which, @NonNull String eventId,
                                  @NonNull Fragment f, @Nullable String userJwt,
                                  @Nullable ActivityResultLauncher<Intent> loginLauncher,
                                  @NotNull Class<? extends Activity> c) {
        switch (which) {
            case "pub" -> {
                EventInfoCallback infoCallback = new EventInfoCallback(f, this);
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
                if (userJwt != null && loginLauncher != null) {
                    RetrofitClient.getInstance().setAccessToken(userJwt);
                    OrganizedEventCallback callback = new OrganizedEventCallback(f, this, loginLauncher, c);

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

    public void getEventInfoIscr(@NonNull Fragment f, @Nullable String userJwt,
                                 @StringRes int noconn, @StringRes int noconnmsg,
                                 @Nullable String data, @NonNull String eventId,
                                 @Nullable ActivityResultLauncher<Intent> loginLauncher,
                                 @NotNull Class<? extends Activity> c) {
        Activity activity = f.getActivity();
        if(activity != null && f.isAdded()) {
            callback = new NetworkCallback(activity);
            if(callback.isOnline(activity)) {
                requestEvInfoIscr(f, userJwt, data, eventId, loginLauncher,
                        c);
            } else {
                //Aggiungi un listener per cercare le informazioni sull'evento quando sarà tornata la connessione ad Internet.
                callback.registerNetworkCallback();
                callback.addDefaultNetworkActiveListener(() ->
                        requestEvInfoIscr(f, userJwt, data, eventId, loginLauncher,
                                c));
                callback.unregisterNetworkCallback();
                setNoConnectionDialog(noconn, noconnmsg);
            }
        }
    }

    public void getEventInfo(@NonNull String which, @NonNull String eventId,
                            @NonNull Fragment f, @Nullable String userJwt,
                            @Nullable ActivityResultLauncher<Intent> loginLauncher,
                            @NotNull Class<? extends Activity> c,
                            @StringRes int noconn, @StringRes int noconnmsg) {
        Activity activity = f.getActivity();
        if(activity != null && f.isAdded()) {
            callback = new NetworkCallback(f.requireActivity());
            if(callback.isOnline(f.requireActivity())) {
                requestEventInfo(which, eventId, f, userJwt, loginLauncher, c);
            } else {
                //Aggiungi un listener per cercare le informazioni sull'evento quando sarà tornata la connessione ad Internet.
                callback.registerNetworkCallback();
                callback.addDefaultNetworkActiveListener(() ->
                        requestEventInfo(which, eventId, f, userJwt, loginLauncher, c));
                callback.unregisterNetworkCallback();
                setNoConnectionDialog(noconn, noconnmsg);
            }
        }
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
                setNoConnectionDialog(noconn, noconnmsg);
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
                        showDialog(R.string.deletion_successful, R.string.deletion_successful_message);
                    }

                    @Override
                    public void onError(int statusCode, String errorMessage) {
                        showDialog(R.string.internal_server_error, R.string.internal_server_error);
                    }
                });
                repo.deleteTicket(eventId, ticketId, data, ora);
            } else {
                setNoConnectionDialog(noconn, noconnmsg);
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
                setNoConnectionDialog(noconn, noconnmsg);
            }
        }

        return null;
    }
}