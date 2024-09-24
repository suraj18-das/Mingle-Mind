package com.example.minglemind.model;

import com.google.firebase.Timestamp;

public class UserModel {
    private String phone;
    private String userName;
    private Timestamp CreatedtimeStamp;
    private String userId;
    private String fcmToken;
    public UserModel() {
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public UserModel(String userName, String phone, Timestamp createdtimeStamp, String userId) {
        this.userName = userName;
        this.phone = phone;
        CreatedtimeStamp = createdtimeStamp;
        this.userId=userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getCreatedtimeStamp() {
        return CreatedtimeStamp;
    }

    public void setCreatedtimeStamp(Timestamp createdtimeStamp) {
        CreatedtimeStamp = createdtimeStamp;
    }
}
