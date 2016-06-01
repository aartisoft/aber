package com.mammutgroup.taxi.service.remote.rest.api.order.model;

import com.google.gson.annotations.SerializedName;
import com.mammutgroup.taxi.service.remote.model.BaseDto;

/**
 * @author mushtu
 * @since 5/28/16.
 */
public class Order extends BaseDto {

    @SerializedName("sourceCoordinate[lat]")
    private Double sourceCoordinateLat;
    @SerializedName("sourceCoordinate[long]")
    private Double getSourceCoordinateLong;
    @SerializedName("destinationCoordinate[lat]")
    private Double destinationCoordinateLat;
    @SerializedName("destinationCoordinate[long]")
    private Double destinationCoordinateLong;
    @SerializedName("source")
    private String sourceAddress;
    @SerializedName("destination")
    private String destinationAddress;
    @SerializedName("estimatedTime")
    private Long estimatedTime;
    @SerializedName("distance")
    private Float distance;


    public Double getSourceCoordinateLat() {
        return sourceCoordinateLat;
    }

    public void setSourceCoordinateLat(Double sourceCoordinateLat) {
        this.sourceCoordinateLat = sourceCoordinateLat;
    }

    public Double getGetSourceCoordinateLong() {
        return getSourceCoordinateLong;
    }

    public void setSourceCoordinateLong(Double getSourceCoordinateLong) {
        this.getSourceCoordinateLong = getSourceCoordinateLong;
    }

    public Double getDestinationCoordinateLat() {
        return destinationCoordinateLat;
    }

    public void setDestinationCoordinateLat(Double destinationCoordinateLat) {
        this.destinationCoordinateLat = destinationCoordinateLat;
    }

    public Double getDestinationCoordinateLong() {
        return destinationCoordinateLong;
    }

    public void setDestinationCoordinateLong(Double destinationCoordinateLong) {
        this.destinationCoordinateLong = destinationCoordinateLong;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public Long getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Long estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }
}
