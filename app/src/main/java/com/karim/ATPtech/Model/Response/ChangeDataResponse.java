package com.karim.ATPtech.Model.Response;

import com.google.gson.annotations.SerializedName;

public class ChangeDataResponse {

    @SerializedName("success")
    int success;

    @SerializedName("name")
    String name;

    @SerializedName("phone")
    String phone;

    @SerializedName("password")
    private String password;

    public String getPassword() {
        return password;
    }
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public ChangeDataResponse() {
    }

    public int getSuccess() {
        return success;
    }
}
