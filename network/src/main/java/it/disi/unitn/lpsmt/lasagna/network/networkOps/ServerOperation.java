package it.disi.unitn.lpsmt.lasagna.network.networkOps;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.disi.unitn.lpsmt.lasagna.network.NetworkRequest;
import okhttp3.Dispatcher;
import okhttp3.ResponseBody;

public abstract class ServerOperation extends Thread {
    private final String baseUrl;

    private final NetworkRequest request;

    protected final ExecutorService executor;

    public ServerOperation() {
        baseUrl = "https://eventmanager-uo29.onrender.com";
        executor = Executors.newFixedThreadPool(1);
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(1);
        dispatcher.setMaxRequestsPerHost(1);

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

    public abstract void run();
}
