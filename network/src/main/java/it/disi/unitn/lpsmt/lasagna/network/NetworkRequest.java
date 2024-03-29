package it.disi.unitn.lpsmt.lasagna.network;

import android.util.Pair;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class NetworkRequest {
    private final OkHttpClient client;

    public NetworkRequest() {
        client = new OkHttpClient();
    }

    public NetworkRequest(@NotNull Dispatcher dispatcher) {
        client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .dispatcher(dispatcher)
                .build();
    }

    @NonNull
    private Request.Builder getRequestBuilder(List<Pair<String, String>> headers) {
        Request.Builder builder = new Request.Builder();

        if(headers != null && headers.size() > 0) {
            for(Pair<String, String> header: headers) {
                if(header.second != null && !header.second.equals("")) {
                    builder.addHeader(header.first, header.second);
                }
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
