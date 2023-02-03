package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network;

import static android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.util.Log;

import androidx.annotation.NonNull;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;

public class NetworkCallback extends ConnectivityManager.NetworkCallback {
    private final Activity a;
    private final NetworkRequest req;
    private final ConnectivityManager connectivityManager;

    public NetworkCallback(@NonNull Activity a) {
        this.a = a;
        req = new NetworkRequest.Builder()
                .addCapability(NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();
        connectivityManager = a.getSystemService(ConnectivityManager.class);
    }

    public void addDefaultNetworkActiveListener(@NonNull ConnectivityManager.OnNetworkActiveListener listener) {
        connectivityManager.addDefaultNetworkActiveListener(listener);
    }

    @Override
    public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);
    }

    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);
        AlertDialog alert = new AlertDialog.Builder(a).create();
        alert.setTitle(R.string.no_connection);
        alert.setMessage(a.getString(R.string.no_connection_message));
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", (dialog1, which) -> dialog1.dismiss());
        alert.show();
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        Log.e("changedCapabilities", "The default network changed capabilities: " + networkCapabilities);
    }

    @Override
    public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
        Log.e("changedLinkProperties", "The default network changed link properties: " + linkProperties);
    }

    @Override
    //@RequiresApi(api = Build.VERSION_CODES.O)
    public void onUnavailable() {
        super.onUnavailable();
        AlertDialog alert = new AlertDialog.Builder(a).create();
        alert.setTitle(R.string.no_connection);
        alert.setMessage(a.getString(R.string.no_connection_message));
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", (dialog1, which) -> dialog1.dismiss());
        alert.show();
    }

    public boolean isOnline(Context ctx) {
        if (ctx == null)
            return false;

        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void registerNetworkCallback() {
        connectivityManager.registerNetworkCallback(req, this);
    }

    public void unregisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(this);
    }
}
