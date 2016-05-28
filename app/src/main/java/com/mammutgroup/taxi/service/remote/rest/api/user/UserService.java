package com.mammutgroup.taxi.service.remote.rest.api.user;

import com.mammutgroup.taxi.service.remote.model.MobileNumber;
import com.mammutgroup.taxi.service.remote.rest.api.user.model.RegisterRequest;
import com.mammutgroup.taxi.service.remote.rest.api.user.model.RegisterResponse;
import com.mammutgroup.taxi.service.remote.rest.api.user.model.VerificationCode;
import com.mammutgroup.taxi.service.remote.rest.api.user.model.VerificationCodeResponse;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.PUT;

/**
 * @author mushtu
 * @since 5/24/16.
 */
public interface UserService {

    @POST("/user")
    void register(@Body RegisterRequest request, Callback<RegisterResponse> callback);

    @PUT("/user/verify")
    void changeMobileNumber(@Body MobileNumber number,Callback<Object> callback);

    @PUT("/user/verify")
    void verifyMobileNumber(@Body VerificationCode code, Callback<VerificationCodeResponse> callback);

}
