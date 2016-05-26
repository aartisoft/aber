package com.mammutgroup.taxi.service.remote.rest.mock;

import com.mammutgroup.taxi.service.remote.rest.api.auth.AuthService;
import com.mammutgroup.taxi.service.remote.rest.api.auth.model.Token;
import retrofit.Callback;
import retrofit.http.Field;

import java.util.UUID;

/**
 * @author mushtu
 * @since 5/26/16.
 */
public class MockAuthService implements AuthService {
    @Override
    public void getAccessToken(String username, String password, String grantType,String clientId,String clientSecret,
                               Callback<Token> callback) {
        Token token = new Token();
        token.setAccessToken(UUID.randomUUID().toString());
        token.setTokenType("Bearer");
        token.setExpires(7200L);
        callback.success(token,null);

    }
}
