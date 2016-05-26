package com.mammutgroup.taxi.service.remote.rest.mock;

import com.mammutgroup.taxi.service.remote.rest.api.user.UserService;
import com.mammutgroup.taxi.service.remote.rest.api.user.model.RegisterRequest;
import com.mammutgroup.taxi.service.remote.rest.api.user.model.RegisterResponse;
import com.mammutgroup.taxi.service.remote.rest.api.user.model.User;
import retrofit.Callback;

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

    private User user(String username,String password)
    {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setId(UUID.randomUUID().toString());
        return user;
    }

}
