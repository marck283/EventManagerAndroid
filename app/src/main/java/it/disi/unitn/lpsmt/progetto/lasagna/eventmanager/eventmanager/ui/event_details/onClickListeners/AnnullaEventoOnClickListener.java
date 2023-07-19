package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.onClickListeners;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.NetworkCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.NavigationSharedViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.EventDetailsFragment;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.EventDetailsViewModel;

public class AnnullaEventoOnClickListener implements View.OnClickListener {

    private final EventDetailsFragment f;

    private final NavigationSharedViewModel nvm;

    private final NetworkCallback callback;

    private final EventDetailsViewModel mViewModel;

    private final String eventId;

    public AnnullaEventoOnClickListener(@NotNull EventDetailsFragment f, @NotNull NavigationSharedViewModel nvm,
                                        @NotNull NetworkCallback callback, @NotNull EventDetailsViewModel vm,
                                        @NotNull String eventId) {
        if(f == null || nvm == null || callback == null || vm == null || eventId == null || eventId.equals("")) {
            throw new IllegalArgumentException("Nessun argomento fornito a questo costruttore puo' " +
                    "essere null o una stringa vuota.");
        }

        this.f = f;
        this.nvm = nvm;
        this.callback = callback;
        mViewModel = vm;
        this.eventId = eventId;
    }

    private void setNoConnectionDialog() {
        Activity activity = f.getActivity();
        if(activity != null && f.isAdded()) {
            AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
            dialog.setTitle(R.string.no_connection);
            dialog.setMessage(f.getString(R.string.no_connection_message));
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
            dialog.show();
        }
    }

    @Override
    public void onClick(View view) {
        Activity activity2 = f.getActivity();
        if(activity2 != null && f.isAdded()) {
            if (!callback.isOnline(f.requireActivity())) {
                setNoConnectionDialog();
                return;
            }
            //Aggiungere ActivityResultLauncher per ottenere un nuovo token dall'Activity di login.
            //Ricordarsi anche di aggiornare "token" all'interno del launcher!
            mViewModel.deleteEvent(Objects.requireNonNull(nvm.getToken().getValue()), eventId, f);
        }
    }
}
