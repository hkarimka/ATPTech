package com.karim.ATPtech.Model.Request;

import com.google.gson.annotations.SerializedName;

public class TechRequest {

    @SerializedName("phone")
    String phone;

    public TechRequest(String phone) {
        this.phone = phone;
    }
}
