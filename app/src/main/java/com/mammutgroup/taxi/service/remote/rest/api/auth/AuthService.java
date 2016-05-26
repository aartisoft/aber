package com.mammutgroup.taxi.service.remote.rest.api.auth;

import com.mammutgroup.taxi.service.remote.rest.api.auth.model.Token;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * @author mushtu
 * @since 5/24/16.
 */
public interface AuthService {

    @FormUrlEncoded
    @POST("/auth/oauth/v2/token")
    void getAccessToken(@Field("username") String username, @Field("password") String password,
                        @Field("grant_type") String grantType, @Field("client_id") String clientId,
                        @Field("client_secret") String clientSecret, Callback<Token> callback);
}
