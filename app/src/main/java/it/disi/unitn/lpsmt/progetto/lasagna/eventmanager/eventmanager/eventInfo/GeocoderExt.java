package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo;

import android.app.AlertDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.widget.TextView;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.EventDetailsFragment;

public class GeocoderExt {
    private Geocoder geocoder;

    private final Fragment f;

    private final TextView address;

    public GeocoderExt(@NonNull Fragment f, @NonNull TextView address) {
        this.f = f;
        geocoder = new Geocoder(f.requireActivity());
        this.address = address;
    }

    private void startGoogleMaps(@NonNull EventDetailsFragment f, @NonNull TextView indirizzo,
                                 @NonNull List<Address> addresses) {
        Address address = addresses.get(0);

        Uri gmURI = Uri.parse("geo:" + address.getLatitude() + "," + address.getLongitude()
                + "?q=" + indirizzo.getText().toString());
        Intent i = new Intent(Intent.ACTION_VIEW, gmURI);
        i.setPackage("com.google.android.apps.maps");

        f.requireActivity().startActivity(i);
    }

    private void noSuchAddressDialog(@NonNull Fragment f) {
        f.requireActivity().runOnUiThread(() -> {
            AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
            dialog.setTitle(R.string.no_such_address);
            dialog.setMessage(f.getString(R.string.address_not_registered));
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
            dialog.show();
        });
    }

    @RequiresApi(33)
    public void fromLocationName(String locationName, @IntRange int maxResults) {
        geocoder.getFromLocationName(locationName, maxResults, addresses -> {
            if (addresses.size() > 0) {
                startGoogleMaps((EventDetailsFragment) f, address, addresses);
            } else {
                noSuchAddressDialog(f);
            }
        });
    }

    public void fromLocationNameThread(String locationName, @IntRange int maxResults) {
        Thread t1 = new Thread() {
            public void run() {
                List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocationName(locationName, maxResults);
                    if (addresses != null && addresses.size() > 0) {
                        startGoogleMaps((EventDetailsFragment) f, address, addresses);
                    } else {
                        noSuchAddressDialog(f);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t1.start();
    }
}
