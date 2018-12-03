package com.karim.ATPtech.Model.Request;

import com.google.gson.annotations.SerializedName;

public class AddTechRequest {

    @SerializedName("phone")
    String phone;

    @SerializedName("type")
    String type;

    @SerializedName("modif")
    String modif;

    @SerializedName("numb")
    String numb;

    @SerializedName("property")
    String property;

    public AddTechRequest(String phone, String type, String modif, String property, String numb) {
        this.phone = phone;
        this.type = type;
        this.modif = modif;
        this.property = property;
        this.numb = numb;
    }
}
