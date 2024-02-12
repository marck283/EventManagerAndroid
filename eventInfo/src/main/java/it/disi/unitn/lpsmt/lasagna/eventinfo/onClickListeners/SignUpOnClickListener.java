package it.disi.unitn.lpsmt.lasagna.eventinfo.onClickListeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import it.disi.unitn.lpsmt.lasagna.eventinfo.EventDetailsViewModel;
import it.disi.unitn.lpsmt.lasagna.sharedprefs.sharedpreferences.SharedPrefs;

public class SignUpOnClickListener implements View.OnClickListener {

    private final SharedPrefs prefs;

    private final String eventId;

    private String day, time;

    private final EventDetailsViewModel mViewModel;

    private final ActivityResultLauncher<Intent> loginLauncher;

    private final Fragment f;

    private final int noconn, noconnmsg, spinner, dateArray;

    private final Class<? extends Activity> c;

    public SignUpOnClickListener(@NotNull SharedPrefs prefs, @NotNull String eventId,
                                 @NotNull String day, @NotNull String time, @NotNull EventDetailsViewModel vm,
                                 @NotNull ActivityResultLauncher<Intent> launcher,
                                 @NotNull Fragment f, @StringRes int noconn, @StringRes int noconnmsg,
                                 @IdRes int spinner, @IdRes int dateArray, @NotNull Class<? extends Activity> c) {
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
        this.noconn = noconn;
        this.noconnmsg = noconnmsg;
        this.spinner = spinner;
        this.dateArray = dateArray;
        this.c = c;
    }

    @Override
    public void onClick(View view) {
        TextInputLayout spinner1 = view.findViewById(spinner), spinner2 = view.findViewById(dateArray);
        EditText spinnerText = spinner1.getEditText(), spinner2Text = spinner2.getEditText();
        String token = prefs.getString("accessToken");
        if (eventId != null && spinnerText != null && !spinnerText.getText().toString().equals("")
                && !spinnerText.getText().toString().equals("---") &&
                spinner2Text != null && !spinner2Text.getText().toString().equals("") &&
                !spinner2Text.getText().toString().equals("---")) {
            String[] dayArr = spinnerText.getText().toString().split("/");
            day = dayArr[1] + "-" + dayArr[0] + "-" + dayArr[2];
            time = spinner2Text.getText().toString();
            mViewModel.registerUser(token, eventId, f, day, time, loginLauncher, noconn, noconnmsg, c);
        }
    }
}
