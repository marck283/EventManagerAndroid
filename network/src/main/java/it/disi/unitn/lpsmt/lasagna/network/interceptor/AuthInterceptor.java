package it.disi.unitn.lpsmt.lasagna.network.interceptor;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This class intercepts all outgoing requests
 * and adds the access token to the headers (if necessary).
 */
public class AuthInterceptor implements Interceptor {

    private String accessToken;

    /**
     * Sets the access token.
     * @param accessToken The new AccessToken
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Intercepts the request and adds the access token
     * to the headers before forwarding the request.
     * @param chain The Interceptor chain. Cannot be null.
     * @return The response to the request. Cannot be null.
     * @throws IOException If a network failure occurs.
     */
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder rbuilder = chain.request().newBuilder();
        if (accessToken != null && !accessToken.isEmpty()) {
            rbuilder.addHeader("x-access-token", accessToken);
        }

        return chain.proceed(rbuilder.build());
    }
}
