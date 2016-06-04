package com.mammutgroup.taxi.service.remote.model;

import java.io.Serializable;

/**
 * @author mushtu
 * @since 5/24/16.
 */
public class BaseDto implements Serializable{
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
