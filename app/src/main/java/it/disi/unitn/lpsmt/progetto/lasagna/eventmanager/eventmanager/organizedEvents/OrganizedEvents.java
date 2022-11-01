package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.organizedEvents;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventList;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.privateEvents.PrivEvAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrganizedEvents {
    private final OrganizedEventsInterface orgEv;
    private final RecyclerView rv;
    private final RecyclerView.LayoutManager lm;
    private OrgEvAdapter p1;

    public OrganizedEvents(@NonNull View layout) {
        Retrofit retro = new Retrofit.Builder()
                .baseUrl("https://eventmanagerzlf.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        orgEv = retro.create(OrganizedEventsInterface.class);
        rv = layout.findViewById(R.id.organizer_recycler_view);
        lm = new LinearLayoutManager(layout.getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(lm);
        p1 = new OrgEvAdapter(new EventCallback());
        rv.setAdapter(p1);
    }

    public void getOrgEvents(String authToken, String data) {
        Call<JsonObject> call = orgEv.orgEv(authToken, data);
        call.enqueue(new Callback<JsonObject>() {

            /**
             * Invoked for a received HTTP response.
             *
             * <p>Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call {@link Response#isSuccessful()} to determine if the response indicates success.
             *
             * @param call
             * @param response
             */
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                EventList ev = new EventList();
                if(response.body() != null) {
                    if(response.isSuccessful()) {
                        ev = ev.parseJSON(response.body());
                        Log.i("orgEvResponse", String.valueOf(response.body()));
                        if(ev != null && ev.getList().size() > 0) {
                            p1 = new OrgEvAdapter(new EventCallback(), ev.getList());
                            p1.submitList(ev.getList());
                            rv.setAdapter(p1);
                        } else {
                            Log.i("nullP", "Event list is null");
                        }
                    } else {
                        Log.i("fail", "Unsuccessful operation");
                    }
                } else {
                    Log.i("noResponse", "response is null");
                    p1 = new OrgEvAdapter(new EventCallback(), new EventList().getList());
                    p1.clearEventList();
                    rv.setAdapter(p1);
                }
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected exception
             * occurred creating the request or processing the response.
             *
             * @param call
             * @param t
             */
            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                try {
                    throw t;
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
