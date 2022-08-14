package com.example.myapplication;

public class User {
    private int userId;
    private String Email;
    private String FullName;
    private String MobileNumber;
    private String Token;

    public User() {
    }

    public User(int userId, String email, String fullName, String mobileNumber, String token) {
        this.userId = userId;
        Email = email;
        FullName = fullName;
        MobileNumber = mobileNumber;
        Token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}