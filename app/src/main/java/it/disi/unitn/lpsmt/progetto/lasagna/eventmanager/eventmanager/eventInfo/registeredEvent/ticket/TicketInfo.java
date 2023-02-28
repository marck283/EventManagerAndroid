package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.registeredEvent.ticket;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.networkRequests.NetworkRequest;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class TicketInfo extends Thread {
    private final String eventId, userId, data, ora;
    private final View v;

    private final Fragment f;

    private final NetworkRequest request;

    public TicketInfo(@NonNull Fragment f, @NonNull View v, @NonNull String eventId,
                      @NonNull String userId, @NonNull String data, @NonNull String ora) {
        request = new NetworkRequest();
        this.eventId = eventId;
        this.userId = userId;
        this.data = data;
        this.ora = ora;
        this.v = v;
        this.f = f;
    }

    public void setAlertDialog(@StringRes int title, @StringRes int message) {
        Activity activity = f.getActivity();
        if(activity != null && f.isAdded()) {
            f.requireActivity().runOnUiThread(() -> {
                AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
                dialog.setTitle(title);
                dialog.setMessage(f.getString(message));
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                dialog.show();
            });
        }
    }

    public void run() {
        List<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<>("x-access-token", userId));
        headers.add(new Pair<>("giorno", data));
        headers.add(new Pair<>("ora", ora));
        Request req = request.getRequest(headers, "https://eventmanagerzlf.herokuapp.com/api/v2/ticket/" + eventId);
        request.enqueue(req, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                try {
                    throw e;
                } catch(IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.body() != null && response.isSuccessful()) {
                    Gson gson = new GsonBuilder().create();
                    Ticket ticket = Ticket.parseJSON(gson.fromJson(response.body().string(), JsonObject.class));

                    Activity activity = f.getActivity();
                    if(activity != null && f.isAdded()) {
                        f.requireActivity().runOnUiThread(() -> {
                            try {
                                ImageView imageViewQrCode = v.findViewById(R.id.qrCode);
                                Glide.with(v).load(ticket.getQR()).into(imageViewQrCode);
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    response.body().close();
                } else {
                    switch(response.code()) {
                        case 400: {
                            setAlertDialog(R.string.malformed_request_or_invalid_date, R.string.malformed_request_or_invalid_date_message);
                            break;
                        }
                        case 401: {
                            setAlertDialog(R.string.user_not_logged_in, R.string.user_not_logged_in_message);
                            break;
                        }
                        case 404: {
                            setAlertDialog(R.string.no_ticket, R.string.no_ticket_message);
                            break;
                        }
                    }
                }
            }
        });
    }
}
