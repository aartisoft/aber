package com.mammutgroup.taxi.service.remote.rest.api.user.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author mushtu
 * @since 5/28/16.
 */
public class VerificationCode {

    @SerializedName("verifyCode")
    private String verifyCode;

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
