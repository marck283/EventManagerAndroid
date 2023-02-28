package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.event_creation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Pair;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.NetworkRequest;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.EventViewModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EventCreation extends Thread {
    private final String userJwt;

    private final EventViewModel evm;

    private final NetworkRequest request;

    private final Fragment f;

    private final ActivityResultLauncher<Intent> i;
    private final Intent loginIntent;

    public EventCreation(@NonNull Fragment f, @NonNull String jwt, @NonNull EventViewModel evm) {
        userJwt = jwt;
        this.evm = evm;
        request = new NetworkRequest();
        this.f = f;
        i = null;
        loginIntent = null;
    }

    public EventCreation(@NonNull Fragment f, @NonNull String jwt, @NonNull EventViewModel evm,
                         @NonNull ActivityResultLauncher<Intent> i, @NonNull Intent loginIntent) {
        userJwt = jwt;
        this.evm = evm;
        request = new NetworkRequest();
        this.f = f;
        this.i = i;
        this.loginIntent = loginIntent;
    }

    private void setAlertDialog(@StringRes int title, @StringRes int message) {
        Activity activity = f.getActivity();
        if(activity != null && f.isAdded()) {
            f.requireActivity().runOnUiThread(() -> {
                AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
                dialog.setTitle(title);
                dialog.setMessage(f.getString(message));
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> {
                    dialog1.dismiss();
                    f.requireActivity().finish();
                });
                dialog.show();
            });
        }
    }

    public void run() {
        JSONObject jsonObject = evm.toJson();
        String url;

        if (!evm.getPrivEvent()) {
            url = "https://eventmanagerzlf.herokuapp.com/api/v2/EventiPubblici";
        } else {
            url = "https://eventmanagerzlf.herokuapp.com/api/v2/EventiPrivati";
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                jsonObject.toString());

        List<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<>("x-access-token", userJwt));
        Request req = request.getPostRequest(body, headers, url);
        request.enqueue(req, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                try {
                    throw e;
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                switch (response.code()) {
                    case 201: {
                        setAlertDialog(R.string.event_creation_ok_title, R.string.event_creation_ok_message);
                        break;
                    }

                    case 400: {
                        setAlertDialog(R.string.event_creation_error, R.string.event_creation_error_message);
                        break;
                    }

                    case 401: {
                        if (i != null) {
                            i.launch(loginIntent);
                        }
                        break;
                    }

                    case 500: {
                        setAlertDialog(R.string.internal_server_error, R.string.retry_later);
                        break;
                    }

                    case 503: {
                        setAlertDialog(R.string.service_unavailable, R.string.service_unavailable_message);
                        break;
                    }
                }
            }
        });
    }
}
