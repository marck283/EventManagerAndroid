package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details;

import android.content.Intent;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.journeyapps.barcodescanner.ScanOptions;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.checkQRCode.CheckQRCode;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.organizedEvent.OrganizedEventInfo;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.publicEvent.EventInfoCall;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.registeredEvent.RegisteredEventInfo;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.registeredEvent.ticket.delete_ticket.DeleteTicket;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.user_event_registration.UserEventRegistration;

public class EventDetailsViewModel extends ViewModel {
    public void getEventInfo(@NonNull String which, @NonNull String eventId, @NonNull View view,
                             @NonNull EventDetailsFragment f, @Nullable String userJwt,
                             @Nullable String data, @Nullable ActivityResultLauncher<ScanOptions> launcher,
                             @Nullable ActivityResultLauncher<Intent> loginLauncher) {
        EventInfoCall c = new EventInfoCall();
        switch(which) {
            case "pub": {
                c.getEventInfo("pub", eventId, view, f, null);
                break;
            }
            case "iscr": {
                if(userJwt != null && data != null) {
                    RegisteredEventInfo info = new RegisteredEventInfo(userJwt, eventId, f, view, data);
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

    public void registerUser(@NonNull String accessToken, @NonNull String eventId, @NonNull EventDetailsFragment f,
                             @NonNull String day, @NonNull String time) {
        UserEventRegistration uer = new UserEventRegistration();
        uer.registerUser(accessToken, eventId, day, time, f);
    }

    public void deleteTicket(@NonNull String accessToken, @NonNull String ticketId,
                             @NonNull String eventId, @NonNull Fragment f) {
        DeleteTicket delete = new DeleteTicket(eventId, ticketId, accessToken, f);
        delete.start();
    }

    public void checkQR(@NonNull String userJwt, @NonNull String qrCode, @NonNull String eventId,
                        @NonNull String day, @NonNull String hour, @NonNull EventDetailsFragment f) {
        CheckQRCode check = new CheckQRCode(userJwt, qrCode, eventId, day, hour, f);
        check.start();
    }
}