package it.disi.unitn.lpsmt.lasagna.network.api;

import com.google.gson.JsonObject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReviewApi {

    @GET("/api/v2/EventiPubblici/{id}/recensioni")
    Call<JsonObject> getEventReviews(@Path("id") String id);

    @FormUrlEncoded
    @POST("/api/v2/Recensioni/{eventId}")
    Call<ResponseBody> postReview(
            @Path("eventId") String eventId,
            @Field("title") String title,
            @Field("evaluation") String evaluation,
            @Field("description") String description
    );
}