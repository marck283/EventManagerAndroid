package it.disi.unitn.lpsmt.lasagna.eventinfo.onClickListeners;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import it.disi.lasagna.navigationsvm.NavigationSharedViewModel;
import it.disi.unitn.lpsmt.lasagna.eventinfo.EventDetailsViewModel;
import it.disi.unitn.lpsmt.lasagna.network.NetworkCallback;

public class AnnullaEventoOnClickListener implements View.OnClickListener {

    private final Fragment f;

    private final NavigationSharedViewModel nvm;

    private final NetworkCallback callback;

    private final EventDetailsViewModel mViewModel;

    private final String eventId;

    private final int noconn, noconnmsg;

    public AnnullaEventoOnClickListener(@NotNull Fragment f, @NotNull NavigationSharedViewModel nvm,
                                        @NotNull NetworkCallback callback, @NotNull EventDetailsViewModel vm,
                                        @NotNull String eventId, @StringRes int noconn,
                                        @StringRes int noconnmsg) {
        if(eventId.equals("")) {
            throw new IllegalArgumentException("Nessun argomento fornito a questo costruttore puo' " +
                    "essere una stringa vuota.");
        }

        this.f = f;
        this.nvm = nvm;
        this.callback = callback;
        mViewModel = vm;
        this.eventId = eventId;
        this.noconn = noconn;
        this.noconnmsg = noconnmsg;
    }

    private void setNoConnectionDialog() {
        Activity activity = f.getActivity();
        if(activity != null && f.isAdded()) {
            AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
            dialog.setTitle(noconn);
            dialog.setMessage(f.getString(noconn));
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
            mViewModel.deleteEvent(Objects.requireNonNull(nvm.getToken().getValue()), eventId, f,
                    noconn, noconnmsg);
        }
    }
}
