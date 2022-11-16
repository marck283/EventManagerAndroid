package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.newDate;

import android.app.AlertDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.LuogoEv;

public class EventLocationViewModel extends ViewModel {
    private final DialogFragment f;
    private final NewDateViewModel nd;

    public EventLocationViewModel(DialogFragment f, NewDateViewModel nd) {
        this.f = f;
        this.nd = nd;
    }

    private void setAlertDialog(int title, String message) {
        AlertDialog ad = new AlertDialog.Builder(f.requireContext()).create();
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
        ad.show();
    }

    private void setAddress(@NonNull List<Address> addresses, LuogoEv luogo) {
        int i = 0;
        for(Address a: addresses) {
            Log.i("addresses", addresses.toString());
            if(a.getAddressLine(i).contains(luogo.toString())) {
                nd.setLuogo(luogo.toString());
                break;
            } else {
                ++i;
                setAlertDialog(R.string.incorrect_location_format_title, f.getString(R.string.incorrect_location_format));
            }
        }
    }

    public void parseAddress(@NonNull EditText t2, @NonNull EditText t3, @NonNull EditText t4, @NonNull EditText t5, @NonNull EditText t6) {
        String location = t2.getText() + ", " + t3.getText() + ", " + t4.getText() + ", " + t5.getText() + ", " + t6.getText();
        Geocoder geocoder = new Geocoder(f.getContext());
        Pattern pattern = Pattern.compile("[a-zA-Z]+, [1-9]+[a-z]*, [1-9]{5}, [a-zA-Z]+, [A-Z]{2}");
        if(pattern.matcher(location).find()) {
            try {
                String[] split = location.split(", ");
                LuogoEv luogo = new LuogoEv(split[0], split[3], split[1], split[4], Integer.parseInt(split[2]));

                //Nessun risultato senza controllare anche la nazione...
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(luogo.getLatitude(), luogo.getLongitude(), 5, addresses -> setAddress(addresses, luogo));
                } else {
                    Thread t1 = new Thread() {
                        @Override
                        public void run() {
                            List<Address> addresses;
                            try {
                                addresses = geocoder.getFromLocationName(String.valueOf(t2.getText()), 5);
                                Looper.prepare();
                                setAddress(addresses, luogo);
                                Looper.loop();
                                Looper.getMainLooper().quitSafely();
                                f.dismiss();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    t1.start();
                }
            } catch(NumberFormatException ex) {
                setAlertDialog(R.string.incorrect_location_format_title, f.getString(R.string.incorrect_location_format));
            }
        } else {
            setAlertDialog(R.string.incorrect_location_format_title, f.getString(R.string.incorrect_location_format));
        }
    }
}