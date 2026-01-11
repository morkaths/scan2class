package com.morkath.scan2class.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SessionDto {

    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @Size(max = 50, message = "Room must be less than 50 characters")
    private String room;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Max(value = 180, message = "Duration cannot exceed 180 minutes")
    private Integer duration = 15; // default 15 mins

    @NotNull(message = "Radius is required")
    @Min(value = 10, message = "Radius must be at least 10 meters")
    private Double radius = 50.0; // default 50m

    private Double latitude;

    private Double longitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
