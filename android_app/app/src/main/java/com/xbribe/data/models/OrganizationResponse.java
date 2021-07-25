package com.xbribe.data.models;

import com.google.gson.annotations.SerializedName;

public class OrganizationResponse {

    @SerializedName("error")
    private String error;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Organizations[] data;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Organizations[] getData() {
        return data;
    }

    public void setData(Organizations[] data) {
        this.data = data;
    }
}
