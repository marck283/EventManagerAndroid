package it.disi.unitn.lpsmt.lasagna.checkqrcode;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.lasagna.network.NetworkRequest;
import it.disi.unitn.lpsmt.lasagna.network.networkOps.ServerOperation;
import okhttp3.Request;

public class CheckQRCode extends ServerOperation {

    private final NetworkRequest request;
    private final String userJwt, qrCode, eventid, day, hour;

    private final Fragment f;

    private final int validQRCT, validQRCMsg, invalid_qr_code, invalid_qr_code_message,
            malformed_request, malformed_request_message, no_session_title, no_session_message;

    public CheckQRCode(@NonNull String userJwt, @NonNull String qrCode, @NonNull String eventid,
                       @NonNull String day, @NonNull String hour, @NonNull Fragment f,
                       @StringRes int validQRCT, @StringRes int validQRCMsg,
                       @StringRes int invalid_qr_code,
                       @StringRes int invalid_qr_code_message, @StringRes int malformed_request,
                       @StringRes int malformed_request_message, @StringRes int no_session_title,
                       @StringRes int no_session_message) {
        this.userJwt = userJwt;
        this.qrCode = qrCode;
        request = getNetworkRequest();
        this.f = f;
        this.eventid = eventid;
        this.day = day;
        this.hour = hour;
        this.validQRCT = validQRCT;
        this.validQRCMsg = validQRCMsg;
        this.invalid_qr_code = invalid_qr_code;
        this.invalid_qr_code_message = invalid_qr_code_message;
        this.malformed_request = malformed_request;
        this.malformed_request_message = malformed_request_message;
        this.no_session_title = no_session_title;
        this.no_session_message = no_session_message;
    }

    public void run() {
        List<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<>("x-access-token", userJwt));
        headers.add(new Pair<>("eventoid", eventid));
        headers.add(new Pair<>("day", day));
        headers.add(new Pair<>("hour", hour));
        Request req = request.getRequest(headers,
                getBaseUrl() + "/api/v2/QRCodeCheck/" + qrCode);
        Log.i("code", qrCode);
        try {
            request.enqueue(req, new QRCodeCallback(f, validQRCT, validQRCMsg, invalid_qr_code,
                    invalid_qr_code_message, malformed_request, malformed_request_message,
                    no_session_title, no_session_message));
        } catch (InvalidObjectException e) {
            e.printStackTrace();
        }
    }
}
