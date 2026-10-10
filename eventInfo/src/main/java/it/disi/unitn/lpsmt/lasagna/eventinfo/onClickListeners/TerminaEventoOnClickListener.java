package it.disi.unitn.lpsmt.lasagna.eventinfo.onClickListeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import it.disi.unitn.lpsmt.lasagna.eventinfo.EventDetailsViewModel;
import it.disi.unitn.lpsmt.lasagna.eventinfo.R;
import it.disi.unitn.lpsmt.lasagna.network.NetworkCallback;

public class TerminaEventoOnClickListener implements View.OnClickListener {

    private final TextInputLayout spinner, spinner2;

    private final Fragment f;

    private String day;

    private final EventDetailsViewModel mViewModel;

    private final String token, eventId;

    private final NetworkCallback callback;

    private final ActivityResultLauncher<Intent> loginLauncher;

    private final Intent loginIntent;

    private final int orgHourTextView;

    public TerminaEventoOnClickListener(@NotNull TextInputLayout s, @NotNull TextInputLayout s2,
                                        @NotNull Fragment f, @NotNull EventDetailsViewModel vm,
                                        @NotNull String token, @NotNull String evId,
                                        @NotNull NetworkCallback callback,
                                        @NotNull ActivityResultLauncher<Intent> loginLauncher,
                                        @NotNull Intent loginIntent, @IdRes int orgHourTextView) {
        if(token.isEmpty() || evId.isEmpty()) {
            throw new IllegalArgumentException("Nessun argomento fornito a questo costruttore puo' " +
                    "essere una stringa vuota.");
        }
        spinner = s;
        spinner2 = s2;
        this.f = f;
        mViewModel = vm;
        this.token = token;
        eventId = evId;
        this.callback = callback;
        this.loginLauncher = loginLauncher;
        this.loginIntent = loginIntent;
        this.orgHourTextView = orgHourTextView;
    }

    @Override
    public void onClick(View view) {
        MaterialAutoCompleteTextView hourTextView = spinner.findViewById(orgHourTextView);
        Activity activity1 = f.getActivity();
        EditText editText1 = spinner2.getEditText();
        if(day == null &&
                editText1 != null &&
                !editText1.getText().toString().isEmpty() &&
                !editText1.getText().toString().equals("---")) {
            day = editText1.getText().toString();
            String[] dayArr = day.split("/");
            day = dayArr[1] + "-" + dayArr[0] + "-" + dayArr[2];
        }
        if(activity1 != null && f.isAdded()) {
            if (!callback.isOnline(f.requireActivity())) {
                mViewModel.showDialog(R.string.no_connection, R.string.no_connection_message);
            } else {
                try {
                    if(day == null || day.isEmpty() || day.equals("---")) {
                        return;
                    }
                    mViewModel.terminateEvent(token, f, eventId, day, hourTextView.getText().toString(),
                            loginLauncher, loginIntent);
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
