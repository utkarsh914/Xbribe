package com.xbribe.data.models;

import com.google.gson.annotations.SerializedName;

public class NearbyCaseResponse
{
    @SerializedName("location")
    private LocationDetails location;

    @SerializedName("place")
    private String city;

    @SerializedName("name")
    private String organizationName;

    public NearbyCaseResponse(LocationDetails location, String city, String organizationName) {
        this.location = location;
        this.city = city;
        this.organizationName = organizationName;
    }

    public LocationDetails getLocation() {
        return location;
    }

    public void setLocation(LocationDetails location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
