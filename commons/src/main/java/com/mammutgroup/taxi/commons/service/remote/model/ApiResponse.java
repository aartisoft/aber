package com.mammutgroup.taxi.commons.service.remote.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mushtu on 6/12/16.
 */
public class ApiResponse implements Serializable {

    @SerializedName("message")
    private String message;
    @SerializedName("status_code")
    private String statusCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
