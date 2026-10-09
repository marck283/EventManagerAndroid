package it.disi.unitn.lpsmt.lasagna.eventinfo.registeredEvent.ticket;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import it.disi.unitn.lpsmt.lasagna.eventinfo.R;

public class TicketInfoCallback {

    private final Fragment f;

    private final View v;

    public TicketInfoCallback(@NotNull Fragment f, @NotNull View v) {
        this.f = f;
        this.v = v;
    }

    public void setAlertDialog(@StringRes int title, @StringRes int message) {
        Activity activity = f.getActivity();
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed() && f.isAdded()) {
            activity.runOnUiThread(() -> {
                if (!activity.isFinishing() && !activity.isDestroyed()) {
                    AlertDialog dialog = new AlertDialog.Builder(activity).create();
                    dialog.setTitle(title);
                    dialog.setMessage(f.getString(message));
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                    dialog.show();
                }
            });
        }
    }

    public void handleTicketSuccess(JsonObject ticketJson, int qrCodeViewId) {
        Ticket ticket = Ticket.parseJSON(ticketJson);

        Activity activity = f.getActivity();
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed() && f.isAdded()) {
            activity.runOnUiThread(() -> {
                try {
                    ImageView imageViewQrCode = v.findViewById(qrCodeViewId);
                    Glide.with(v).load(ticket.getQR()).into(imageViewQrCode);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void handleTicketError(int statusCode) {
        switch (statusCode) {
            case 400 -> setAlertDialog(R.string.malformed_request_or_invalid_date, R.string.malformed_request_or_invalid_date_message);
            case 401 -> setAlertDialog(R.string.user_not_logged_in, R.string.user_not_logged_in_message);
            case 404 -> setAlertDialog(R.string.no_ticket, R.string.no_ticket_message);
        }
    }
}
