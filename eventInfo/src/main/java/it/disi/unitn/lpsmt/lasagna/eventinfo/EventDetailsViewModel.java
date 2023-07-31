package it.disi.unitn.lpsmt.lasagna.eventinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.journeyapps.barcodescanner.ScanOptions;

import it.disi.unitn.lpsmt.lasagna.checkqrcode.CheckQRCode;
import it.disi.unitn.lpsmt.lasagna.network.NetworkCallback;
import it.disi.unitn.lpsmt.lasagna.eventinfo.organizedEvent.DeleteEvent;
import it.disi.unitn.lpsmt.lasagna.eventinfo.organizedEvent.OrganizedEventInfo;
import it.disi.unitn.lpsmt.lasagna.eventinfo.organizedEvent.TerminateEvent;
import it.disi.unitn.lpsmt.lasagna.eventinfo.publicEvent.EventInfoCall;
import it.disi.unitn.lpsmt.lasagna.eventinfo.registeredEvent.RegisteredEventInfo;
import it.disi.unitn.lpsmt.lasagna.eventinfo.registeredEvent.ticket.delete_ticket.DeleteTicket;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.user_event_registration.UserEventRegistration;

public class EventDetailsViewModel extends ViewModel {
    private NetworkCallback callback;

    private void setNoConnectionDialog(@NonNull EventDetailsFragment f) {
        Activity activity = f.getActivity();
        if(activity != null && f.isAdded()) {
            AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
            dialog.setTitle(R.string.no_connection);
            dialog.setMessage(f.getString(R.string.no_connection_message));
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which1) -> dialog1.dismiss());
            dialog.show();
        }
    }

    public void terminateEvent(@NonNull String accessToken, @NonNull EventDetailsFragment f,
                               @NonNull String eventId, @NonNull String data, @NonNull String ora,
                               @NonNull View v, @NonNull ActivityResultLauncher<Intent> loginLauncher,
                               @NonNull Intent loginIntent) {
        Activity activity = f.getActivity();
        if(activity != null && f.isAdded()) {
            callback = new NetworkCallback(f.requireActivity());
            if(callback.isOnline(f.requireActivity())) {
                TerminateEvent terminate = new TerminateEvent(accessToken, eventId, data, ora, v, f,
                        loginLauncher, loginIntent);
                terminate.start();
            } else {
                setNoConnectionDialog(f);
            }
        }
    }

    public void deleteEvent(@NonNull String accessToken, @NonNull String eventId, @NonNull EventDetailsFragment f) {
        Activity activity = f.getActivity();
        if(activity != null && f.isAdded()) {
            if(callback.isOnline(f.requireActivity())) {
                DeleteEvent deleteEvent = new DeleteEvent(accessToken, eventId, f);
                deleteEvent.start();
            } else {
                setNoConnectionDialog(f);
            }
        }
    }

    private void requestEventInfo(@NonNull String which, @NonNull String eventId, @NonNull View view,
                                  @NonNull EventDetailsFragment f, @Nullable String userJwt,
                                  @Nullable String data, @Nullable ActivityResultLauncher<ScanOptions> launcher,
                                  @Nullable ActivityResultLauncher<Intent> loginLauncher) {
        switch (which) {
            case "pub" -> {
                EventInfoCall c = new EventInfoCall(eventId, view, f);
                c.start();
            }
            case "iscr" -> {
                if (userJwt != null && data != null && loginLauncher != null) {
                    RegisteredEventInfo info = new RegisteredEventInfo(userJwt, eventId, f, view, data, loginLauncher);
                    info.start();
                }
            }
            case "org" -> {
                if (userJwt != null && launcher != null) {
                    OrganizedEventInfo orgEvInfo;
                    if (data != null) {
                        //La data di un evento è inclusa solo quando la richiesta parte dal calendario dell'utente.
                        orgEvInfo = new OrganizedEventInfo(view, f, userJwt, eventId, data);
                    } else {
                        if (loginLauncher != null) {
                            //n questo caso, ci potrebbe essere il rischio che l'utente non sia autenticato al sistema.
                            //Questo problema è risolto aggiungendo un ActivityResultLauncher che permette l'avvio
                            //dell'Activity di login e, una volta ricevuto il risultato, esegue di nuovo la
                            //richiesta al server.
                            orgEvInfo = new OrganizedEventInfo(view, f, userJwt, eventId, loginLauncher);
                        } else {
                            //La richiesta al server viene eseguita di nuovo passando per questa riga di codice.
                            orgEvInfo = new OrganizedEventInfo(view, f, userJwt, eventId);
                        }
                    }
                    orgEvInfo.start();
                }
            }
        }
    }

    public Void getEventInfo(@NonNull String which, @NonNull String eventId, @NonNull View view,
                               @NonNull EventDetailsFragment f, @Nullable String userJwt,
                               @Nullable String data, @Nullable ActivityResultLauncher<ScanOptions> launcher,
                               @Nullable ActivityResultLauncher<Intent> loginLauncher) {
        Activity activity = f.getActivity();
        if(activity != null && f.isAdded()) {
            callback = new NetworkCallback(f.requireActivity());
            if(callback.isOnline(f.requireActivity())) {
                requestEventInfo(which, eventId, view, f, userJwt, data, launcher, loginLauncher);
            } else {
                //Aggiungi un listener per cercare le informazioni sull'evento quando sarà tornata la connessione ad Internet.
                callback.registerNetworkCallback();
                callback.addDefaultNetworkActiveListener(() ->
                        requestEventInfo(which, eventId, view, f, userJwt, data, launcher,
                                loginLauncher));
                callback.unregisterNetworkCallback();
                setNoConnectionDialog(f);
            }
        }
        return null;
    }

    public void registerUser(@NonNull String accessToken, @NonNull String eventId, @NonNull EventDetailsFragment f,
                             @NonNull String day, @NonNull String time, @Nullable ActivityResultLauncher<Intent> launcher) {
        Activity activity = f.getActivity();
        if(activity != null && f.isAdded()) {
            callback = new NetworkCallback(f.requireActivity());
            if(callback.isOnline(f.requireActivity())) {
                UserEventRegistration uer = new UserEventRegistration(accessToken, eventId, day, time, f, launcher);
                uer.start();
            } else {
                setNoConnectionDialog(f);
            }
        }
    }

    public void deleteTicket(@NonNull String accessToken, @NonNull String ticketId,
                             @NonNull String eventId, @NonNull EventDetailsFragment f,
                             @NonNull String data, @NonNull String ora) {
        Activity activity = f.getActivity();
        if(activity != null && f.isAdded()) {
            callback = new NetworkCallback(f.requireActivity());
            if(callback.isOnline(f.requireActivity())) {
                DeleteTicket delete = new DeleteTicket(eventId, ticketId, accessToken, f, data, ora);
                delete.start();
            } else {
                setNoConnectionDialog(f);
            }
        }
    }

    public Void checkQR(@NonNull String userJwt, @NonNull String qrCode, @NonNull String eventId,
                        @NonNull String day, @NonNull String hour, @NonNull EventDetailsFragment f) {
        Activity activity = f.getActivity();
        if(activity != null && f.isAdded()) {
            callback = new NetworkCallback(f.requireActivity());
            if(callback.isOnline(f.requireActivity())) {
                String[] dataArr = day.split("/");
                day = dataArr[1] + "-" + dataArr[0] + "-" + dataArr[2];
                CheckQRCode check = new CheckQRCode(userJwt, qrCode, eventId, day, hour, f);
                check.start();
            } else {
                setNoConnectionDialog(f);
            }
        }

        return null;
    }
}