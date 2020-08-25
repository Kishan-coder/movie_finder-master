
package com.example.lenovo.retrofit_check;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tmdb implements Serializable {

    public  Tmdb(ArrayList<Result> results){this.results=results;}
    public Tmdb(Tmdb data){
        this.results=new ArrayList<>(data.results);
    }
    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    public List<Result> getResults() {
        return results;
    }
}
