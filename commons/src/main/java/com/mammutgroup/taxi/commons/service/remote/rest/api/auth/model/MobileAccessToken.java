package com.mammutgroup.taxi.commons.service.remote.rest.api.auth.model;

/**
 * Created by mushtu on 6/12/16.
 */
public class MobileAccessToken extends AccessTokenRequest {

    private String mobile;
    private String token;

    public MobileAccessToken()
    {
        setGrantType("mobile");
    }
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
