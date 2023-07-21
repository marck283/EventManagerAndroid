package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.networkOps;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.network.NetworkRequest;
import okhttp3.Dispatcher;
import okhttp3.ResponseBody;

public abstract class ServerOperation extends Thread {
    private String baseUrl;
    private NetworkRequest request;

    public ServerOperation() {
        baseUrl = "https://eventmanager-uo29.onrender.com";
        request = new NetworkRequest();
    }

    public void createOperation(@NotNull Dispatcher dispatcher) {
        baseUrl = "https://eventmanager-uo29.onrender.com";
        request = new NetworkRequest(dispatcher);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public NetworkRequest getNetworkRequest() {
        return request;
    }

    public JsonObject parseBody(@NotNull ResponseBody body) throws IOException {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(body.string(), JsonObject.class);
    }
}
