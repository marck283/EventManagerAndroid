package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network;

import android.app.Activity;
import android.app.AlertDialog;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;

public class NetworkCallback extends ConnectivityManager.NetworkCallback {
    private final Activity a;

    public NetworkCallback(@NonNull Activity a) {
        this.a = a;
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onUnavailable() {
        super.onUnavailable();
        AlertDialog alert = new AlertDialog.Builder(a).create();
        alert.setTitle(R.string.no_connection);
        alert.setMessage(a.getString(R.string.no_connection_message));
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", (dialog1, which) -> dialog1.dismiss());
        alert.show();
    }
}
