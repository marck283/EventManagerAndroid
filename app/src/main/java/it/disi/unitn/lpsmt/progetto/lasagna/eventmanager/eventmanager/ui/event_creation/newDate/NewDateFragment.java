package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.newDate;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;

public class NewDateFragment extends DialogFragment {

    private NewDateViewModel mViewModel;

    @NonNull
    public static NewDateFragment newInstance() {
        return new NewDateFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_date, container, false);
    }

    private boolean parseBeginDate(@NonNull EditText t) {
        String beginDate = String.valueOf(t.getText());
        if(beginDate.length() == 10) {
            try {
                Pattern pattern;
                int substr = Integer.parseInt(beginDate.substring(6));
                if(substr%400 == 0 || (substr%100 == 0 && substr%4 != 0)) {
                    //Anno bisestile
                    pattern = Pattern.compile("(([0-2][1-9]/02)|((30|20|[0-2][1-9])/(04|06|09|11))|((31|30|20|[0-2][1-9])/(01|03|05|07|08|10|12)))/[1-9][0-9]{3}");
                } else {
                    pattern = Pattern.compile("(([0-2][1-8]/02)|((30|20|[0-2][1-9])/(04|06|09|11))|((31|30|20|[0-2][1-9])/(01|03|05|07|08|10|12)))/[1-9][0-9]{3}");
                }
                if(pattern.matcher(beginDate).find()) {
                    SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
                    boolean over = false;
                    Date toCheck = sdformat.parse(beginDate);
                    Date d = new Date();

                    if(toCheck != null && toCheck.compareTo(d) > 0) {
                        over = true;
                        mViewModel.setData(beginDate);
                    } else {
                        AlertDialog ad = new AlertDialog.Builder(requireContext()).create();
                        ad.setTitle(R.string.wrong_date);
                        ad.setMessage(getString(R.string.date_less_than_current_date));
                        ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                        ad.show();
                    }
                    return over;
                } else {
                    setAlertDialog(R.string.incorrect_date_format_title, getString(R.string.incorrect_date_format));
                }
            } catch(NumberFormatException ex) {
                setAlertDialog(R.string.incorrect_date_format_title, getString(R.string.incorrect_date_format));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            setAlertDialog(R.string.incorrect_date_format_title, getString(R.string.incorrect_date_format));
        }
        return false;
    }

    private boolean parseBeginHour(@NonNull EditText t1) {
        if(Pattern.compile("([0-1][0-9]|2[0-3]):[0-5][0-9]").matcher(String.valueOf(t1.getText())).matches()) {
            mViewModel.setOra(String.valueOf(t1.getText()));
            return true;
        }
        setAlertDialog(R.string.incorrect_hour_format_title, getString(R.string.incorrect_hour_format));
        return false;
    }

    private boolean parseSeats(@NonNull EditText t3) {
        Pattern pattern = Pattern.compile("([1-9][0-9])+");
        try {
            if (pattern.matcher(String.valueOf(t3.getText())).find()) {
                mViewModel.setPosti(Integer.parseInt(String.valueOf(t3.getText())));
                return true;
            }
            setAlertDialog(R.string.incorrect_seats_format, getString(R.string.incorrect_seats_format));
        } catch(NumberFormatException ex) {
            setAlertDialog(R.string.incorrect_seats_format, getString(R.string.incorrect_seats_format));
        }
        return false;
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
            EditText t3 = view.findViewById(R.id.seats_value);
            if(t.getText() != null && t1.getText() != null && t3.getText() != null) {
                if(parseBeginDate(t) && parseBeginHour(t1) && parseSeats(t3)) {
                    mViewModel.setOk(true);
                    NavHostFragment.findNavController(this).navigate(R.id.action_newDateFragment_to_eventLocationFragment);
                }
            } else {
                setAlertDialog(R.string.no_value_title, getString(R.string.no_value));
            }
        });
    }
}