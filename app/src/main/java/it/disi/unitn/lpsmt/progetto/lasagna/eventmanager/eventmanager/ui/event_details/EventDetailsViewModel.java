package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.journeyapps.barcodescanner.ScanOptions;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.checkQRCode.CheckQRCode;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.organizedEvent.OrganizedEventInfo;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.publicEvent.EventInfoCall;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.registeredEvent.RegisteredEventInfo;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.registeredEvent.ticket.delete_ticket.DeleteTicket;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.NetworkCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.user_event_registration.UserEventRegistration;

public class EventDetailsViewModel extends ViewModel {
    private NetworkCallback callback;

    private void setNoConnectionDialog(@NonNull EventDetailsFragment f) {
        AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
        dialog.setTitle(R.string.no_connection);
        dialog.setMessage(f.getString(R.string.no_connection_message));
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which1) -> dialog1.dismiss());
        dialog.show();
    }

    private void requestEventInfo(@NonNull String which, @NonNull String eventId, @NonNull View view,
                                  @NonNull EventDetailsFragment f, @Nullable String userJwt,
                                  @Nullable String data, @Nullable ActivityResultLauncher<ScanOptions> launcher,
                                  @Nullable ActivityResultLauncher<Intent> loginLauncher,
                                  @Nullable ActivityResultLauncher<Intent> loginLauncherTicket) {
        switch(which) {
            case "pub": {
                EventInfoCall c = new EventInfoCall(eventId, view, f);
                c.start();
                break;
            }
            case "iscr": {
                if(userJwt != null && data != null && loginLauncher != null) {
                    RegisteredEventInfo info = new RegisteredEventInfo(userJwt, eventId, f, view, data,
                            loginLauncher, loginLauncherTicket);
                    info.start();
                }
                break;
            }
            case "org": {
                if(userJwt != null && launcher != null) {
                    OrganizedEventInfo orgEvInfo;
                    if(data != null) {
                        //La data di un evento è inclusa solo quando la richiesta parte dal calendario dell'utente.
                        orgEvInfo = new OrganizedEventInfo(view, f, userJwt, eventId, data, launcher);
                    } else {
                        if(loginLauncher != null) {
                            //n questo caso, ci potrebbe essere il rischio che l'utente non sia autenticato al sistema.
                            //Questo problema è risolto aggiungendo un ActivityResultLauncher che permette l'avvio
                            //dell'Activity di login e, una volta ricevuto il risultato, esegue di nuovo la
                            //richiesta al server.
                            orgEvInfo = new OrganizedEventInfo(view, f, userJwt, eventId, launcher, loginLauncher);
                        } else {
                            //La richiesta al server viene eseguita di nuovo passando per questa riga di codice.
                            orgEvInfo = new OrganizedEventInfo(view, f, userJwt, eventId, launcher);
                        }
                    }
                    orgEvInfo.start();
                }
                break;
            }
        }
    }

    public void getEventInfo(@NonNull String which, @NonNull String eventId, @NonNull View view,
                             @NonNull EventDetailsFragment f, @Nullable String userJwt,
                             @Nullable String data, @Nullable ActivityResultLauncher<ScanOptions> launcher,
                             @Nullable ActivityResultLauncher<Intent> loginLauncher,
                             @Nullable ActivityResultLauncher<Intent> loginLauncherTicket) {
        callback = new NetworkCallback(f.requireActivity());
        if(callback.isOnline(f.requireActivity())) {
            requestEventInfo(which, eventId, view, f, userJwt, data, launcher, loginLauncher, loginLauncherTicket);
        } else {
            //Aggiungi un listener per cercare le informazioni sull'evento quando sarà tornata la connessione ad Internet.
            callback.registerNetworkCallback();
            callback.addDefaultNetworkActiveListener(() ->
                            requestEventInfo(which, eventId, view, f, userJwt, data, launcher,
                                    loginLauncher, loginLauncherTicket));
            callback.unregisterNetworkCallback();
            setNoConnectionDialog(f);
        }
    }

    public void registerUser(@NonNull String accessToken, @NonNull String eventId, @NonNull EventDetailsFragment f,
                             @NonNull String day, @NonNull String time, @NonNull ActivityResultLauncher<Intent> launcher) {
        callback = new NetworkCallback(f.requireActivity());
        if(callback.isOnline(f.requireActivity())) {
            UserEventRegistration uer = new UserEventRegistration(accessToken, eventId, day, time, f, launcher);
            uer.start();
        } else {
            setNoConnectionDialog(f);
        }
    }

    public void deleteTicket(@NonNull String accessToken, @NonNull String ticketId,
                             @NonNull String eventId, @NonNull EventDetailsFragment f,
                             @Nullable ActivityResultLauncher<Intent> loginLauncher) {
        callback = new NetworkCallback(f.requireActivity());
        if(callback.isOnline(f.requireActivity())) {
            DeleteTicket delete;
            if(loginLauncher != null) {
                delete = new DeleteTicket(eventId, ticketId, accessToken, f, loginLauncher);
            } else {
                delete = new DeleteTicket(eventId, ticketId, accessToken, f);
            }
            delete.start();
        } else {
            setNoConnectionDialog(f);
        }
    }

    public void checkQR(@NonNull String userJwt, @NonNull String qrCode, @NonNull String eventId,
                        @NonNull String day, @NonNull String hour, @NonNull EventDetailsFragment f) {
        callback = new NetworkCallback(f.requireActivity());
        if(callback.isOnline(f.requireActivity())) {
            CheckQRCode check = new CheckQRCode(userJwt, qrCode, eventId, day, hour, f);
            check.start();
        } else {
            setNoConnectionDialog(f);
        }
    }
}