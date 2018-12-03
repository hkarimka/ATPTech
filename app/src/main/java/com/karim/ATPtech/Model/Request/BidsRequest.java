package com.karim.ATPtech.Model.Request;

import com.google.gson.annotations.SerializedName;

public class BidsRequest {
    @SerializedName("phone")
    String phone;

    public BidsRequest(String phone) {
        this.phone = phone;
    }
}
