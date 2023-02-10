package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.organizedEvent;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.LuogoEv;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.NetworkCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.EventDetailsFragment;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.callbacks.OrganizerCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.spinnerImplementation.SpinnerArrayAdapter;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrganizedEventInfo extends Thread {
    private final OkHttpClient client;

    private final String userJwt, eventId;

    private final View v;

    private final EventDetailsFragment f;

    private String day;

    private final ActivityResultLauncher<ScanOptions> launcher;

    private final ActivityResultLauncher<Intent> loginLauncher;

    public OrganizedEventInfo(@NonNull View v, @NonNull EventDetailsFragment f, @NonNull String userJwt,
                              @NonNull String evId, @NonNull String day,
                              @NonNull ActivityResultLauncher<ScanOptions> launcher) {
        client = new OkHttpClient();
        this.userJwt = userJwt;
        eventId = evId;
        this.v = v;
        this.f = f;
        this.day = day;
        this.launcher = launcher;
        this.loginLauncher = null;
    }

    public OrganizedEventInfo(@NonNull View v, @NonNull EventDetailsFragment f, @NonNull String userJwt,
                              @NonNull String evId, @NonNull ActivityResultLauncher<ScanOptions> launcher,
                              @NonNull ActivityResultLauncher<Intent> loginLauncher) {
        client = new OkHttpClient();
        this.userJwt = userJwt;
        eventId = evId;
        this.v = v;
        this.f = f;
        this.day = null;
        this.launcher = launcher;
        this.loginLauncher = loginLauncher;
    }

    public OrganizedEventInfo(@NonNull View v, @NonNull EventDetailsFragment f, @NonNull String userJwt,
                              @NonNull String evId, @NonNull ActivityResultLauncher<ScanOptions> launcher) {
        client = new OkHttpClient();
        this.userJwt = userJwt;
        eventId = evId;
        this.v = v;
        this.f = f;
        this.day = null;
        this.launcher = launcher;
        this.loginLauncher = null;
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
        AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
        dialog.setTitle(R.string.no_such_address);
        dialog.setMessage(f.getString(R.string.address_not_registered));
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
        dialog.show();
    }

    private void setNoConnectionDialog() {
        AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
        dialog.setTitle(R.string.no_connection);
        dialog.setMessage(f.getString(R.string.no_connection_message));
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
        dialog.show();
    }

    public void run() {
        Request request;
        if (day != null) {
            request = new Request.Builder()
                    .addHeader("x-access-token", userJwt)
                    .addHeader("date", day)
                    .url("https://eventmanagerzlf.herokuapp.com/api/v2/InfoEventoOrg/" + eventId)
                    .build();
        } else {
            request = new Request.Builder()
                    .addHeader("x-access-token", userJwt)
                    .url("https://eventmanagerzlf.herokuapp.com/api/v2/InfoEventoOrg/" + eventId)
                    .build();
        }
        client.newCall(request).enqueue(new OrganizerCallback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Gson gson = new Gson();
                if (response.isSuccessful() && response.body() != null) {
                    OrganizedEvent event = OrganizedEvent.parseJSON(gson.fromJson(response.body().string(), JsonObject.class));
                    Log.i("OK", "OK");

                    if (f.isAdded()) {
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

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    if (evDay.getEditText() != null &&
                                            !evDay.getEditText().getText().toString().equals("---")) {
                                        TextView address = v.findViewById(R.id.textView15);
                                        final int pos = dayArr.indexOf(evDay.getEditText().getText().toString());

                                        address.setText(f.getString(R.string.event_address, event.getLuogoEv()
                                                .get(pos - 1).getAddress()));
                                        address.setOnClickListener(c -> {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                                Geocoder geocoder = new Geocoder(f.requireActivity());
                                                geocoder.getFromLocationName(address.getText().toString(), 5, addresses -> {
                                                    if (addresses.size() > 0) {
                                                        startGoogleMaps(f, address, addresses);
                                                    } else {
                                                        noSuchAddressDialog(f);
                                                    }
                                                });
                                            } else {
                                                Thread t1 = new Thread() {
                                                    public void run() {
                                                        List<Address> addresses;
                                                        try {
                                                            Geocoder geocoder = new Geocoder(f.requireActivity());
                                                            addresses = geocoder.getFromLocationName(address.getText().toString(), 5);
                                                            if (addresses != null && addresses.size() > 0) {
                                                                startGoogleMaps(f, address, addresses);
                                                            } else {
                                                                f.requireActivity().runOnUiThread(() -> noSuchAddressDialog(f));
                                                            }
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                };
                                                t1.start();
                                            }
                                        });
                                        address.setPaintFlags(address.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                                        TextInputLayout spinner = v.findViewById(R.id.spinner);
                                        MaterialAutoCompleteTextView hourTextView = spinner.findViewById(R.id.orgHourTextView);

                                        ArrayList<CharSequence> hourArr = new ArrayList<>();
                                        hourArr.add("---");

                                        if (day == null) {
                                            String[] dayArr = evDay.getEditText().getText().toString().split("/");
                                            day = dayArr[1] + "-" + dayArr[0] + "-" + dayArr[2];
                                        }
                                        for (LuogoEv l : event.getOrari(day)) {
                                            hourArr.add(l.getOra());
                                        }

                                        hourTextView.setAdapter(new SpinnerArrayAdapter(f.requireContext(),
                                                R.layout.list_item, hourArr));

                                        hourTextView.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                            }

                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                Button qrCodeScan = v.findViewById(R.id.button8),
                                                        terminaEvento = v.findViewById(R.id.button12);
                                                //EditText editText = spinner.getEditText(), editText1 = evDay.getEditText();
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
                                                        /*qrCodeScan.setOnClickListener(c -> {
                                                            if (editText != null &&
                                                                    !editText.getText().toString().equals("") &&
                                                                    !editText.getText().toString().equals("---") &&
                                                                    editText1 != null && !editText.getText().toString().equals("") &&
                                                                    !editText.getText().toString().equals("---")) {
                                                                launcher.launch(new ScanOptions());
                                                            }
                                                        });*/

                                                        terminaEvento.setEnabled(true);
                                                        /*terminaEvento.setOnClickListener(c -> {
                                                            NetworkCallback callback = new NetworkCallback(f.requireActivity());
                                                            if (!callback.isOnline(f.requireActivity())) {
                                                                setNoConnectionDialog();
                                                            } else {
                                                                try {
                                                                    f.getViewModel().terminateEvent(userJwt,
                                                                            f, eventId, day, hourTextView.getText().toString(), v);
                                                                } catch (NullPointerException ex) {
                                                                    ex.printStackTrace();
                                                                }
                                                            }
                                                        });*/
                                                    }
                                                }
                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {

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

                                }
                            });
                            dayText.setAdapter(new SpinnerArrayAdapter(f.requireContext(), R.layout.list_item, dayArr));

                            TextView duration = v.findViewById(R.id.textView12);
                            String[] durata = event.getDurata().split(":");
                            if(event.getDurata() == null || event.getDurata().equals("")) {
                                duration.setText(f.getString(R.string.duration,
                                        f.getString(R.string.parameter_not_set), "", ""));
                            } else {
                                duration.setText(f.getString(R.string.duration, durata[0], durata[1], durata[2]));
                            }
                        });
                    }
                    response.body().close();
                } else {
                    switch (response.code()) {
                        case 401: {
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
                            break;
                        }
                        case 404: {
                            f.requireActivity().runOnUiThread(() -> {
                                AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
                                dialog.setTitle(R.string.no_org_event);
                                dialog.setMessage(f.getString(R.string.no_org_event_message));
                                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                                dialog.show();
                            });
                            break;
                        }
                    }
                }
            }
        });
    }
}
