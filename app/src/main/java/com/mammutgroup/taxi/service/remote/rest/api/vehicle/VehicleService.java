package com.mammutgroup.taxi.service.remote.rest.api.vehicle;

import com.mammutgroup.taxi.model.BasicLocation;
import com.mammutgroup.taxi.service.remote.rest.api.vehicle.model.TaxiLatLng;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Query;

/**
 * @author mushtu
 * @since 5/28/16.
 */
public interface VehicleService {


    @GET("/vehicle/find")
    void findTaxi(@Query("longitude") Double longitude, @Query("latitude") Double latitude);

    @PUT("/vehicle/location")
    Response sendLocation(@Body BasicLocation location);

    @GET("/vehicle/lastLocation")
    void taxiLastLocation(@Query("taxiId") String taxiId, Callback<TaxiLatLng> callback);


}
