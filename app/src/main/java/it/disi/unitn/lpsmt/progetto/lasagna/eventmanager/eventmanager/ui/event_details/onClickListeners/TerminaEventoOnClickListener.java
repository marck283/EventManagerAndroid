package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.onClickListeners;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.NetworkCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.EventDetailsFragment;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.EventDetailsViewModel;

public class TerminaEventoOnClickListener implements View.OnClickListener {

    private final TextInputLayout spinner, spinner2;

    private final EventDetailsFragment f;

    private String day;

    private final EventDetailsViewModel mViewModel;

    private final String token, eventId;

    private final NetworkCallback callback;

    private final View v;

    public TerminaEventoOnClickListener(@NotNull TextInputLayout s, @NotNull TextInputLayout s2,
                                        @NotNull EventDetailsFragment f, @NotNull EventDetailsViewModel vm,
                                        @NotNull String token, @NotNull String evId,
                                        @NotNull NetworkCallback callback, @NotNull View v) {
        if(token.equals("") || evId.equals("")) {
            throw new IllegalArgumentException("Nessun argomento fornito a questo costruttore puo' " +
                    "essere null o una stringa vuota.");
        }
        spinner = s;
        spinner2 = s2;
        this.f = f;
        mViewModel = vm;
        this.token = token;
        eventId = evId;
        this.callback = callback;
        this.v = v;
    }

    @Override
    public void onClick(View view) {
        MaterialAutoCompleteTextView hourTextView = spinner.findViewById(R.id.orgHourTextView);
        Activity activity1 = f.getActivity();
        EditText editText1 = spinner2.getEditText();
        if(day == null) {
            if(editText1 != null && !editText1.getText().toString().equals("") &&
                    !editText1.getText().toString().equals("---")) {
                day = editText1.getText().toString();
                String[] dayArr = day.split("/");
                day = dayArr[1] + "-" + dayArr[0] + "-" + dayArr[2];
            }
        }
        if(activity1 != null && f.isAdded()) {
            if (!callback.isOnline(f.requireActivity())) {
                setNoConnectionDialog();
            } else {
                try {
                    //Aggiungere ActivityResultLauncher per ottenere un nuovo token dall'Activity di login.
                    //Ricordarsi anche di aggiornare "token" all'interno del launcher!
                    if(day == null || day.equals("") || day.equals("---")) {
                        return;
                    }
                    mViewModel.terminateEvent(token,
                            f, eventId, day, hourTextView.getText().toString(), v);
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
            }
        }
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
}
