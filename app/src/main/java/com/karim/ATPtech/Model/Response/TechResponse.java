package com.karim.ATPtech.Model.Response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TechResponse {
    @SerializedName("success")
    int success;

    @SerializedName("typeandmod")
    ArrayList<String> typeandmod;

    @SerializedName("property")
    ArrayList<String> property;

    @SerializedName("techs")
    ArrayList<Integer> techs;

    public ArrayList<Integer> getTechs() {
        return techs;
    }

    public int getSuccess() {
        return success;
    }

    public ArrayList<String> getTypeandmod() {
        return typeandmod;
    }

    public ArrayList<String> getProperty() {
        return property;
    }

    public TechResponse() {
    }
}
