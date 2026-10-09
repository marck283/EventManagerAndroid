package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.events;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import it.disi.unitn.lpsmt.lasagna.localdatabase.queryClasses.DBOrgEvents;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.R;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.organizedEvents.OrgEvAdapter;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.privateEvents.PrivEvAdapter;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.publicEvents.PubEvAdapter;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.event_management.EventManagementFragment;
import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.ui.user_login.ui.login.LoginActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class JsonCallback implements Callback {
    private static AlertDialog activeNoEventDialog = null;
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

    public JsonCallback(@Nullable Fragment f, String type, RecyclerView view,
                        @NonNull ActivityResultLauncher<Intent> launcher) {
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

    public JsonCallback(@Nullable Fragment f, String type, RecyclerView view) {
        this.type = type;
        mRecyclerView = view;
        this.f = f;
        this.day = null;
        this.executor = null;
        launcher = null;
    }

    private void initAdapter(@Nullable Fragment f, EventList ev, @Nullable String day) {
        switch (type) {
            case "org" -> {
                if (day != null) {
                    p1 = new OrgEvAdapter(new EventCallback(), ev.getList(), day);
                } else {
                    p1 = new OrgEvAdapter(new EventCallback(), ev.getList());
                }

                if (f != null) {
                    Activity activity = f.getActivity();
                    if (f instanceof EventManagementFragment && activity != null && f.isAdded()) {
                        DBOrgEvents dbOrg = new DBOrgEvents(f, "updateAll", ev.getList(), mRecyclerView);
                        dbOrg.start();
                    }
                }
            }
            case "priv" -> {
                this.day = day;
                p1 = new PrivEvAdapter(new EventCallback(), ev.getList(), day);
            }
            case "pub" -> {
                if (f != null) {
                    p1 = new PubEvAdapter(f, new EventCallback(), ev.getList());
                }
            }
            default -> Log.i("noCategory", "no category with that name");
        }
    }

    public void handleJsonSuccess(JsonObject body) {
        EventList ev = new EventList();
        ev = ev.parseJSON(body);
        if (ev != null && !ev.getList().isEmpty()) {
            initAdapter(f, ev, day);
            p1.submitList(ev.getList());

            if (f != null) {
                Activity activity = f.getActivity();
                if (activity != null && !activity.isFinishing() && !activity.isDestroyed() && f.isAdded()) {
                    activity.runOnUiThread(() -> mRecyclerView.setAdapter(p1));
                }
            }

            if (executor != null) {
                executor.shutdown();
            }
        }
    }

    public void handleJsonError(int statusCode) {
        switch (statusCode) {
            case 401 -> {
                if (f != null && launcher != null) {
                    Activity activity = f.getActivity();
                    if (activity != null && !activity.isFinishing() && !activity.isDestroyed() && f.isAdded()) {
                        activity.runOnUiThread(() -> {
                            Intent loginIntent = new Intent(activity, LoginActivity.class);
                            launcher.launch(loginIntent);
                        });
                    }
                }
            }
            case 404 -> {
                if (f != null) {
                    Activity activity = f.getActivity();
                    if (activity != null && !activity.isFinishing() && !activity.isDestroyed() && f.isAdded()) {
                        activity.runOnUiThread(() -> {
                            if (!activity.isFinishing() && !activity.isDestroyed()) {
                                if (activeNoEventDialog == null || !activeNoEventDialog.isShowing()) {
                                    AlertDialog dialog = new AlertDialog.Builder(activity).create();
                                    dialog.setTitle(R.string.no_org_event);
                                    dialog.setMessage(f.getString(R.string.no_org_event_message));
                                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> {
                                        dialog1.dismiss();
                                        activeNoEventDialog = null;
                                    });
                                    dialog.setOnDismissListener(d -> activeNoEventDialog = null);
                                    activeNoEventDialog = dialog;
                                    dialog.show();
                                }

                                initAdapter(f, new EventList(), day);
                                if (p1 != null) {
                                    p1.clearEventList();
                                }
                                mRecyclerView.setAdapter(p1);
                            }
                        });
                    }
                }
            }
            case 500 -> {
                if (f != null) {
                    Activity activity = f.getActivity();
                    if (activity != null && !activity.isFinishing() && !activity.isDestroyed() && f.isAdded()) {
                        activity.runOnUiThread(() -> {
                            if (!activity.isFinishing() && !activity.isDestroyed()) {
                                AlertDialog dialog = new AlertDialog.Builder(activity).create();
                                dialog.setTitle(R.string.unknown_error);
                                dialog.setMessage(f.getString(R.string.unknown_error_message));
                                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> dialog1.dismiss());
                                dialog.show();
                            }
                        });
                    }
                }
            }
        }
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) {
        if (response.isSuccessful()) {
            try {
                Gson gson = new GsonBuilder().create();
                JsonObject body = gson.fromJson(response.body().string(), JsonObject.class);
                handleJsonSuccess(body);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            response.body().close();
        } else {
            handleJsonError(response.code());
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
    public void onFailure(@NonNull Call call, @NonNull IOException t) {
        try {
            throw t;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
