package com.mammutgroup.taxi.service.remote.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author mushtu
 * @since 5/24/16.
 */
public class MobileNumber {

    private String number;
    @SerializedName(value = "code",alternate = {"countryCode","numberCode"})
    private String code;
    @SerializedName("isVerifiedNumber")
    private Boolean isVerified;

    public MobileNumber()
    {
        this.code = "IR";
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean isVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }
}
