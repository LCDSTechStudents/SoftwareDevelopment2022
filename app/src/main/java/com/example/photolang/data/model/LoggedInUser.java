package com.example.photolang.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private Integer ID;
    private String displayName;
    private String token;
    public LoggedInUser(int userId, String displayName, String token) {
        this.ID = userId;
        this.displayName = displayName;
        this.token = token;
    }

    public Integer getUserId() {
        return ID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}