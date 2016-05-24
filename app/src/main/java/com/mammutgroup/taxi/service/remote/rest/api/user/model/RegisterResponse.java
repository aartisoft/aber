package com.mammutgroup.taxi.service.remote.rest.api.user.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author mushtu
 * @since 5/24/16.
 */
public class RegisterResponse {

    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("expires_in")
    private Long expires ;
    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("scope")
    private String scope;
    @SerializedName("refresh_token")
    private String refreshToken;
    private String status;

}
