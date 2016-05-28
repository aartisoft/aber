package com.mammutgroup.taxi.service.remote.rest.api.vehicle;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * @author mushtu
 * @since 5/28/16.
 */
public interface VehicleService {


    @GET("/vehicle/find")
    void findTaxi(@Query("longitude") Double longitude,@Query("latitude") Double latitude);
}
