package it.disi.unitn.lpsmt.lasagna.eventinfo.onClickListeners;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import it.disi.unitn.lpsmt.lasagna.eventinfo.EventDetailsViewModel;
import it.disi.unitn.lpsmt.lasagna.network.NetworkCallback;

public class TerminaEventoOnClickListener implements View.OnClickListener {

    private final TextInputLayout spinner, spinner2;

    private final Fragment f;

    private String day;

    private final EventDetailsViewModel mViewModel;

    private final String token, eventId;

    private final NetworkCallback callback;

    private final View v;

    private final ActivityResultLauncher<Intent> loginLauncher;

    private final Intent loginIntent;

    private final int orgHourTextView, noconn, noconnmsg, malformed_req, malf_req_msg, no_session_title,
    no_session_content, attempt_ok, attempt_ok_msg, but8, but12, internal_server_error;

    public TerminaEventoOnClickListener(@NotNull TextInputLayout s, @NotNull TextInputLayout s2,
                                        @NotNull Fragment f, @NotNull EventDetailsViewModel vm,
                                        @NotNull String token, @NotNull String evId,
                                        @NotNull NetworkCallback callback, @NotNull View v,
                                        @NotNull ActivityResultLauncher<Intent> loginLauncher,
                                        @NotNull Intent loginIntent, @IdRes int orgHourTextView,
                                        @StringRes int noconn, @StringRes int noconnmsg,
                                        @StringRes int malformed_req,
                                        @StringRes int malf_req_msg, @StringRes int no_session_title,
                                        @StringRes int no_session_content, @StringRes int attempt_ok,
                                        @StringRes int attempt_ok_msg, @IdRes int but8, @IdRes int but12,
                                        @StringRes int internal_server_error) {
        if(token.equals("") || evId.equals("")) {
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
        this.v = v;
        this.loginLauncher = loginLauncher;
        this.loginIntent = loginIntent;
        this.orgHourTextView = orgHourTextView;
        this.noconn = noconn;
        this.noconnmsg = noconnmsg;
        this.malformed_req = malformed_req;
        this.malf_req_msg = malf_req_msg;
        this.no_session_title = no_session_title;
        this.no_session_content = no_session_content;
        this.attempt_ok = attempt_ok;
        this.attempt_ok_msg = attempt_ok_msg;
        this.but8 = but8;
        this.but12 = but12;
        this.internal_server_error = internal_server_error;
    }

    @Override
    public void onClick(View view) {
        MaterialAutoCompleteTextView hourTextView = spinner.findViewById(orgHourTextView);
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
                            f, eventId, day, hourTextView.getText().toString(), v, loginLauncher,
                            loginIntent, noconn, noconnmsg, malformed_req,
                            malf_req_msg, no_session_title,
                            no_session_content, attempt_ok, attempt_ok_msg,
                            but8, but12, internal_server_error);
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
            dialog.setTitle(noconn);
            dialog.setMessage(f.getString(noconnmsg));
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
            dialog.show();
        }
    }
}
