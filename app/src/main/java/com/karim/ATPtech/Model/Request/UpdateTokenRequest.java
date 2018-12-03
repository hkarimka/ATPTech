package com.karim.ATPtech.Model.Request;

import com.google.gson.annotations.SerializedName;

public class UpdateTokenRequest {
    @SerializedName("token")
    String token;

    @SerializedName("login")
    String login;

    public UpdateTokenRequest(String token, String login) {
        this.token = token;
        this.login = login;
    }
}
