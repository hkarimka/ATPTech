package com.karim.ATPtech.Model.Response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetTypes {
    @SerializedName("success")
    int success;

    @SerializedName("types")
    ArrayList<String> types;

    public int getSuccess() {
        return success;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public GetTypes() {
    }
}
