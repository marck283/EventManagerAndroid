package it.disi.unitn.lpsmt.lasagna.eventinfo.organizedEvent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lasagna.eventcreation.helpers.LuogoEv;
import it.disi.unitn.lpsmt.lasagna.network.NetworkRequest;
import it.disi.unitn.lpsmt.lasagna.network.networkOps.ServerOperation;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.GeocoderExt;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.EventDetailsFragment;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.callbacks.OrganizerCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.spinnerImplementation.SpinnerArrayAdapter;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class OrganizedEventInfo extends ServerOperation {

    private final NetworkRequest request;

    private final String userJwt, eventId;

    private final View v;

    private final EventDetailsFragment f;

    private String day;

    private final ActivityResultLauncher<Intent> loginLauncher;

    public OrganizedEventInfo(@NonNull View v, @NonNull EventDetailsFragment f, @NonNull String userJwt,
                              @NonNull String evId, @NonNull String day) {
        request = getNetworkRequest();
        this.userJwt = userJwt;
        eventId = evId;
        this.v = v;
        this.f = f;
        this.day = day;
        this.loginLauncher = null;
    }

    public OrganizedEventInfo(@NonNull View v, @NonNull EventDetailsFragment f, @NonNull String userJwt,
                              @NonNull String evId,
                              @NonNull ActivityResultLauncher<Intent> loginLauncher) {
        request = getNetworkRequest();
        this.userJwt = userJwt;
        eventId = evId;
        this.v = v;
        this.f = f;
        this.day = null;
        this.loginLauncher = loginLauncher;
    }

    public OrganizedEventInfo(@NonNull View v, @NonNull EventDetailsFragment f, @NonNull String userJwt,
                              @NonNull String evId) {
        request = getNetworkRequest();
        this.userJwt = userJwt;
        eventId = evId;
        this.v = v;
        this.f = f;
        this.day = null;
        this.loginLauncher = null;
    }

    public void run() {
        Request req;
        List<Pair<String, String>> headers = new ArrayList<>();
        headers.add(new Pair<>("x-access-token", userJwt));
        if (day != null) {
            headers.add(new Pair<>("date", day));
        }
        req = request.getRequest(headers, getBaseUrl() + "/api/v2/InfoEventoOrg/" + eventId);
        request.enqueue(req, new OrganizerCallback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Gson gson = new Gson();
                if (response.isSuccessful()) {
                    OrganizedEvent event = OrganizedEvent.parseJSON(gson.fromJson(response.body().string(), JsonObject.class));
                    Log.i("OK", "OK");

                    Activity activity = f.getActivity();
                    if (activity != null && f.isAdded()) {
                        f.requireActivity().runOnUiThread(() -> {
                            ImageView iView = v.findViewById(R.id.imageView3);
                            Bitmap bm = event.decodeBase64();
                            if (bm != null) {
                                Glide.with(v).load(bm).into(iView);
                            }

                            TextView evName = v.findViewById(R.id.textView6);
                            evName.setText(f.getString(R.string.info_on_event, event.getEventName()));

                            TextInputLayout evDay = v.findViewById(R.id.spinner2);
                            MaterialAutoCompleteTextView dayText = evDay.findViewById(R.id.orgDateTextView);

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
                                        TextView address = v.findViewById(R.id.textView15);
                                        TextInputLayout spinner = v.findViewById(R.id.spinner);
                                        MaterialAutoCompleteTextView hourTextView = spinner.findViewById(R.id.orgHourTextView);

                                        ArrayList<CharSequence> hourArr = new ArrayList<>();
                                        hourArr.add("---");

                                        String[] dayArr = evDay.getEditText().getText().toString().split("/");
                                        day = dayArr[1] + "-" + dayArr[0] + "-" + dayArr[2];
                                        for (LuogoEv l : event.getOrari(day)) {
                                            hourArr.add(l.getOra());
                                        }

                                        hourTextView.setAdapter(new SpinnerArrayAdapter(f.requireContext(),
                                                R.layout.list_item, hourArr));

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
                                                        address.setText(f.getString(R.string.event_address, ""));
                                                    } else {
                                                        Button qrCodeScan = v.findViewById(R.id.button8),
                                                                terminaEvento = v.findViewById(R.id.button12);
                                                        address.setText(f.getString(R.string.event_address,
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
                                        TextInputLayout hour = v.findViewById(R.id.spinner);
                                        MaterialAutoCompleteTextView hourTextView = hour.findViewById(R.id.orgHourTextView);
                                        hourTextView.setAdapter(new SpinnerArrayAdapter(f.requireContext(),
                                                R.layout.list_item, new ArrayList<>()));
                                        hourTextView.setText("");

                                        TextView address = v.findViewById(R.id.textView15);
                                        address.setText(f.getString(R.string.event_address, ""));
                                    }
                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    //Nulla da scrivere qui...
                                }
                            });
                            dayText.setAdapter(new SpinnerArrayAdapter(f.requireContext(), R.layout.list_item, dayArr));

                            TextView duration = v.findViewById(R.id.textView12);
                            String eventDurata = event.getDurata();
                            if(event.getDurata() == null || event.getDurata().equals("")) {
                                duration.setText(f.getString(R.string.duration,
                                        "0", "0", "0"));
                            } else {
                                String[] durata = eventDurata.split(":");
                                duration.setText(f.getString(R.string.duration, durata[0], durata[1], durata[2]));
                            }
                        });
                    }
                    response.body().close();
                } else {
                    switch (response.code()) {
                        case 401 -> {
                            if (loginLauncher != null) {
                                Intent loginIntent = new Intent(f.requireActivity(), LoginActivity.class);
                                loginLauncher.launch(loginIntent);
                            } else {
                                f.requireActivity().runOnUiThread(() -> {
                                    AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
                                    dialog.setTitle(R.string.user_not_logged_in);
                                    dialog.setMessage(f.getString(R.string.user_not_logged_in_message));
                                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                                    dialog.show();
                                });
                            }
                        }
                        case 404 -> f.requireActivity().runOnUiThread(() -> {
                            AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
                            dialog.setTitle(R.string.no_org_event);
                            dialog.setMessage(f.getString(R.string.no_org_event_message));
                            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                            dialog.show();
                        });
                    }
                }
            }
        });
    }
}