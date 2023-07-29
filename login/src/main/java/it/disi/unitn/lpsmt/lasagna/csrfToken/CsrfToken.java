package it.disi.unitn.lpsmt.lasagna.csrfToken;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.AccessToken;

import java.util.ArrayList;

import it.disi.unitn.lpsmt.lasagna.network.NetworkRequest;
import it.disi.unitn.lpsmt.lasagna.network.networkOps.ServerOperation;
import okhttp3.Request;

public class CsrfToken extends ServerOperation {

    private final Activity a;

    private final String gJwt, which;

    private final AccessToken fbJwt;

    private final Intent i;

    public CsrfToken(@NonNull Activity a, @Nullable String gJwt,
                     @Nullable AccessToken fbJwt, @NonNull String which, @Nullable Intent i) {
        this.a = a;
        this.gJwt = gJwt;
        this.fbJwt = fbJwt;
        this.which = which;
        this.i = i;
    }

    //Come associo il token CSRF alla classe di autenticazione senza dimenticare che potrebbe servirmi anche per altre classi in futuro?
    public void run() {
        NetworkRequest req = new NetworkRequest();
        //Authentication o = new Authentication();
        Request request = req.getRequest(new ArrayList<>(), getBaseUrl() + "/api/v2/csrfToken");
        req.enqueue(request, new CsrfTokenCallback(/*o, */a, i, which, fbJwt, gJwt));
    }
}
