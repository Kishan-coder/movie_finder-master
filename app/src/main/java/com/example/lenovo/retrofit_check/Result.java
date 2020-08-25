
package com.example.lenovo.retrofit_check;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result implements Serializable {

    public  Result(String title, String posterPath, String releaseDate){
        this.title=title;
        this.posterPath=posterPath;
        this.releaseDate=releaseDate;
    }
    public  Result(String title, String posterPath, String releaseDate, String id){
        this.title=title;
        this.posterPath=posterPath;
        this.releaseDate=releaseDate;
        this.id=id;
    }
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @SerializedName("key")
    @Expose
    private String key;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("id")
    @Expose
    private String id;


    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }
    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }


}
