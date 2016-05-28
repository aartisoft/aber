package com.mammutgroup.taxi.service.remote.rest.mock;

import com.mammutgroup.taxi.service.remote.model.MobileNumber;
import com.mammutgroup.taxi.service.remote.rest.api.user.UserService;
import com.mammutgroup.taxi.service.remote.rest.api.user.model.*;
import retrofit.Callback;
import retrofit.http.Body;

import java.util.UUID;

/**
 * @author mushtu
 * @since 5/26/16.
 */
public class MockUserService implements UserService {

    @Override
    public void register(RegisterRequest request, Callback<RegisterResponse> callback) {

        //callback.success(response(),new Response());
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setAccessToken(UUID.randomUUID().toString());
        registerResponse.setUser(user(request.getUsername(),request.getPassword()));
        callback.success(registerResponse,null);


    }

    @Override
    public void changeMobileNumber(MobileNumber number, Callback<Object> callback) {
        callback.success(new Object(),null);
    }


    @Override
    public void verifyMobileNumber(VerificationCode code, Callback<VerificationCodeResponse> callback) {
        callback.success(new VerificationCodeResponse(),null);
    }


    private User user(String username,String password)
    {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setId(UUID.randomUUID().toString());
        return user;
    }

}
