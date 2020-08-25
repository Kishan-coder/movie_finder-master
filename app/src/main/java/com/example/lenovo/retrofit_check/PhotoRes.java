package com.example.lenovo.retrofit_check;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhotoRes {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("profile_path")
    @Expose
    private String profilePath;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("title")
    @Expose
    private String title;

    public String getTitle(){
        return title;
    }

    public Integer getId() {
        return id;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getName() {
        return name;
    }

}