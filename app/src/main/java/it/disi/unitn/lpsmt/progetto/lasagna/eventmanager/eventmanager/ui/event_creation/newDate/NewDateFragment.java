package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.newDate;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.EventViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.newDate.text_masks.Mask;

public class NewDateFragment extends DialogFragment {

    private NewDateViewModel mViewModel;

    private EventViewModel evm;

    private boolean dateOK = false, timeOK = false, seatsOK = false;

    @NonNull
    public static NewDateFragment newInstance() {
        return new NewDateFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_date, container, false);
    }

    private boolean parseBeginDate(@NonNull String t) {
        String beginDate = /*String.valueOf(t.getText())*/t;
        if (beginDate.length() == 10) {
            try {
                Pattern pattern;
                int substr = Integer.parseInt(beginDate.substring(6));
                if (substr % 400 == 0 || (substr % 100 == 0 && substr % 4 != 0)) {
                    //Anno bisestile
                    pattern = Pattern.compile("(((10|[0-2][1-9])/02)|(([23]0|[0-2][1-9])/" +
                            "(0[469]|11))|((31|[123]0|[0-2][1-9])/(0[13578]|1[02])))/[1-9]\\d{3}");
                } else {
                    pattern = Pattern.compile("(((10|09|[0-2][1-8])/02)|(([23]0|[0-2][1-9])/" +
                            "(0[469]|11))|((31|[123]0|[0-2][1-9])/(0[13578]|1[02])))/[1-9]\\d{3}");
                }
                if (pattern.matcher(beginDate).find()) {
                    SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
                    boolean over = false;
                    Date toCheck = sdformat.parse(beginDate);
                    Date d = new Date();

                    if (toCheck != null && toCheck.compareTo(d) > 0) {
                        over = true;

                        String[] dataArr = beginDate.split("/");
                        beginDate = dataArr[1] + "-" + dataArr[0] + "-" + dataArr[2];
                        mViewModel.setData(beginDate);
                    } else {
                        setAlertDialog(R.string.wrong_date, getString(R.string.date_less_than_current_date));
                    }
                    return over;
                } else {
                    setAlertDialog(R.string.incorrect_date_format_title, getString(R.string.incorrect_date_format));
                }
            } catch (NumberFormatException ex) {
                setAlertDialog(R.string.incorrect_date_format_title, getString(R.string.non_numeric_year_inserted));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            setAlertDialog(R.string.incorrect_date_format_title, getString(R.string.incorrect_date_format));
        }
        return false;
    }

    private boolean parseBeginHour(@NonNull String t1) {
        if (Pattern.compile("([0-1]\\d|2[0-3]):[0-5]\\d").matcher(/*String.valueOf(t1.getText())*/t1).matches()) {
            mViewModel.setOra(/*String.valueOf(t1.getText())*/t1);
            return true;
        }
        setAlertDialog(R.string.incorrect_hour_format_title, getString(R.string.incorrect_hour_format));
        return false;
    }

    private boolean parseSeats(@NonNull /*EditText*/ String t3) {
        if (t3/*.getText() == null || t3.getText().toString()*/.equals("")) {
            setAlertDialog(R.string.error, getString(R.string.empty_seats_field_message));
            return false;
        }

        Pattern pattern = Pattern.compile("[1-9]\\d*");
        try {
            if (pattern.matcher(/*String.valueOf(t3.getText())*/t3).find()) {
                mViewModel.setPosti(Integer.parseInt(/*String.valueOf(t3.getText())*/t3));
                return true;
            }
            setAlertDialog(R.string.incorrect_seats_format, getString(R.string.incorrect_seats_format));
        } catch (NumberFormatException ex) {
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
        evm = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        TextInputLayout seatsLayout = view.findViewById(R.id.seatsLayout);
        TextInputEditText seats = seatsLayout.findViewById(R.id.seats_value);
        if (evm.getPrivEvent()) {
            view.findViewById(R.id.seats_value).setVisibility(View.GONE);
            seatsOK = true;
        } else {
            seats.setOnFocusChangeListener((v, hasFocus) -> {
                if(!hasFocus && seats.getText() != null) {
                    seatsOK = parseSeats(seats.getText().toString());
                }
            });
        }

        TextInputLayout beginTimeLayout = view.findViewById(R.id.btInputLayout);
        TextInputEditText beginTime = beginTimeLayout.findViewById(R.id.begin_time);
        beginTime.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus && beginTime.getText() != null) {
                timeOK = parseBeginHour(beginTime.getText().toString());
            }
        });
        beginTime.addTextChangedListener(new Mask("##:##"));

        TextInputLayout beginDateInputLayout = view.findViewById(R.id.bdInputLayout);
        TextInputEditText beginDate = beginDateInputLayout.findViewById(R.id.begin_date);
        beginDate.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus && beginDate.getText() != null) {
                dateOK = parseBeginDate(beginDate.getText().toString());
            }
        });
        beginDate.addTextChangedListener(new Mask("##/##/####"));

        Button b = view.findViewById(R.id.button3);
        b.setOnClickListener(c -> {
            if (beginDate.getText() != null && beginTime.getText() != null && seats.getText() != null &&
                    parseBeginDate(beginDate.getText().toString()) &&
                    parseBeginHour(beginTime.getText().toString()) &&
                    (evm.getPrivEvent() || (!evm.getPrivEvent() &&
                    parseSeats(seats.getText().toString())))) {
                mViewModel.setOk(true);
                NavHostFragment.findNavController(this).navigate(R.id.action_newDateFragment_to_eventLocationFragment);
            }
        });
    }
}