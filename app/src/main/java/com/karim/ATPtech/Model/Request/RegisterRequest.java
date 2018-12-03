package com.karim.ATPtech.Model.Request;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {

    @SerializedName("login")
    private String login;

    @SerializedName("password")
    private String password;

    @SerializedName("name")
    private String name;

    public RegisterRequest() {
    }

    public RegisterRequest(String login, String password, String name) {
        this.login = login;
        this.password = password;
        this.name = name;
    }
}
