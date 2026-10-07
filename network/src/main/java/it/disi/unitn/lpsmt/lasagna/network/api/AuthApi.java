package it.disi.unitn.lpsmt.lasagna.network.api;

import it.disi.unitn.lpsmt.lasagna.network.model.CsrfResponse;
import it.disi.unitn.lpsmt.lasagna.network.model.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthApi {
    @GET("/api/v2/csrfToken")
    Call<CsrfResponse> getCsrfToken();

    @FormUrlEncoded
    @POST("/api/v2/authentications")
    Call<LoginResponse> loginWithGoogle(
            @Field("csrfToken") String csrfToken,
            @Field("googleJwt") String googleJwt
    );
}
