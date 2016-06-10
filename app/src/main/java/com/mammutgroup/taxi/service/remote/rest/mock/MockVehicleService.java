package com.mammutgroup.taxi.service.remote.rest.mock;

import com.mammutgroup.taxi.model.BasicLocation;
import com.mammutgroup.taxi.service.remote.rest.api.vehicle.VehicleService;
import com.mammutgroup.taxi.service.remote.rest.api.vehicle.model.TaxiLatLng;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Query;

/**
 * @author mushtu
 * @since 6/1/16.
 */
public class MockVehicleService implements VehicleService {


    static int testState = 1;

    @Override
    public void findTaxi(@Query("longitude") Double longitude, @Query("latitude") Double latitude) {

    }

    @Override
    public Response sendLocation(@Body BasicLocation location) {

        return null;
    }

    @Override
    public void taxiLastLocation(@Body String taxiId, Callback<TaxiLatLng> callback) {
        TaxiLatLng taxiLatLng = new TaxiLatLng();
        switch (testState) {
            case 1:
                taxiLatLng.setLat(35.699335f);
                taxiLatLng.setLng(51.419559f);
                testState++;
                break;
            case 2:
                taxiLatLng.setLat(35.699353f);
                taxiLatLng.setLng(51.420181f);
                testState++;
                break;
            case 3:
                taxiLatLng.setLat(35.699387f);
                taxiLatLng.setLng(51.421640f);
                testState++;
                break;
            default:
                taxiLatLng.setLat(35.699387f);
                taxiLatLng.setLng(51.422640f);
                testState = 1;
                break;
        }
        callback.success(taxiLatLng, null);
    }
}
