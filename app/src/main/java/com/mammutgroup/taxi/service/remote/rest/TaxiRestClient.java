package com.mammutgroup.taxi.service.remote.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mammutgroup.taxi.service.remote.rest.api.auth.AuthService;
import com.mammutgroup.taxi.service.remote.rest.api.login.LoginService;
import com.mammutgroup.taxi.service.remote.rest.api.order.OrderService;
import com.mammutgroup.taxi.service.remote.rest.api.report.ReportService;
import com.mammutgroup.taxi.service.remote.rest.api.user.UserService;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * @author mushtu
 * @since 5/24/16.
 */
public class TaxiRestClient {
    private static final String BASE_URL = "http://78.110.116.137:8080/api/v1/"; // todo get from shared preferences

    protected AuthService authService;
    protected LoginService loginService;
    protected OrderService orderService;
    protected ReportService reportService;
    protected UserService userService;

    public TaxiRestClient() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(gson))
                .build();

        authService = restAdapter.create(AuthService.class);
        loginService = restAdapter.create(LoginService.class);
        orderService = restAdapter.create(OrderService.class);
        reportService = restAdapter.create(ReportService.class);
        userService = restAdapter.create(UserService.class);
    }

    public AuthService authService() {
        return authService;
    }

    public LoginService loginService() {
        return loginService;
    }

    public OrderService orderService() {
        return orderService;
    }

    public ReportService reportService() {
        return reportService;
    }

    public UserService userService() {
        return userService;
    }
}
