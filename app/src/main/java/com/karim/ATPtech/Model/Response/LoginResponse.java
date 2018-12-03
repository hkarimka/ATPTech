package com.karim.ATPtech.Model.Response;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("success")
    private int success;

    @SerializedName("message")
    private String message;

    @SerializedName("name")
    private String name;

    @SerializedName("isActivated")
    private String isActivated;

    public LoginResponse() {}

    public int getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public String getIsActivated() {
        return isActivated;
    }
}