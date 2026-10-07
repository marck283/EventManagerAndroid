package it.disi.unitn.lpsmt.lasagna.network.api;

import com.google.gson.JsonObject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TicketApi {

    @POST("/api/v2/EventiPubblici/{eventId}/Iscrizioni")
    Call<ResponseBody> registerForPublicEvent(@Path("eventId") String eventId);

    @DELETE("/api/v2/EventiPubblici/{eventId}/Iscrizioni/{ticketId}")
    Call<ResponseBody> deleteTicket(
            @Path("eventId") String eventId,
            @Path("ticketId") String ticketId,
            @Header("data") String data,
            @Header("ora") String ora
    );

    @GET("/api/v2/ticket/{eventId}")
    Call<JsonObject> getTicketInfo(@Path("eventId") String eventId);

    @GET("/api/v2/QRCodeCheck/{qrCode}")
    Call<ResponseBody> checkQRCode(@Path("qrCode") String qrCode);
}