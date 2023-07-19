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

import org.jetbrains.annotations.Contract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.EventViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.matchers.Matcher;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.newDate.text_masks.Mask;

public class NewDateFragment extends DialogFragment {

    private NewDateViewModel mViewModel;

    private EventViewModel evm;

    private boolean dateOK = false;

    @NonNull
    public static NewDateFragment newInstance() {
        return new NewDateFragment();
    }

    @NonNull
    @Contract(pure = true)
    private String padStart(@NonNull String s) {
        if(s.length() < 2) {
            return "0" + s;
        }
        return s;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_date, container, false);
    }

    private boolean parseBeginDate(@NonNull String t) {
        if (t.length() == 10) {
            try {
                Matcher matcher;
                int substr = Integer.parseInt(t.substring(6));
                if (substr % 400 == 0 || (substr % 100 == 0 && substr % 4 != 0)) {
                    //Anno bisestile
                    matcher = new Matcher("(((10|[0-2][1-9])/02)|(([23]0|[0-2][1-9])/" +
                            "(0[469]|11))|((31|[123]0|[0-2][1-9])/(0[13578]|1[02])))/[1-9]\\d{3}", t);
                } else {
                    matcher = new Matcher("((([12]0|[01]9|[0-2][1-8])/02)|(([23]0|[0-2][1-9])/" +
                            "(0[469]|11))|((31|[123]0|[0-2][1-9])/(0[13578]|1[02])))/[1-9]\\d{3}", t);
                }
                if (matcher.isValid()) {
                    SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);

                    //Soluzione al problema dell'inserimento della data corrente
                    Calendar calendar = new GregorianCalendar(Locale.ITALIAN);
                    String today = padStart(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))) + "/"
                            + padStart(String.valueOf(calendar.get(Calendar.MONTH) + 1))
                            + "/" + calendar.get(Calendar.YEAR);

                    boolean over = false;
                    Date toCheck = sdformat.parse(t);
                    Date d = sdformat.parse(today);

                    if (toCheck != null && toCheck.compareTo(d) >= 0) {
                        over = true;

                        String[] dataArr = t.split("/");
                        t = dataArr[1] + "-" + dataArr[0] + "-" + dataArr[2];
                        mViewModel.setData(t);
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

    private boolean parseBeginHour(@NonNull String t1, @NonNull String date) {
        Matcher matcher = new Matcher("([0-1]\\d|2[0-3]):[0-5]\\d", t1);
        if (matcher.matches()) {
            //Soluzione al problema dell'orario passato per la giornata corrente
            if(dateOK) {
                Date now = new Date();
                String[] dateArr = date.split("/"), hourArr = t1.split(":");
                Calendar calendar = new GregorianCalendar(Integer.parseInt(dateArr[2]),
                        Integer.parseInt(dateArr[1]), Integer.parseInt(dateArr[0]), Integer.parseInt(hourArr[0]),
                        Integer.parseInt(hourArr[1]));
                if(now.before(calendar.getTime())) {
                    mViewModel.setOra(t1);
                    return true;
                } else {
                    setAlertDialog(R.string.incorrect_hour_value, getString(R.string.incorrect_hour_value_message));
                }
            }
            return false;
        }
        setAlertDialog(R.string.incorrect_hour_format_title, getString(R.string.incorrect_hour_format));
        return false;
    }

    private boolean parseSeats(@NonNull String t3) {
        if (t3 == null || t3.equals("")) {
            setAlertDialog(R.string.error, getString(R.string.empty_seats_field_message));
            return false;
        }

        Matcher matcher = new Matcher("[1-9]\\d*", t3);
        try {
            if (matcher.isValid()) {
                mViewModel.setPosti(Integer.parseInt(t3));
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
        } else {
            seats.setOnFocusChangeListener((v, hasFocus) -> {
                if(!hasFocus && seats.getText() != null) {
                    parseSeats(seats.getText().toString());
                }
            });
        }

        TextInputLayout beginDateInputLayout = view.findViewById(R.id.bdInputLayout);
        TextInputEditText beginDate = beginDateInputLayout.findViewById(R.id.begin_date);
        beginDate.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus && beginDate.getText() != null) {
                dateOK = parseBeginDate(beginDate.getText().toString());
            }
        });
        beginDate.addTextChangedListener(new Mask("##/##/####"));

        TextInputLayout beginTimeLayout = view.findViewById(R.id.btInputLayout);
        TextInputEditText beginTime = beginTimeLayout.findViewById(R.id.begin_time);
        beginTime.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus && beginTime.getText() != null && beginDate.getText() != null) {
                parseBeginHour(beginTime.getText().toString(), beginDate.getText().toString());
            }
        });
        beginTime.addTextChangedListener(new Mask("##:##"));

        Button b = view.findViewById(R.id.button3);
        b.setOnClickListener(c -> {
            if (beginDate.getText() != null && beginTime.getText() != null && seats.getText() != null &&
                    parseBeginDate(beginDate.getText().toString()) &&
                    parseBeginHour(beginTime.getText().toString(), beginDate.getText().toString()) &&
                    (evm.getPrivEvent() || (!evm.getPrivEvent() &&
                    parseSeats(seats.getText().toString())))) {
                mViewModel.setOk(true);
                NavHostFragment.findNavController(this).navigate(R.id.action_newDateFragment_to_eventLocationFragment);
            }
        });
    }
}