package com.mammutgroup.taxi.service.remote.rest.api.user.model;

import com.google.gson.annotations.SerializedName;
import com.mammutgroup.taxi.service.remote.model.MobileNumber;

/**
 * @author mushtu
 * @since 5/24/16.
 */
public class RegisterRequest {

    public static final String TYPE_PASSENGER = "Passenger";
    public static final String TYPE_DRIVER = "Driver";
    public static final String TYPE_OPERATOR = "Operator";


    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String gender;
    @SerializedName("client_id")
    private String clientId;
    @SerializedName("client_secret")
    private String clientSecret;
    @SerializedName("grant_type")
    private String grantType;
    private MobileNumber number;
    private String type;
    private String email;

    public RegisterRequest()
    {
        this.type = TYPE_PASSENGER;
        this.clientId = "test";
        this.clientSecret = "test";
        this.grantType = "password";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

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

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public MobileNumber getNumber() {
        return number;
    }

    public void setNumber(MobileNumber number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
