package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.newDate;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.LuogoEv;

public class NewDateFragment extends DialogFragment {

    private NewDateViewModel mViewModel;

    @NonNull
    @Contract(" -> new")
    public static NewDateFragment newInstance() {
        return new NewDateFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_date, container, false);
    }

    private void parseBeginDate(@NonNull EditText t) {
        String beginDate = String.valueOf(t.getText());
        if(beginDate.length() == 10) {
            Pattern pattern;
            int substr = Integer.parseInt(beginDate.substring(6));
            if(substr%400 == 0 || (substr%100 == 0 && substr%4 != 0)) {
                //Anno bisestile
                pattern = Pattern.compile("(([1-29]-2)|([1-30]-(4|6|9|11))|([1-31]-(1|3|5|7|8|10|12)))-[1-9][0-9]{3}"); //Non compila
            } else {
                pattern = Pattern.compile("(([1-28]-2)|([1-30]-(4|6|9|11))|([1-31]-(1|3|5|7|8|10|12)))-[1-9][0-9]{3}"); //Non compila
            }
            if(pattern.matcher(beginDate).matches()) {
                mViewModel.setData(beginDate);
            } else {
                setAlertDialog(R.string.incorrect_date_format_title, getString(R.string.incorrect_date_format));
            }
        } else {
            setAlertDialog(R.string.incorrect_date_format_title, getString(R.string.incorrect_date_format));
        }
    }

    private void parseBeginHour(@NonNull EditText t1) {
        if(Pattern.compile("([0-1][0-9]|2[0-3]):[0-5][0-9]").matcher(String.valueOf(t1.getText())).matches()) {
            mViewModel.setOra(String.valueOf(t1.getText()));
        } else {
            setAlertDialog(R.string.incorrect_hour_format_title, getString(R.string.incorrect_hour_format));
        }
    }

    private void setAddress(@NonNull List<Address> addresses, LuogoEv luogo) {
        if(addresses.contains(luogo)) {
            mViewModel.setLuogo(luogo.toString());
        } else {
            setAlertDialog(R.string.incorrect_location_format_title, getString(R.string.incorrect_location_format));
        }
    }

    private void parseLocation(@NonNull EditText t2) {
        String location = String.valueOf(t2.getText());
        Geocoder geocoder = new Geocoder(getContext());
        Pattern pattern = Pattern.compile("[a-zA-Z]+, [1-9]+[a-z]*, [1-9]{5}, [a-zA-Z]+, [A-Z]{2}");
        if(pattern.matcher(location).matches()) {
            try {
                String[] split = location.split(", ");
                LuogoEv luogo = new LuogoEv(split[0], split[3], split[1], split[4], Integer.parseInt(split[2]));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(luogo.getLatitude(), luogo.getLongitude(), 5, addresses -> setAddress(addresses, luogo));
                } else {
                    Thread t1 = new Thread() {
                        @Override
                        public void run() {
                            List<Address> addresses;
                            try {
                                addresses = geocoder.getFromLocation(luogo.getLatitude(), luogo.getLongitude(), 5);
                                setAddress(addresses, luogo);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    t1.start();
                }
            } catch(NumberFormatException ex) {
                setAlertDialog(R.string.incorrect_location_format_title, getString(R.string.incorrect_location_format));
            }
        } else {
            setAlertDialog(R.string.incorrect_location_format_title, getString(R.string.incorrect_location_format));
        }
    }

    private void parseSeats(@NonNull EditText t3) {
        try {
            mViewModel.setPosti(Integer.parseInt(String.valueOf(t3.getText())));
        } catch(NumberFormatException ex) {
            setAlertDialog(R.string.incorrect_seats_format, getString(R.string.incorrect_seats_format));
        }
    }

    private void setAlertDialog(int title, String message) {
        AlertDialog ad = new AlertDialog.Builder(requireContext()).create();
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
        ad.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(NewDateViewModel.class);
        Button b = view.findViewById(R.id.button3);
        b.setOnClickListener(c -> {
            EditText t = view.findViewById(R.id.begin_date);
            EditText t1 = view.findViewById(R.id.begin_time);
            EditText t2 = view.findViewById(R.id.venue_value);
            EditText t3 = view.findViewById(R.id.seats_value);
            if(t.getText() != null && t1.getText() != null && t2.getText() != null && t3.getText() != null) {
                //Check fields for correct formats
                parseBeginDate(t);
                parseBeginHour(t1);
                parseLocation(t2);
                parseSeats(t3);
            } else {
                setAlertDialog(R.string.no_value, getString(R.string.no_value_title));
            }
        });
    }
}