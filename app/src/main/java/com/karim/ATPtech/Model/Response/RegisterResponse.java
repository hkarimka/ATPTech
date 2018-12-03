package com.karim.ATPtech.Model.Response;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {
    @SerializedName("success")
    private int success;

    @SerializedName("message")
    private String message;

    public int getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public RegisterResponse() {}
}
