package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.onClickListeners;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;

import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.lasagna.sharedprefs.sharedpreferences.SharedPrefs;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.EventDetailsFragment;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.EventDetailsViewModel;

public class SignUpOnClickListener implements View.OnClickListener {

    private final SharedPrefs prefs;

    private final String eventId;

    private String day, time;

    private final EventDetailsViewModel mViewModel;

    private final ActivityResultLauncher<Intent> loginLauncher;

    private final EventDetailsFragment f;

    public SignUpOnClickListener(@NotNull SharedPrefs prefs, @NotNull String eventId,
                                 @NotNull String day, @NotNull String time, @NotNull EventDetailsViewModel vm,
                                 @NotNull ActivityResultLauncher<Intent> launcher,
                                 @NotNull EventDetailsFragment f) {
        if(eventId.equals("") || day.equals("") || time.equals("")) {
            throw new IllegalArgumentException("Nessun argomento fornito a questo costruttore puo' essere" +
                    " una stringa vuota.");
        }
        this.prefs = prefs;
        this.eventId = eventId;
        this.day = day;
        this.time = time;
        mViewModel = vm;
        loginLauncher = launcher;
        this.f = f;
    }

    @Override
    public void onClick(View view) {
        TextInputLayout spinner = view.findViewById(R.id.spinner), spinner2 = view.findViewById(R.id.dateArray);
        EditText spinnerText = spinner.getEditText(), spinner2Text = spinner2.getEditText();
        String token = prefs.getString("accessToken");
        if (eventId != null && spinnerText != null && !spinnerText.getText().toString().equals("")
                && !spinnerText.getText().toString().equals("---") &&
                spinner2Text != null && !spinner2Text.getText().toString().equals("") &&
                !spinner2Text.getText().toString().equals("---")) {
            String[] dayArr = spinnerText.getText().toString().split("/");
            day = dayArr[1] + "-" + dayArr[0] + "-" + dayArr[2];
            time = spinner2Text.getText().toString();
            mViewModel.registerUser(token, eventId, f, day, time, loginLauncher);
        }
    }
}
