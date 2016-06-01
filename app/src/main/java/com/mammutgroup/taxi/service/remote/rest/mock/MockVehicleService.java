package com.mammutgroup.taxi.service.remote.rest.mock;

import com.mammutgroup.taxi.model.BasicLocation;
import com.mammutgroup.taxi.service.remote.rest.api.vehicle.VehicleService;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Query;

/**
 * @author mushtu
 * @since 6/1/16.
 */
public class MockVehicleService implements VehicleService {



    @Override
    public void findTaxi(@Query("longitude") Double longitude, @Query("latitude") Double latitude) {

    }

    @Override
    public Response sendLocation(@Body BasicLocation location) {

        return null;
    }
}
