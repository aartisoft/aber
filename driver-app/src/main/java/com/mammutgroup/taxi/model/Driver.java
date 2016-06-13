package com.mammutgroup.taxi.model;


import com.mammutgroup.taxi.commons.service.remote.model.BasicLocation;
import com.mammutgroup.taxi.commons.service.remote.rest.api.order.model.Order;
import com.mammutgroup.taxi.commons.service.remote.rest.api.user.model.User;

/**
 * @author mushtu
 * @since 5/31/16.
 */
public class Driver extends User {

    private BasicLocation location;
    private DriverStatus status;
    private Order currentOrder;

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public synchronized BasicLocation getLocation() {
        return location;
    }

    public synchronized void setLocation(BasicLocation location) {
        this.location = location;
    }
//
//    public boolean isReadyForService() {
//        return readyForService;
//    }
//
//    public void setReadyForService(boolean readyForService) {
//        this.readyForService = readyForService;
//    }
}
