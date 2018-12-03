package com.karim.ATPtech.Model.Response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetSubtypesResponse {
    @SerializedName("success")
    int success;

    @SerializedName("subtypes")
    ArrayList<String> subtypes;

    public int getSuccess() {
        return success;
    }

    public ArrayList<String> getSubtypes() {
        return subtypes;
    }

    public GetSubtypesResponse() {
    }
}
