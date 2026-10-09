package it.disi.unitn.lpsmt.lasagna.network.repository;

import androidx.annotation.NonNull;
import com.google.gson.JsonObject;
import it.disi.unitn.lpsmt.lasagna.network.client.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizedEventRepository {

    public interface EventListCallback {
        void onSuccess(JsonObject eventsJson);
        void onError(int statusCode, String errorMessage);
    }

    public interface ActionCallback {
        void onSuccess();
        void onError(int statusCode, String errorMessage);
    }

    public interface EventInfoCallback {
        void onSuccess(JsonObject eventInfo);
        void onError(int statusCode, String errorMessage);
    }

    private final EventListCallback el_cb;
    private final ActionCallback ac_cb;

    private final Callback<JsonObject> elcb_cb = new Callback<>() {
        @Override
        public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
            if (response.isSuccessful() && response.body() != null) {
                el_cb.onSuccess(response.body());
            } else {
                el_cb.onError(response.code(), "Error: " + response.code());
            }
        }

        @Override
        public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
            el_cb.onError(-1, t.getMessage());
        }
    };

    private final Callback<ResponseBody> accb_cb = new Callback<>() {
        @Override
        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                ac_cb.onSuccess();
            } else {
                ac_cb.onError(response.code(), "Cancel failed: " + response.code());
            }
        }

        @Override
        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
            ac_cb.onError(-1, t.getMessage());
        }
    };

    public OrganizedEventRepository() {
        this.el_cb = null;
        this.ac_cb = null;
    }

    public OrganizedEventRepository(EventListCallback el_cb) {
        this.el_cb = el_cb;
        this.ac_cb = null;
    }

    public OrganizedEventRepository(ActionCallback ac_cb) {
        this.ac_cb = ac_cb;
        this.el_cb = null;
    }

    public void getOrganizedEvents() {
        RetrofitClient.getInstance().getOrganizedEventsApi().getOrganizedEvents().enqueue(elcb_cb);
    }

    public void getOrganizedEventsByName(String name) {
        RetrofitClient.getInstance().getOrganizedEventsApi().getOrganizedEventsByName(name).enqueue(elcb_cb);
    }

    public void getOrganizedEventsByDate(String data) {
        RetrofitClient.getInstance().getOrganizedEventsApi().getOrganizedEventsByDate(data).enqueue(elcb_cb);
    }

    public void getOrganizedEventInfo(String eventId, EventInfoCallback ei_cb) {
        RetrofitClient.getInstance().getOrganizedEventsApi().getOrganizedEventInfo(eventId).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ei_cb.onSuccess(response.body());
                } else {
                    ei_cb.onError(response.code(), "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                ei_cb.onError(-1, t.getMessage());
            }
        });
    }

    public void cancelEvent(String eventId) {
        RetrofitClient.getInstance().getOrganizedEventsApi().cancelEvent(eventId).enqueue(accb_cb);
    }

    public void terminateEvent(String eventId, String data, String ora) {
        RetrofitClient.getInstance().getOrganizedEventsApi().terminateEvent(eventId, data, ora).enqueue(accb_cb);
    }
}