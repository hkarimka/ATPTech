package com.karim.ATPtech.Model.Request;

import com.google.gson.annotations.SerializedName;

public class AcceptBidRequest {
    @SerializedName("phone")
    String phone;

    @SerializedName("bid")
    int bid;

    @SerializedName("key")
    String key;

    public AcceptBidRequest(String phone, int bid, String key) {
        this.phone = phone;
        this.bid = bid;
        this.key = key;
    }
}
