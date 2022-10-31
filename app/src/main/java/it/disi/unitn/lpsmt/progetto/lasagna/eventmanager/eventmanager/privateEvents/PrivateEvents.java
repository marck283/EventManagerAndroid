package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.privateEvents;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventCallback;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events.EventList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrivateEvents {
    private PrivateEventsInterface privEv;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager lm;
    private PrivEvAdapter p1;

    public PrivateEvents(@NonNull View layout) {
        Retrofit retro = new Retrofit.Builder()
                .baseUrl("https://eventmanagerzlf.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        privEv = retro.create(PrivateEventsInterface.class);

        //Qui imposta la RecyclerView, il LayoutManager e l'Adapter
        mRecyclerView = layout.findViewById(R.id.personal_recycler_view);
        lm = new LinearLayoutManager(layout.getContext());
        mRecyclerView.setLayoutManager(lm);
        p1 = new PrivEvAdapter(new EventCallback());
        mRecyclerView.setAdapter(p1);
    }

    public void getPersonalEvents(String authToken, String data, ConstraintLayout l) {
        Call<JsonObject> json = privEv.privEv(authToken, data);
        json.enqueue(new Callback<JsonObject>() {

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

                        //Qui serviti della RecyclerView per impostare l'Adapter.
                        if(ev != null && ev.getList().size() > 0) {
                            p1 = new PrivEvAdapter(new EventCallback(), ev.getList());
                            p1.submitList(ev.getList());
                            mRecyclerView.setAdapter(p1);
                        } else {
                            Log.i("nullP", "Event list is null");
                        }
                    } else {
                        Log.i("noSuccess", "Unsuccessful operation");
                    }
                } else {
                    Log.i("null", "response is null");
                    Log.i("response", String.valueOf(response));
                    p1 = new PrivEvAdapter(new EventCallback(), new EventList().getList());
                    p1.clearEventList();
                    mRecyclerView.setAdapter(p1);
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
