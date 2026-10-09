package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_creation.newDate;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import it.disi.unitn.lasagna.eventcreation.viewmodel.EventViewModel;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lasagna.eventmanager.ui_extra.special_buttons.ListenerButton;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

public class NewDateFragment extends DialogFragment {

    private NewDateViewModel mViewModel;

    private EventViewModel evm;

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
        if (t.isEmpty()) {
            return false;
        }
        try {
            SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);

            Date toCheck = sdformat.parse(t);
            Date d = sdformat.parse(sdformat.format(new Date()));

            if (toCheck != null && toCheck.compareTo(d) >= 0) {
                String[] dataArr = t.split("/");
                t = dataArr[1] + "-" + dataArr[0] + "-" + dataArr[2];
                mViewModel.setData(t);
                return true;
            } else {
                setAlertDialog(R.string.wrong_date, getString(R.string.date_less_than_current_date));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean parseBeginHour(@NonNull String t1, @NonNull String date) {
        if (t1.isEmpty() || date.isEmpty()) {
            return false;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ITALIAN);
            Date eventDateTime = sdf.parse(date + " " + t1);

            if (eventDateTime != null && eventDateTime.after(new Date())) {
                mViewModel.setOra(t1);
                return true;
            } else {
                setAlertDialog(R.string.incorrect_hour_value, getString(R.string.incorrect_hour_value_message));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean parseSeats(@NonNull String t3) {
        if (t3.isEmpty()) {
            setAlertDialog(R.string.error, getString(R.string.empty_seats_field_message));
            return false;
        }

        try {
            int seats = Integer.parseInt(t3);
            if (seats > 0) {
                mViewModel.setPosti(seats);
                return true;
            }
        } catch (NumberFormatException ignored) {
            // Purposefully left blank here...
        }

        setAlertDialog(R.string.incorrect_seats_format, getString(R.string.incorrect_seats_format));
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
                parseBeginDate(beginDate.getText().toString());
            }
        });

        View.OnClickListener showDatePicker = v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText(R.string.insert_date)
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
                String formattedDate = sdf.format(new Date(selection));
                beginDate.setText(formattedDate);
                parseBeginDate(formattedDate);
            });

            datePicker.show(getChildFragmentManager(), "DATE_PICKER");
        };
        beginDate.setOnClickListener(showDatePicker);
        beginDateInputLayout.setEndIconOnClickListener(showDatePicker);

        TextInputLayout beginTimeLayout = view.findViewById(R.id.btInputLayout);
        TextInputEditText beginTime = beginTimeLayout.findViewById(R.id.begin_time);

        View.OnClickListener showTimePicker = v -> {
            Calendar calendar = Calendar.getInstance();
            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                    .setMinute(calendar.get(Calendar.MINUTE))
                    .setTitleText(R.string.insert_hour)
                    .build();

            timePicker.addOnPositiveButtonClickListener(v1 -> {
                String formattedTime = String.format(Locale.ITALIAN, "%02d:%02d", timePicker.getHour(), timePicker.getMinute());
                beginTime.setText(formattedTime);
                if (beginDate.getText() != null) {
                    parseBeginHour(formattedTime, beginDate.getText().toString());
                }
            });

            timePicker.show(getChildFragmentManager(), "TIME_PICKER");
        };
        beginTime.setOnClickListener(showTimePicker);
        beginTimeLayout.setEndIconOnClickListener(showTimePicker);

        ListenerButton b = view.findViewById(R.id.button3);
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