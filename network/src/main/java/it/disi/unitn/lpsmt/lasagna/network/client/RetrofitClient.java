package it.disi.unitn.lpsmt.lasagna.network.client;

import it.disi.unitn.lpsmt.lasagna.network.api.AuthApi;
import it.disi.unitn.lpsmt.lasagna.network.api.EventApi;
import it.disi.unitn.lpsmt.lasagna.network.api.OrganizedEventsApi;
import it.disi.unitn.lpsmt.lasagna.network.api.ReviewApi;
import it.disi.unitn.lpsmt.lasagna.network.api.TicketApi;
import it.disi.unitn.lpsmt.lasagna.network.api.UserApi;
import it.disi.unitn.lpsmt.lasagna.network.interceptor.AuthInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://eventmanager-85ec.onrender.com";
    private static RetrofitClient instance;
    private final AuthApi authApi;
    private final UserApi userApi;
    private final OrganizedEventsApi organizedEventsApi;
    private final EventApi eventApi;
    private final TicketApi ticketApi;
    private final ReviewApi reviewApi;
    private final AuthInterceptor authInterceptor;

    private RetrofitClient() {
        authInterceptor = new AuthInterceptor();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        authApi = retrofit.create(AuthApi.class);
        userApi = retrofit.create(UserApi.class);
        organizedEventsApi = retrofit.create(OrganizedEventsApi.class);
        eventApi = retrofit.create(EventApi.class);
        ticketApi = retrofit.create(TicketApi.class);
        reviewApi = retrofit.create(ReviewApi.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public AuthApi getAuthApi() {
        return authApi;
    }

    public UserApi getUserApi() {
        return userApi;
    }

    public OrganizedEventsApi getOrganizedEventsApi() {
        return organizedEventsApi;
    }

    public EventApi getEventApi() {
        return eventApi;
    }

    public TicketApi getTicketApi() {
        return ticketApi;
    }

    public ReviewApi getReviewApi() {
        return reviewApi;
    }

    public void setAccessToken(String token) {
        authInterceptor.setAccessToken(token);
    }
}