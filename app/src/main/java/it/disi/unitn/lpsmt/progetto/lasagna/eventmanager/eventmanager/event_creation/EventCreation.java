package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.event_creation;

import android.app.AlertDialog;

import androidx.annotation.NonNull;
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
    private String userJwt = "";

    private EventViewModel evm;

    private OkHttpClient client;

    private Fragment f;

    public EventCreation(@NonNull Fragment f, @NonNull String jwt, @NonNull EventViewModel evm) {
        userJwt = jwt;
        this.evm = evm;
        client = new OkHttpClient();
        this.f = f;
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
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                switch(response.code()) {
                    case 200: {
                        AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
                        dialog.setTitle(R.string.event_creation_ok_title);
                        dialog.setMessage(f.getString(R.string.event_creation_ok_message));
                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> {
                            dialog1.dismiss();
                            f.requireActivity().finish();
                        });
                        dialog.show();
                        break;
                    }

                    case 401: {
                        AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
                        dialog.setTitle(R.string.unauthorized);
                        dialog.setMessage(f.getString(R.string.log_in_to_authorize));
                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> {
                            dialog1.dismiss();
                            f.requireActivity().finish();
                        });
                        dialog.show();
                        break;
                    }

                    case 500: {
                        AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
                        dialog.setTitle(R.string.internal_server_error);
                        dialog.setMessage(f.getString(R.string.retry_later));
                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> {
                            dialog1.dismiss();
                            f.requireActivity().finish();
                        });
                        dialog.show();
                        break;
                    }
                }
            }
        });
    }
}
