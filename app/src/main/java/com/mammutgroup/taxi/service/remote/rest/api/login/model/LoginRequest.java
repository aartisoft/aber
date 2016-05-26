package com.mammutgroup.taxi.service.remote.rest.api.login.model;

/**
 * @author mushtu
 * @since 5/24/16.
 */
public class LoginRequest {

    private String username;
    private String password;

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
}
