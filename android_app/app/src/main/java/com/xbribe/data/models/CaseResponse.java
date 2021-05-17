package com.xbribe.data.models;

import com.google.gson.annotations.SerializedName;

public class CaseResponse {

    @SerializedName("error")
    private String error;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private CaseData data;

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

    public CaseData getData() {
        return data;
    }

    public void setData(CaseData data) {
        this.data = data;
    }
}
