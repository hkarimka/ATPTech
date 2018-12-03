package com.karim.ATPtech.Model.Request;

import com.google.gson.annotations.SerializedName;

public class RemoveTokenRequest {

    @SerializedName("login")
    String login;

    public RemoveTokenRequest(String login) {
        this.login = login;
    }
}
