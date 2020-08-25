package com.example.lenovo.retrofit_check;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bio {

    @SerializedName("biography")
    @Expose
    private String biography;

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
    @SerializedName("known_for_department")
    @Expose
    private String k;

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }
    @SerializedName("gender")
    @Expose
    private int gender;

    public int getGender() {
        return gender;
    }

    public void setGender(int g) {
        this.gender = g;
    }
    @SerializedName("birthday")
    @Expose
    private String b;

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }
}