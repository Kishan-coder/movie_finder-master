
package com.example.lenovo.retrofit_check;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class plot implements Serializable{

    @SerializedName("Title")
    @Expose
    public String title;
    @SerializedName("Year")
    @Expose
    public String year;
    @SerializedName("Rated")
    @Expose
    public String rated;
    @SerializedName("Released")
    @Expose
    public String released;
    @SerializedName("Runtime")
    @Expose
    public String runtime;
    @SerializedName("Genre")
    @Expose
    public String genre;
    @SerializedName("Director")
    @Expose
    public String director;
    @SerializedName("Writer")
    @Expose
    public String writer;
    @SerializedName("Actors")
    @Expose
    public String actors;
    @SerializedName("Plot")
    @Expose
    public String plot;
    @SerializedName("Country")
    @Expose
    public String country;
    @SerializedName("Awards")
    @Expose
    public String awards;
    @SerializedName("Poster")
    @Expose
    public String poster;
    @SerializedName("Ratings")
    @Expose
    public List<Rating> ratings = null;
    @SerializedName("imdbRating")
    @Expose
    public String imdbRating;
    @SerializedName("Type")
    @Expose
    public String type;
    @SerializedName("BoxOffice")
    @Expose
    public String boxOffice;
    @SerializedName("Production")
    @Expose
    public String production;

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getRated() {
        return rated;
    }

    public String getReleased() {
        return released;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public String getWriter() {
        return writer;
    }

    public String getActors() {
        return actors;
    }

    public String getPlot() {
        return plot;
    }

    public String getCountry() {
        return country;
    }

    public String getAwards() {
        return awards;
    }

    public String getPoster() {
        return poster;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public String getType() {
        return type;
    }

    public String getBoxOffice() {
        return boxOffice;
    }

    public String getProduction() {
        return production;
    }

}
