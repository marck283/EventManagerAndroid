package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.event_creation;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Looper;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.io.IOException;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.EventViewModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EventCreation extends Thread {
    private final String userJwt;

    private final EventViewModel evm;

    private final OkHttpClient client;

    private final Fragment f;

    private final ActivityResultLauncher<Intent> i;
    private final Intent loginIntent;

    public EventCreation(@NonNull Fragment f, @NonNull String jwt, @NonNull EventViewModel evm) {
        userJwt = jwt;
        this.evm = evm;
        client = new OkHttpClient();
        this.f = f;
        i = null;
        loginIntent = null;
    }

    public EventCreation(@NonNull Fragment f, @NonNull String jwt, @NonNull EventViewModel evm,
                         @NonNull ActivityResultLauncher<Intent> i, @NonNull Intent loginIntent) {
        userJwt = jwt;
        this.evm = evm;
        client = new OkHttpClient();
        this.f = f;
        this.i = i;
        this.loginIntent = loginIntent;
    }

    private void setAlertDialog(@StringRes int title, @StringRes int message) {
        Looper.prepare();
        AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
        dialog.setTitle(title);
        dialog.setMessage(f.getString(message));
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> {
            dialog1.dismiss();
            f.requireActivity().finish();
        });
        dialog.show();
        Looper.loop();
        Looper.getMainLooper().quitSafely();
    }

    public void run() {
        JSONObject jsonObject = evm.toJson();
        String url;

        if(!evm.getPrivEvent()) {
            url = "https://eventmanagerzlf.herokuapp.com/api/v2/EventiPubblici";
        } else {
            url = "https://eventmanagerzlf.herokuapp.com/api/v2/EventiPrivati";
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                jsonObject.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("x-access-token", userJwt)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //Nulla qui...
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                switch(response.code()) {
                    case 201: {
                        setAlertDialog(R.string.event_creation_ok_title, R.string.event_creation_ok_message);
                        break;
                    }

                    case 401: {
                        //setAlertDialog(R.string.unauthorized, R.string.log_in_to_authorize);
                        if(i != null) {
                            i.launch(loginIntent);
                        }
                        break;
                    }

                    case 500: {
                        setAlertDialog(R.string.internal_server_error, R.string.retry_later);
                        break;
                    }
                }
            }
        });
    }
}
