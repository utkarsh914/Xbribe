package com.xbribe.data.models;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("fcmToken")
    private String fcmToken;

    @SerializedName("token")
    private String token;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String fcmToken) {
        this.email = email;
        this.password = password;
        this.fcmToken = fcmToken;
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

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
