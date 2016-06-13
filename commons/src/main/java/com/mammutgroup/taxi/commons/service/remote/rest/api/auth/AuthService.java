package com.mammutgroup.taxi.commons.service.remote.rest.api.auth;

import com.mammutgroup.taxi.commons.service.remote.model.ApiResponse;
import com.mammutgroup.taxi.commons.service.remote.rest.api.auth.model.AccessTokenRequest;
import com.mammutgroup.taxi.commons.service.remote.rest.api.auth.model.MobileVerificationRequest;
import com.mammutgroup.taxi.commons.service.remote.rest.api.auth.model.Token;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * @author mushtu
 * @since 5/24/16.
 */
public interface AuthService {

    @POST("/api/auth/token")
    void getAccessToken(@Body AccessTokenRequest request, Callback<Token> callback);

    @POST("/api/auth/verification")
    void verifyMobile(@Body MobileVerificationRequest request, Callback<ApiResponse> callback);

}
