package it.disi.unitn.lpsmt.lasagna.eventinfo.organizedEvent;

import android.content.Intent;
import android.util.Pair;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.lasagna.eventinfo.callbacks.TerminatorCallback;
import it.disi.unitn.lpsmt.lasagna.network.NetworkRequest;
import it.disi.unitn.lpsmt.lasagna.network.networkOps.ServerOperation;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class TerminateEvent extends ServerOperation {

    private final NetworkRequest request;

    private final String eventId, accessToken, data, ora;

    private final View v;

    private final Fragment f;

    private final ActivityResultLauncher<Intent> launcher;

    private final Intent loginIntent;

    private final int malformed_req, malf_req_msg, no_session_title, no_session_msg, attempt_ok,
    attempt_ok_msg, but8, but12, internal_server_error;

    public TerminateEvent(@NonNull String accessToken, @NonNull String eventId, @NonNull String data,
                          @NonNull String ora, @NonNull View v, @NonNull Fragment f,
                          @NonNull ActivityResultLauncher<Intent> launcher, @NonNull Intent loginIntent,
                          @StringRes int malformed_req,
                          @StringRes int malf_req_msg, @StringRes int no_session_title,
                          @StringRes int no_session_content, @StringRes int attempt_ok,
                          @StringRes int attempt_ok_msg, @IdRes int but8, @IdRes int but12,
                          @StringRes int internal_server_error) {
        request = new NetworkRequest();
        this.eventId = eventId;
        this.data = data;
        this.ora = ora;
        this.v = v;
        this.f = f;
        this.accessToken = accessToken;
        this.launcher = launcher;
        this.loginIntent = loginIntent;
        this.malformed_req = malformed_req;
        this.malf_req_msg = malf_req_msg;
        this.no_session_title = no_session_title;
        this.no_session_msg = no_session_content;
        this.attempt_ok = attempt_ok;
        this.attempt_ok_msg = attempt_ok_msg;
        this.but8 = but8;
        this.but12 = but12;
        this.internal_server_error = internal_server_error;
    }

    public void run() {
        RequestBody body = new FormBody.Builder()
                .add("data", data)
                .add("ora", ora)
                .build();
        List<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<>("x-access-token", accessToken));
        Request req = request.getPatchRequest(body, headers,
                getBaseUrl() + "/api/v2/terminaEvento/" + eventId);
        request.enqueue(req, new TerminatorCallback(f, launcher, loginIntent, v, malformed_req,
                malf_req_msg, no_session_title, no_session_msg, attempt_ok, attempt_ok_msg, but8,
                but12, internal_server_error));
    }
}
