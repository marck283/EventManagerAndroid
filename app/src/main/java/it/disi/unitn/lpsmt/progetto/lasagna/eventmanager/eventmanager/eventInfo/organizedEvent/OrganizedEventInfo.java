package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.organizedEvent;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.publicEvent.LuogoEvento;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.EventDetailsFragment;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_details.callbacks.OrganizerCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.spinnerImplementation.SpinnerArrayAdapter;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrganizedEventInfo extends Thread {
    private final OkHttpClient client;

    private final String userJwt, eventId;

    private final View v;

    private final EventDetailsFragment f;

    private final String day;

    private ActivityResultLauncher<ScanOptions> launcher;

    public OrganizedEventInfo(@NonNull View v, @NonNull EventDetailsFragment f, @NonNull String userJwt,
                              @NonNull String evId, @Nullable String day, @NonNull ActivityResultLauncher<ScanOptions> launcher) {
        client = new OkHttpClient();
        this.userJwt = userJwt;
        eventId = evId;
        this.v = v;
        this.f = f;
        this.day = day;
        this.launcher = launcher;
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

    public void run() {
        Request request = new Request.Builder()
                .addHeader("x-access-token", userJwt)
                .addHeader("date", day)
                .url("https://eventmanagerzlf.herokuapp.com/api/v2/InfoEventoOrg/" + eventId)
                .build();
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
                            Glide.with(v).load(event.decodeBase64()).into(iView);

                            TextView evName = v.findViewById(R.id.textView6);
                            evName.setText(f.getString(R.string.info_on_event, event.getEventName()));

                            TextView evDay = v.findViewById(R.id.textView9);
                            evDay.setText(f.getString(R.string.day_not_selectable, day));

                            TextView duration = v.findViewById(R.id.textView12);
                            duration.setText(f.getString(R.string.duration, event.getDurata()));

                            TextView address = v.findViewById(R.id.textView15);
                            address.setText(f.getString(R.string.event_address, event.getLuogoEv().get(0).toString()));
                            address.setOnClickListener(c -> {
                                Geocoder geocoder = new Geocoder(f.requireActivity());
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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
                                                addresses = geocoder.getFromLocationName(address.getText().toString(), 5);
                                                if (addresses != null && addresses.size() > 0) {
                                                    startGoogleMaps(f, address, addresses);
                                                } else {
                                                    Looper.prepare();
                                                    noSuchAddressDialog(f);
                                                    Looper.loop();
                                                    Looper.getMainLooper().quitSafely();
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

                            Spinner spinner = v.findViewById(R.id.spinner);

                            ArrayList<CharSequence> hourArr = new ArrayList<>();
                            hourArr.add("---");
                            for (LuogoEvento l : event.getLuogoEv()) {
                                hourArr.add(l.getOra());
                            }

                            spinner.setAdapter(new SpinnerArrayAdapter(f.requireContext(), R.layout.list_item, hourArr));

                            Button qrCodeScan = v.findViewById(R.id.button8);
                            qrCodeScan.setOnClickListener(c -> {
                                if(spinner.getSelectedItem() != "---") {
                                    launcher.launch(new ScanOptions());
                                }
                            });
                        });
                    }
                    response.body().close();
                } else {
                    switch(response.code()) {
                        case 401: {

                        }
                    }
                }
            }
        });
    }
}
