package com.mammutgroup.taxi.service.local;

import android.location.Location;
import io.nlopez.smartlocation.OnLocationUpdatedListener;

/**
 * @author mushtu
 * @since 5/24/16.
 */
public interface LocationService {

    Location getLastLocation();
    void requestLocationUpdates(OnLocationUpdatedListener listener) throws ServiceNotStartedException;
    void removeLocationUpdates(OnLocationUpdatedListener listener);
}
