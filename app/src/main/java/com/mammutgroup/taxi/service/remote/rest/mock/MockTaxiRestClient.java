package com.mammutgroup.taxi.service.remote.rest.mock;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mammutgroup.taxi.service.remote.rest.ItemTypeAdapterFactory;
import com.mammutgroup.taxi.service.remote.rest.TaxiRestClient;
import com.mammutgroup.taxi.service.remote.rest.api.auth.AuthService;
import com.mammutgroup.taxi.service.remote.rest.api.login.LoginService;
import com.mammutgroup.taxi.service.remote.rest.api.order.OrderService;
import com.mammutgroup.taxi.service.remote.rest.api.report.ReportService;
import com.mammutgroup.taxi.service.remote.rest.api.user.UserService;
import com.mammutgroup.taxi.service.remote.rest.api.vehicle.VehicleService;
import retrofit.MockRestAdapter;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * @author mushtu
 * @since 5/26/16.
 */
public class MockTaxiRestClient extends TaxiRestClient {

    private static final String BASE_URL = "http://78.110.116.137:8080/api/v1/"; // todo get from shared preferences

    public MockTaxiRestClient() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(gson))
                .build();
        MockRestAdapter mockRestAdapter = MockRestAdapter.from(restAdapter);
        // instantiate mock services
        MockUserService mockUserService = new MockUserService();
        MockLoginService mockLoginService = new MockLoginService();
        MockAuthService mockAuthService = new MockAuthService();
        MockOrderService mockOrderService = new MockOrderService();
        MockReportService mockReportService = new MockReportService();
        MockVehicleService mockVehicleService = new MockVehicleService();
        authService = mockRestAdapter.create(AuthService.class, mockAuthService);
        loginService = mockRestAdapter.create(LoginService.class, mockLoginService);
        orderService = mockRestAdapter.create(OrderService.class, mockOrderService);
        reportService = mockRestAdapter.create(ReportService.class, mockReportService);
        userService = mockRestAdapter.create(UserService.class, mockUserService);
        vehicleService = mockRestAdapter.create(VehicleService.class,mockVehicleService);
    }

}
