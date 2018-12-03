package com.karim.ATPtech.Model.Response;

import com.google.gson.annotations.SerializedName;

public class AddTechResponse {
    @SerializedName("success")
    int success;

    @SerializedName("message")
    String message;

    public int getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public AddTechResponse() {
    }
}
