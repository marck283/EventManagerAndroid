package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.util.concurrent.ExecutorService;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.organizedEvents.OrgEvAdapter;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.privateEvents.PrivEvAdapter;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents.PubEvAdapter;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JsonCallback implements Callback<JsonObject> {
    private final String type;
    private String day;
    private EventAdapter p1;
    private final RecyclerView mRecyclerView;

    private final Fragment f;

    private final ActivityResultLauncher<Intent> launcher;

    private final ExecutorService executor;

    public JsonCallback(@Nullable Fragment f, String type, RecyclerView view, @Nullable String day) {
        this.type = type;
        mRecyclerView = view;
        this.f = f;
        this.day = day;
        executor = null;
        launcher = null;
    }

    public JsonCallback(@Nullable Fragment f, String type, RecyclerView view, @Nullable String day,
                        @NonNull ActivityResultLauncher<Intent> launcher) {
        this.type = type;
        mRecyclerView = view;
        this.f = f;
        this.day = day;
        executor = null;
        this.launcher = launcher;
    }

    public JsonCallback(@Nullable Fragment f, String type, RecyclerView view, @NonNull ActivityResultLauncher<Intent> launcher) {
        this.type = type;
        mRecyclerView = view;
        this.f = f;
        this.day = null;
        executor = null;
        this.launcher = launcher;
    }

    public JsonCallback(@Nullable Fragment f, String type, RecyclerView view, @Nullable String day,
                        @NonNull ExecutorService executor) {
        this.type = type;
        mRecyclerView = view;
        this.f = f;
        this.day = day;
        this.executor = executor;
        launcher = null;
    }

    private void initAdapter(@Nullable Fragment f, EventList ev, @Nullable String day) {
        switch(type) {
            case "org": {
                if(day != null) {
                    p1 = new OrgEvAdapter(new EventCallback(), ev.getList(), day);
                } else {
                    p1 = new OrgEvAdapter(new EventCallback(), ev.getList());
                }
                break;
            }
            case "priv": {
                this.day = day;
                p1 = new PrivEvAdapter(new EventCallback(), ev.getList(), day);
                break;
            }
            case "pub": {
                if(f != null) {
                    p1 = new PubEvAdapter(f, new EventCallback(), ev.getList());
                }
                break;
            }
            default: {
                Log.i("noCategory", "no category with that name");
                break;
            }
        }
    }

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
                Log.i("orgEvResponse", String.valueOf(response.body()));
                ev = ev.parseJSON(response.body());
                if(ev != null && ev.getList().size() > 0) {
                    initAdapter(f, ev, day);
                    p1.submitList(ev.getList());
                    mRecyclerView.setAdapter(p1);

                    if(executor != null) {
                        //Chiudo la pool di connessioni per terminare i thread in essa contenuti
                        executor.shutdown();
                    }
                } else {
                    Log.i("nullP", "Event list is null");
                }
            } else {
                Log.i("fail", "Unsuccessful operation");
                switch(response.code()) {
                    case 401: {
                        if(f != null && launcher != null) {
                            Intent loginIntent = new Intent(f.requireActivity(), LoginActivity.class);
                            launcher.launch(loginIntent);
                        }
                        break;
                    }
                    case 404: {
                        if(f != null) {
                            AlertDialog dialog = new AlertDialog.Builder(f.requireActivity()).create();
                            dialog.setTitle(R.string.no_org_event);
                            dialog.setMessage(f.getString(R.string.no_org_event_message));
                            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                            dialog.show();
                        }
                        break;
                    }
                }
            }
        } else {
            Log.i("noResponse", "response is null");
            initAdapter(f, new EventList(), day);
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
}
