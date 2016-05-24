package com.mammutgroup.taxi.service.remote.rest.api.user.model;

import com.mammutgroup.taxi.service.remote.model.BaseDto;

/**
 * @author mushtu
 * @since 5/24/16.
 */
public class ProfilePicture extends BaseDto {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
