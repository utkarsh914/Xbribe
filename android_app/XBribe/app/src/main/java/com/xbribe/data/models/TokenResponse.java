package com.xbribe.data.models;

import com.google.gson.annotations.SerializedName;

public class TokenResponse {

    @SerializedName("createdAt")
    private String Date;

    @SerializedName("_id")
    private String ID;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("_v")
    private Integer value;

    @SerializedName("token")
    private String token;

    public TokenResponse(String token) {
        this.token = token;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
