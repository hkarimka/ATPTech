package com.karim.ATPtech.Model.Request;

import com.google.gson.annotations.SerializedName;

public class GetSubtypesRequest {
    @SerializedName("type")
    String type;

    public GetSubtypesRequest(String type) {
        this.type = type;
    }
}
