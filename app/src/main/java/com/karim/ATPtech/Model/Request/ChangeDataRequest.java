package com.karim.ATPtech.Model.Request;

import com.google.gson.annotations.SerializedName;

public class ChangeDataRequest {

    @SerializedName("key")
    int key;

    @SerializedName("login")
    String login;

    @SerializedName("value")
    String value;

    public ChangeDataRequest(int key, String value, String login) {
        this.key = key;
        this.value = value;
        this.login = login;
    }
}
