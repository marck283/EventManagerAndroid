package it.disi.unitn.lpsmt.lasagna.eventinfo.callbacks;

import android.app.Activity;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import it.disi.unitn.lpsmt.lasagna.eventinfo.EventDetailsViewModel;
import it.disi.unitn.lpsmt.lasagna.eventinfo.R;
import it.disi.unitn.lpsmt.lasagna.eventinfo.fields.Field;

public class TerminatorCallback {

    private final Fragment f;

    private final EventDetailsViewModel eventVM;

    private final ActivityResultLauncher<Intent> launcher;

    private final Intent loginIntent;

    public TerminatorCallback(@NotNull Fragment f, @NotNull EventDetailsViewModel eventVM,
                              @NotNull ActivityResultLauncher<Intent> l,
                              @NotNull Intent lintent) {
        this.f = f;
        this.eventVM = eventVM;
        launcher = l;
        loginIntent = lintent;
    }

    public void handleResponseCode(int code) {
        switch(code) {
            case 400 -> eventVM.showDialog(R.string.malformed_request, R.string.malformed_request_message);
            case 401 -> {
                eventVM.showDialog(R.string.no_session_title, R.string.no_session_content);
                Activity a = f.getActivity();
                if (a != null && !a.isFinishing() && !a.isDestroyed() && f.isAdded()) {
                    a.runOnUiThread(() -> {
                        if (!a.isFinishing() && !a.isDestroyed() && f.isAdded()) {
                            launcher.launch(loginIntent);
                        }
                    });
                }
            }
            case 200 -> {
                eventVM.showDialog(R.string.attempt_ok, R.string.attempt_ok_message);
                Field terminated = new Field();
                terminated.setFieldId("eventTerminated");
                terminated.setFieldValue("true");
                eventVM.setField(terminated);
            }
            case 500 -> eventVM.showDialog(R.string.internal_server_error, R.string.internal_server_error);
        }
    }
}
