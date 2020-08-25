package com.example.lenovo.retrofit_check;

import androidx.annotation.CallSuper;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class myAPI {
    private static final String url="http://www.omdbapi.com";
    private static final String key="ca4eec27";
    private static  String remaining="";
    public static getData Getdata=null;


    public static getData getService(){
        if(Getdata==null){
            Retrofit retrofit= new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
            Getdata=retrofit.create(getData.class);
        }
        return Getdata;
    }
    public interface getData{

        @GET("?&apikey="+key)
        Call<Pojoex> getPojoex(@Query("s") String ans);

        @GET("?&apikey="+key+"&plot=full")
        Call<plot> getplot(@Query("t") String ans);

        @GET("?&apikey="+key+"&plot=full")
        Call<plot> getPlot(@Query("i") String ans);
    }
}

