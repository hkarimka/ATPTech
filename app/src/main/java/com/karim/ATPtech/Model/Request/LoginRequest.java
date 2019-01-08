package com.karim.ATPtech.Model.Request;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("login")
    private String login;

    @SerializedName("password")
    private String password;

    @SerializedName("token")
    private String token;

    @SerializedName("version_app")
    int version;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public LoginRequest(){}


    public LoginRequest(String login, String password, String token, int version){
        this.login = login;
        this.password = password;
        this.token = token;
        this.version = version;
    }
}