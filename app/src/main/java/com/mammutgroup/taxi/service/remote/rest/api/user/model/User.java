package com.mammutgroup.taxi.service.remote.rest.api.user.model;

import com.google.gson.annotations.SerializedName;
import com.mammutgroup.taxi.service.remote.model.BaseDto;
import com.mammutgroup.taxi.service.remote.model.MobileNumber;

/**
 * @author mushtu
 * @since 5/24/16.
 */
public class User extends BaseDto {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String fullName;
    private String gender;
    private boolean hasNumber;
    @SerializedName(value = "number",alternate = {"numberInfo"})
    private MobileNumber number;
    private String type;
    private String email;
    private ProfilePicture profilePicture;

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isHasNumber() {
        return hasNumber;
    }

    public void setHasNumber(boolean hasNumber) {
        this.hasNumber = hasNumber;
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

    public ProfilePicture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(ProfilePicture profilePicture) {
        this.profilePicture = profilePicture;
    }
}
