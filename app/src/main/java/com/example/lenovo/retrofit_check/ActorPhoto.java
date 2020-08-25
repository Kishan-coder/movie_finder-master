package com.example.lenovo.retrofit_check;

import com.example.lenovo.retrofit_check.PhotoRes;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActorPhoto {

    @SerializedName("results")
    @Expose
    private List<PhotoRes> results = null;
    public List<PhotoRes> getResults() {
        return results;
    }

}