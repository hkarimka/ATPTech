package com.karim.ATPtech.Model.Response;

import com.google.gson.annotations.SerializedName;

public class DeleteTechResponse {
    @SerializedName("success")
    int success;

    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    public int getSuccess() {
        return success;
    }
}
