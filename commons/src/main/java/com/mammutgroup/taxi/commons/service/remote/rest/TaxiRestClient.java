package com.mammutgroup.taxi.commons.service.remote.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mammutgroup.taxi.commons.service.remote.model.ApiResponse;
import com.mammutgroup.taxi.commons.service.remote.model.ResponseDeserializer;
import com.mammutgroup.taxi.commons.service.remote.rest.api.auth.AuthService;
//import com.mammutgroup.taxi.commons.service.remote.rest.api.user.UserService;
import com.mammutgroup.taxi.commons.service.remote.rest.api.order.OrderService;
import com.mammutgroup.taxi.commons.service.remote.rest.api.vehicle.VehicleService;
import com.mammutgroup.taxi.commons.service.remote.rest.mock.MockOrderService;
import com.mammutgroup.taxi.commons.service.remote.rest.mock.MockVehicleService;
import retrofit.MockRestAdapter;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * @author mushtu
 * @since 5/24/16.
 */
public class TaxiRestClient {


    private static final String BASE_URL = " http://aber.i-nest.ir/"; // todo get from shared preferences
    private volatile static TaxiRestClient instance;

    protected AuthService authService;
    //protected UserService userService;
    protected VehicleService vehicleService;
    protected OrderService orderService;

    private TaxiRestClient() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ApiResponse.class,new ResponseDeserializer())
                .create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(gson))
                .build();
        MockRestAdapter mockRestAdapter = MockRestAdapter.from(restAdapter);
        MockVehicleService mockVehicleService = new MockVehicleService();
        MockOrderService mockOrderService = new MockOrderService();

        authService = restAdapter.create(AuthService.class);
        //userService = restAdapter.create(UserService.class);
        //vehicleService = restAdapter.create(VehicleService.class);

        // mocked services
        vehicleService = mockRestAdapter.create(VehicleService.class,mockVehicleService);
        orderService = mockRestAdapter.create(OrderService.class,mockOrderService);
    }

    public static TaxiRestClient getInstance() {
        if (instance == null) {
            synchronized (TaxiRestClient.class) {
                if (instance == null) {
                    instance = new TaxiRestClient();
                }

            }
        }
        return instance;
    }

    public AuthService authService() {
        return authService;
    }

    public VehicleService vehicleService(){
        return vehicleService;
    }

    public OrderService orderService() {
        return orderService;
    }


    /*public UserService userService() {
        return userService;
    }*/

}
