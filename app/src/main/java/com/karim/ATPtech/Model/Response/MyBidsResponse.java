package com.karim.ATPtech.Model.Response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MyBidsResponse {
    @SerializedName("success")
    int success;

    @SerializedName("typeandmod")
    ArrayList<String> typeandmod;

    @SerializedName("text")
    ArrayList<String> text;

    @SerializedName("bids")
    ArrayList<Integer> id;

    @SerializedName("isWinner")
    ArrayList<Integer> isWinner;

    @SerializedName("status")
    ArrayList<Integer> status;

    @SerializedName("op_phone")
    ArrayList<String> op_phone;

    public ArrayList<Integer> getStatus() {
        return status;
    }

    public ArrayList<String> getOp_phone() {
        return op_phone;
    }

    public MyBidsResponse() {
    }

    public int getSuccess() {
        return success;
    }

    public ArrayList<String> getTypeandmod() {
        return typeandmod;
    }

    public ArrayList<String> getText() {
        return text;
    }

    public ArrayList<Integer> getId() {
        return id;
    }

    public ArrayList<Integer> getIsWinner() {
        return isWinner;
    }
}
