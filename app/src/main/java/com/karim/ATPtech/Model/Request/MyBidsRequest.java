package com.karim.ATPtech.Model.Request;

import com.google.gson.annotations.SerializedName;

public class MyBidsRequest {
    @SerializedName("phone")
    String phone;

    public MyBidsRequest(String phone) {
        this.phone = phone;
    }
}
