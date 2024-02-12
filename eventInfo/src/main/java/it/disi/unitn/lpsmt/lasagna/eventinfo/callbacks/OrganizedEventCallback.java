package it.disi.unitn.lpsmt.lasagna.eventinfo.callbacks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import it.disi.unitn.lasagna.eventcreation.helpers.LuogoEv;
import it.disi.unitn.lasagna.eventmanager.geocoder.GeocoderExt;
import it.disi.unitn.lasagna.eventmanager.ui_extra.spinnerImplementation.SpinnerArrayAdapter;
import it.disi.unitn.lpsmt.lasagna.eventinfo.organizedEvent.OrganizedEvent;
import okhttp3.Call;
import okhttp3.Response;

public class OrganizedEventCallback extends OrganizerCallback {

    private final View v;

    private final Fragment f;

    private final ActivityResultLauncher<Intent> loginLauncher;

    private String day;

    private final Class<? extends Activity> c;

    private final int iv3, tv6, info_on_event, spinner2, orgDateTextView, tv15, spinner, orgHourTextView,
    list_item, event_address, bt8, bt12, tv12, duration, user_not_logged_in, user_not_logged_in_message,
    no_org_event, no_org_event_message;

    public OrganizedEventCallback(@NotNull View v, @NotNull Fragment f, @NotNull ActivityResultLauncher<Intent> loginLauncher,
                                  String day, @NotNull Class<? extends Activity> c, @IdRes int iv3,
                                  @IdRes int tv6, @StringRes int info_on_event, @IdRes int spinner2,
                                  @IdRes int orgDateTextView, @IdRes int tv15, @IdRes int spinner,
                                  @IdRes int orgHourTextView, @LayoutRes int list_item, @StringRes int event_address,
                                  @IdRes int bt8, @IdRes int bt12, @IdRes int tv12, @StringRes int duration,
                                  @StringRes int user_not_logged_in, @StringRes int user_not_logged_in_message,
                                  @StringRes int no_org_event, @StringRes int no_org_event_message) {
        this.v = v;
        this.f = f;
        this.loginLauncher = loginLauncher;
        this.day = day;
        this.c = c;
        this.iv3 = iv3;
        this.tv6 = tv6;
        this.info_on_event = info_on_event;
        this.spinner2 = spinner2;
        this.orgDateTextView = orgDateTextView;
        this.tv15 = tv15;
        this.spinner = spinner;
        this.orgHourTextView = orgHourTextView;
        this.list_item = list_item;
        this.event_address = event_address;
        this.bt8 = bt8;
        this.bt12 = bt12;
        this.tv12 = tv12;
        this.duration = duration;
        this.user_not_logged_in = user_not_logged_in;
        this.user_not_logged_in_message = user_not_logged_in_message;
        this.no_org_event = no_org_event;
        this.no_org_event_message = no_org_event_message;
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        Gson gson = new Gson();
        if (response.isSuccessful()) {
            OrganizedEvent event = OrganizedEvent.parseJSON(gson.fromJson(response.body().string(), JsonObject.class));
            Log.i("OK", "OK");

            Activity activity = f.getActivity();
            if (activity != null && f.isAdded()) {
                f.requireActivity().runOnUiThread(() -> {
                    ImageView iView = v.findViewById(iv3);
                    Bitmap bm = event.decodeBase64();
                    if (bm != null) {
                        Glide.with(v).load(bm).into(iView);
                    }

                    TextView evName = v.findViewById(tv6);
                    evName.setText(f.getString(info_on_event, event.getEventName()));

                    TextInputLayout evDay = v.findViewById(spinner2);
                    MaterialAutoCompleteTextView dayText = evDay.findViewById(orgDateTextView);

                    ArrayList<CharSequence> dayArr = new ArrayList<>();
                    dayArr.add("---");
                    for (LuogoEv l : event.getLuogoEv()) {
                        String[] dateArr = l.getData().split("-");
                        dayArr.add(dateArr[1] + "/" + dateArr[0] + "/" + dateArr[2]);
                    }

                    dayText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            //Nulla da scrivere qui...
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (evDay.getEditText() != null &&
                                    !evDay.getEditText().getText().toString().equals("---")) {
                                TextView address = v.findViewById(tv15);
                                TextInputLayout spinner1 = v.findViewById(spinner);
                                MaterialAutoCompleteTextView hourTextView = spinner1.findViewById(orgHourTextView);

                                ArrayList<CharSequence> hourArr = new ArrayList<>();
                                hourArr.add("---");

                                String[] dayArr = evDay.getEditText().getText().toString().split("/");
                                day = dayArr[1] + "-" + dayArr[0] + "-" + dayArr[2];
                                for (LuogoEv l : event.getOrari(day)) {
                                    hourArr.add(l.getOra());
                                }

                                hourTextView.setAdapter(new SpinnerArrayAdapter(f.requireContext(),
                                        list_item, hourArr));

                                hourTextView.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                        //Nulla da scrivere qui...
                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        //L'ultima condizione di questo blocco if non ci dovrebbe essere
                                        if(hourTextView.getText() != null && !hourTextView.getText().toString().equals("") &&
                                                !hourTextView.getText().toString().equals("---")) {
                                            if(hourTextView.getText().toString().equals("---")) {
                                                address.setText(f.getString(event_address, ""));
                                            } else {
                                                Button qrCodeScan = v.findViewById(bt8),
                                                        terminaEvento = v.findViewById(bt12);
                                                address.setText(f.getString(event_address,
                                                        event.getLuogo(day, hourTextView.getText().toString()).getAddress()));
                                                if(!address.hasOnClickListeners()) {
                                                    address.setOnClickListener(c -> {
                                                        GeocoderExt geocoder = new GeocoderExt(f, address);
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                                            geocoder.fromLocationName(address.getText().toString(), 5);
                                                        } else {
                                                            geocoder.fromLocationNameThread(address.getText().toString(), 5);
                                                        }
                                                    });
                                                }
                                                address.setPaintFlags(address.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                                                String[] day1 = dayText.getText().toString().split("/");
                                                if (day1.length > 1) {
                                                    String day2 = day1[1] + "-" + day1[0] + "-" + day1[2];
                                                    if (event.getEventType().equals("priv") || (
                                                            !dayText.getText().toString().equals("---") &&
                                                                    !hourTextView.getText().toString().equals("---") &&
                                                                    event.getLuogo(day2,
                                                                            hourTextView.getText().toString()).getTerminato())) {
                                                        qrCodeScan.setEnabled(false);
                                                        terminaEvento.setEnabled(false);
                                                    } else {
                                                        qrCodeScan.setEnabled(true);
                                                        terminaEvento.setEnabled(true);
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        //Nulla da scrivere qui...
                                    }
                                });
                            } else {
                                TextInputLayout hour = v.findViewById(spinner);
                                MaterialAutoCompleteTextView hourTextView = hour.findViewById(orgHourTextView);
                                hourTextView.setAdapter(new SpinnerArrayAdapter(f.requireContext(),
                                        list_item, new ArrayList<>()));
                                hourTextView.setText("");

                                TextView address = v.findViewById(tv15);
                                address.setText(f.getString(event_address, ""));
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            //Nulla da scrivere qui...
                        }
                    });
                    dayText.setAdapter(new SpinnerArrayAdapter(f.requireContext(), list_item, dayArr));

                    TextView duration1 = v.findViewById(tv12);
                    String eventDurata = event.getDurata();
                    if(event.getDurata() == null || event.getDurata().equals("")) {
                        duration1.setText(f.getString(duration,
                                "0", "0", "0"));
                    } else {
                        String[] durata = eventDurata.split(":");
                        duration1.setText(f.getString(duration, durata[0], durata[1], durata[2]));
                    }
                });
            }
            response.body().close();
        } else {
            switch (response.code()) {
                case 401 -> {
                    if (loginLauncher != null) {
                        Intent loginIntent = new Intent(f.requireActivity(), c);
                        loginLauncher.launch(loginIntent);
                    } else {
                        f.requireActivity().runOnUiThread(() -> {
                            AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
                            dialog.setTitle(user_not_logged_in);
                            dialog.setMessage(f.getString(user_not_logged_in_message));
                            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                            dialog.show();
                        });
                    }
                }
                case 404 -> f.requireActivity().runOnUiThread(() -> {
                    AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
                    dialog.setTitle(no_org_event);
                    dialog.setMessage(f.getString(no_org_event_message));
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                    dialog.show();
                });
            }
        }
    }

}
