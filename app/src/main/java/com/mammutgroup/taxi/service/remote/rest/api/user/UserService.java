package com.mammutgroup.taxi.service.remote.rest.api.user;

import com.mammutgroup.taxi.service.remote.rest.api.user.model.RegisterRequest;
import com.mammutgroup.taxi.service.remote.rest.api.user.model.RegisterResponse;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * @author mushtu
 * @since 5/24/16.
 */
public interface UserService {

    @POST("/user")
    void register(@Body RegisterRequest request, Callback<RegisterResponse> callback);

}
