package com.mammutgroup.taxi.commons.service.remote.rest.api.auth.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mushtu on 6/12/16.
 */
public class AccessTokenRequest {

    @SerializedName("client_id")
    private String clientId;
    @SerializedName("client_secret")
    private String clientSecret;
    @SerializedName("scope")
    private String scope;
    @SerializedName("grant_type")
    private String grantType;
/*
    public AccessTokenRequest()
    {
        this.clientId = "72827391eb471ed2d8174d3de4b8171a";
        this.clientSecret = "87d45be523522cd7425efd0ea61d2583981384f4";
    }*/

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }
}
