package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.eventInfo.organizedEvent;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.IOException;
import java.util.ArrayList;

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
                              @NonNull String evId, @NonNull String day, @NonNull ActivityResultLauncher<ScanOptions> launcher) {
        client = new OkHttpClient();
        this.userJwt = userJwt;
        eventId = evId;
        this.v = v;
        this.f = f;
        this.day = day;
        this.launcher = launcher;
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

                            TextView evDay = v.findViewById(R.id.textView9)/*, evHour = v.findViewById(R.id.textView10)*/;

                            /*Sistemare con l'ora richiesta*/
                            evDay.setText(f.getString(R.string.day_not_selectable, day));
                            /*evHour.setText(f.getString(R.string.time_not_selectable, ));*/

                            TextView duration = v.findViewById(R.id.textView12);
                            duration.setText(f.getString(R.string.duration, event.getDurata()));

                            TextView address = v.findViewById(R.id.textView15);
                            address.setText(f.getString(R.string.event_address, event.getLuogoEv().get(0).toString()));

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
                }
            }
        });
    }
}