package com.mammutgroup.taxi.commons.service.remote.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mushtu on 6/12/16.
 */
public class ApiResponse implements Serializable {

    @SerializedName("message")
    private String message;
    @SerializedName("status_code")
    private String statusCode;
    private BaseDto singleData;
    private List<BaseDto> listData;

    private boolean dataIsSingleObject;


    public BaseDto getSingleData() {
        return singleData;
    }

    public void setSingleData(BaseDto singleData) {
        this.singleData = singleData;
    }

    public List<BaseDto> getListData() {
        return listData;
    }

    public void setListData(List<BaseDto> listData) {
        this.listData = listData;
    }

    public boolean dataIsSingleObject() {
        return dataIsSingleObject;
    }

    public void setDataIsSingleObject(boolean dataIsSingleObject) {
        this.dataIsSingleObject = dataIsSingleObject;
    }

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
