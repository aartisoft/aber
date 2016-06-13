package com.mammutgroup.taxi.commons.service.remote.rest.api.auth.model;

import java.io.Serializable;

/**
 * Created by mushtu on 6/12/16.
 */
public class MobileVerificationRequest implements Serializable{

    private String mobile;

    public MobileVerificationRequest(String mobile)
    {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
