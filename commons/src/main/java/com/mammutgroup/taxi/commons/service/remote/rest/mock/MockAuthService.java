package com.mammutgroup.taxi.commons.service.remote.rest.mock;

import com.mammutgroup.taxi.commons.service.remote.model.ApiResponse;
import com.mammutgroup.taxi.commons.service.remote.rest.api.auth.AuthService;
import com.mammutgroup.taxi.commons.service.remote.rest.api.auth.model.AccessTokenRequest;
import com.mammutgroup.taxi.commons.service.remote.rest.api.auth.model.MobileVerificationRequest;
import com.mammutgroup.taxi.commons.service.remote.rest.api.auth.model.Token;
import retrofit.Callback;
import retrofit.http.Body;

import java.util.UUID;

/**
 * @author mushtu
 * @since 5/26/16.
 */
public class MockAuthService implements AuthService {

    @Override
    public void getAccessToken(@Body AccessTokenRequest request, Callback<Token> callback) {
        Token token = new Token();
        token.setAccessToken(UUID.randomUUID().toString());
        token.setRefreshToken(UUID.randomUUID().toString());
        token.setTokenType("Bearer");
        token.setExpires(7200L);
        callback.success(token,null);
    }

    @Override
    public void verifyMobile(@Body MobileVerificationRequest request, Callback<ApiResponse> callback) {
        ApiResponse response = new ApiResponse();
        response.setMessage("sended!!");
        response.setStatusCode("200");
        callback.success(response,null);
    }
}
