package it.disi.unitn.lpsmt.lasagna.eventinfo.organizedEvent;

import android.app.AlertDialog;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.lasagna.eventinfo.interfaces.OrgEvInterface;
import it.disi.unitn.lpsmt.lasagna.network.NetworkRequest;
import it.disi.unitn.lpsmt.lasagna.network.networkOps.ServerOperation;
import it.disi.unitn.lpsmt.lasagna.eventinfo.callbacks.OrganizerCallback;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class DeleteEvent extends ServerOperation {
    private final NetworkRequest request;
    private final String accessToken, eventId;

    private final Fragment f;

    public DeleteEvent(@NonNull String accessToken, @NonNull String eventId, @NonNull Fragment f) {
        request = getNetworkRequest();
        this.accessToken = accessToken;
        this.eventId = eventId;
        this.f = f;
    }

    private void setAlertDialog(@StringRes int title, @StringRes int message) {
        f.requireActivity().runOnUiThread(() -> {
            AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
            dialog.setTitle(title);
            dialog.setMessage(f.getString(message));
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
            dialog.show();
        });
    }

    public void run() {
        List<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<>("x-access-token", accessToken));
        Request req = request.getDeleteRequest(headers, getBaseUrl() + "/api/v2/annullaEvento/" + eventId);
        request.enqueue(req, new OrganizerCallback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                ((OrgEvInterface)f.requireActivity()).showRes(response.code());
            }
        });
    }
}
