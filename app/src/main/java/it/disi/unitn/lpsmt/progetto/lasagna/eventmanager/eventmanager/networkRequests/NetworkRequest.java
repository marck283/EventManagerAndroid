package it.disi.unitn.lpsmt.progetto.lasagna.eventmanager.eventmanager.networkRequests;

import android.util.Pair;

import androidx.annotation.NonNull;

import java.util.List;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class NetworkRequest {
    private final OkHttpClient client;

    public NetworkRequest() {
        client = new OkHttpClient();
    }

    private Request.Builder getRequestBuilder(List<Pair<String, String>> headers) {
        Request.Builder builder = new Request.Builder();

        if(headers != null && headers.size() > 0) {
            for(Pair<String, String> header: headers) {
                builder.addHeader(header.first, header.second);
            }
        }

        return builder;
    }

    public Request getRequest(List<Pair<String, String>> headers, @NonNull String url) {
        return getRequestBuilder(headers).url(url).build();
    }

    public Request getDeleteRequest(List<Pair<String, String>> headers, @NonNull String url) {
        return getRequestBuilder(headers).url(url).delete().build();
    }

    public Request getPatchRequest(@NonNull RequestBody body, List<Pair<String, String>> headers, @NonNull String url) {
        return getRequestBuilder(headers).url(url).patch(body).build();
    }

    public Request getPostRequest(@NonNull RequestBody body, List<Pair<String, String>> headers, @NonNull String url) {
        return getRequestBuilder(headers).url(url).post(body).build();
    }

    public void enqueue(@NonNull Request request, @NonNull Callback callback) {
        client.newCall(request).enqueue(callback);
    }
}
