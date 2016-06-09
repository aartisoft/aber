package com.mammutgroup.taxi.service.remote.rest.api.order.model;

import com.google.gson.annotations.SerializedName;
import com.mammutgroup.taxi.service.remote.model.BaseDto;

/**
 * Created by Iraj on 6/9/2016.
 */
public class PriceResponse extends BaseDto {
    @SerializedName("minOrderPrice")
    private Double minPrice;
    @SerializedName("maxOrderPrice")
    private Double maxPrice;

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }
}
