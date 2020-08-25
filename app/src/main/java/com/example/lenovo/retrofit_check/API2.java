package com.example.lenovo.retrofit_check;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Lenovo on 1/25/2019.
 */

public class API2 {
    private static final String url="http://api.themoviedb.org/3/";
    public static API2.getData Getdata=null;
    private static final String key="6de800fc448d56225920d59e2cf378d2";

    public static API2.getData getService(){
        if(Getdata==null){
            Retrofit retrofit= new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
            Getdata=retrofit.create(getData.class);
        }
        return Getdata;
    }
    public interface getData{

        @GET("trending/movie/week?api_key="+key)
        Call<Tmdb> getPop();

        @GET("movie/now_playing?api_key="+key+"&page=1&region=IN")
        Call<Tmdb> getNowPlayn();

        @GET("movie/top_rated?api_key="+key+"&page=1&region=IN")
        Call<Tmdb> gettopRated();

        @GET("movie/upcoming?api_key="+key+"&page=1&region=US")
        Call<Tmdb> getReleasing();

        @GET("search/person?api_key="+key)
        Call<ActorPhoto> getActor(@Query("query") String ans);

        @GET("person/{id}?api_key="+key)
        Call<Bio> getBio(@Path(value = "id", encoded = true) String userId);

        @GET("search/movie?api_key="+key)
        Call<ActorPhoto> getMovieId(@Query("query") String ans);

        @GET("discover/movie?api_key="+key)
        Call<Tmdb> discover(@Query("with_people") String actor, @Query("primary_release_date.gte") String date1,
                                  @Query("primary_release_date.lte") String date2, @Query("with_genres") String genres);
        @GET("discover/movie?api_key="+key)
        Call<Tmdb> starrer(@Query("with_cast") String actor, @Query("primary_release_date.gte") String date1,
                            @Query("primary_release_date.lte") String date2, @Query("with_genres") String genres);

        @GET("search/TV?api_key="+key)
        Call<ActorPhoto> getTVId(@Query("query") String ans);

        @GET("movie/{id}/recommendations?api_key="+key)
        Call<Tmdb> getRecommendations(@Path(value = "id", encoded = true) Integer tmdbId);

        @GET("movie/{id}/videos?api_key="+key)
        Call<Tmdb> getVideos(@Path(value = "id", encoded = true) Integer tmdbId);

        @GET("movie/popular?api_key="+key)
        Call<Tmdb> getRand(@Query("page") String ans);
        @GET("movie/popular?api_key="+key+"&with_original_language=hi")
        Call<Tmdb> getRandIN(@Query("page") String ans);
    }
}
