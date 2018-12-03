package com.karim.ATPtech.Model.Request;

import com.google.gson.annotations.SerializedName;

public class DeleteTechRequest {
    @SerializedName("phone")
    String phone;

    @SerializedName("tech")
    Integer tech;

    public DeleteTechRequest(String phone, Integer tech) {
        this.phone = phone;
        this.tech = tech;
    }
}
