package com.karim.ATPtech.Model.Response;

import com.google.gson.annotations.SerializedName;

public class AcceptBidResponse {
    @SerializedName("success")
    int success;

    public int getSuccess() {
        return success;
    }
}
