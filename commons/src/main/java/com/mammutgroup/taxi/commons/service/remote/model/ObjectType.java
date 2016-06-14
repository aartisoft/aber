package com.mammutgroup.taxi.commons.service.remote.model;

import java.io.Serializable;

/**
 * @author mushtu
 * @since 6/14/16.
 */
public enum  ObjectType implements Serializable {
    USER("User"),
    VEHICLE("Vehicle"),
    TRIP("Trip");


    private final String value;
    private ObjectType(final String value)
    {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
