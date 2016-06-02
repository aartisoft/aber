package com.mammutgroup.taxi.model;


/**
 * @author mushtu
 * @since 5/31/16.
 */
public class Driver {

    private BasicLocation location;
    private volatile boolean readyForService;


    public synchronized BasicLocation getLocation() {
        return location;
    }

    public synchronized void setLocation(BasicLocation location) {
        this.location = location;
    }

    public boolean isReadyForService() {
        return readyForService;
    }

    public void setReadyForService(boolean readyForService) {
        this.readyForService = readyForService;
    }
}
