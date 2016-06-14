package com.mammutgroup.taxi.commons.service.remote.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author mushtu
 * @since 6/14/16.
 */
public class DataObject implements Serializable{

    private ObjectType type;
    private String id;
    @SerializedName("attributes")
    private Object object;

    public ObjectType getType() {
        return type;
    }

    public void setType(ObjectType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
