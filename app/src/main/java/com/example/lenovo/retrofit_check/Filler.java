package com.example.lenovo.retrofit_check;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Pair;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.gson.JsonObject;
public class Filler extends AppCompatActivity {

    int counter;
    String name;
    private  Intent i;
    private String type="";
    private Tmdb data;
    ArrayList<Pair<String, String>> Data;
    private RecyclerView recyclerView;
    String arr[]={"%20Official%20Trailers&maxResults=8","%20Songs&maxResults=20"};
    String Valids[]={"Gaane Sune Ansune", "Vaaraahi", "Aditya", "T-", "Inc", "Sony", "Viacom","Tips", "Universal", "Venus", "Speed", "Zee", "Netflix", "Yash", "SVF", "Pen", "Shemaroo", "YRF",
     "Warner", "Lion", "Columbia", "Paramount", "Dream", "20", "Disney", "Marvel", "Summit", "Fox", "Reliance", "Salman", "PVR", "Movieclips"
    , "Pixar", "Red", "Dharma", "Yash", "Rajshri", "Eros", "UTV", "Balaji", "Aamir", "Nadia", "Suresh", "Viacom", "Ajay", "Phantom"};
    int count=0, I;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filler);

        Paper.init(Filler.this);

        recyclerView=findViewById(R.id.rv1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        i=getIntent();
        int check=i.getIntExtra("case", 0);
       Call<Tmdb> Tmdbcall=null;
        if(check==1) {
            type="now";
            Tmdbcall = API2.getService().getNowPlayn();
        }
        else if(check==2) {
            type="top";
            Tmdbcall = API2.getService().gettopRated();
        }
        else if(check==3) {
            type="coming";
            Tmdbcall = API2.getService().getReleasing();
        }
        if(check==1||check==2) {
            Tmdbcall.enqueue(new Callback<Tmdb>() {
                @Override
                public void onResponse(Call<Tmdb> call, Response<Tmdb> response) {
                    data = response.body();
                    String url2 = "http://api.themoviedb.org/3/movie/now_playing?api_key=6de800fc448d56225920d59e2cf378d2&";
                    RequestQueue queue = Volley.newRequestQueue(Filler.this);

                    final JsonObjectRequest JsonObjectRequest = new JsonObjectRequest

                            (Request.Method.GET, url2, null, new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        //to-Do
                                        JSONArray jsonArray = response.getJSONArray("results");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            data.getResults().add(new Result(jsonArray.getJSONObject(i).getString("title"),
                                                    jsonArray.getJSONObject(i).getString("poster_path"),
                                                    jsonArray.getJSONObject(i).getString("release_date"),
                                                    jsonArray.getJSONObject(i).getString("id")));
                                        }
                                        Collections.shuffle(data.getResults());
                                        recyclerView.setAdapter(new tmdbadapter(data, true, Filler.this));
                                        recyclerView.setNestedScrollingEnabled(false);
                                        if (!Paper.book().contains(type)) {
                                            Paper.book().write(type, data);
                                        } else {
                                            Tmdb tempdata = Paper.book().read(type);
                                            tempdata.getResults();
                                            if (!tempdata.getResults().get(0).getTitle().equals(data.getResults().get(0).getTitle())) {
                                                tempdata = new Tmdb(data);
                                                Paper.book().write(type, tempdata);
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO: Handle error
                                }
                            });
                    queue.add(JsonObjectRequest);
                }

                @Override
                public void onFailure(Call<Tmdb> call, Throwable t) {
                    Tmdb data = Paper.book().read(type);
                    if (data != null)
                        recyclerView.setAdapter(new tmdbadapter(data, false, Filler.this));
                    recyclerView.setNestedScrollingEnabled(false);
                }
            });
        }
        if(check==3){
            Tmdbcall.enqueue(new Callback<Tmdb>() {
                @Override
                public void onResponse(Call<Tmdb> call, Response<Tmdb> response) {
                    data = response.body();
                    String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                    String url2 = "http://api.themoviedb.org/3/discover/movie?api_key=6de800fc448d56225920d59e2cf378d2&" +
                            "with_original_language=hi"+ "&primary_release_date.gte=" + currentDate;
                    RequestQueue queue = Volley.newRequestQueue(Filler.this);

                    final JsonObjectRequest JsonObjectRequest = new JsonObjectRequest

                            (Request.Method.GET, url2, null, new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        //to-Do
                                        JSONArray jsonArray = response.getJSONArray("results");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            data.getResults().add(new Result(jsonArray.getJSONObject(i).getString("title"),
                                                    jsonArray.getJSONObject(i).getString("poster_path"),
                                                    jsonArray.getJSONObject(i).getString("release_date"),
                                                    jsonArray.getJSONObject(i).getString("id")));
                                        }
                                        Collections.shuffle(data.getResults());
                                        recyclerView.setAdapter(new tmdbadapter(data, true, Filler.this));
                                        recyclerView.setNestedScrollingEnabled(false);
                                        if (!Paper.book().contains(type)) {
                                            Paper.book().write(type, data);
                                        } else {
                                            Tmdb tempdata = Paper.book().read(type);
                                            tempdata.getResults();
                                            if (!tempdata.getResults().get(0).getTitle().equals(data.getResults().get(0).getTitle())) {
                                                tempdata = new Tmdb(data);
                                                Paper.book().write(type, tempdata);
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO: Handle error
                                }
                            });
                    queue.add(JsonObjectRequest);
                }

                @Override
                public void onFailure(Call<Tmdb> call, Throwable t) {
                    Tmdb data = Paper.book().read(type);
                    if (data != null)
                        recyclerView.setAdapter(new tmdbadapter(data, false, Filler.this));
                    recyclerView.setNestedScrollingEnabled(false);
                }
            });
        }
        if(check==4){
            data=new Tmdb(new ArrayList<Result>());
            final tmdbadapter heavy_duty=new tmdbadapter(data, true, Filler.this);
            recyclerView.setAdapter(heavy_duty);
            final ArrayList<Integer> tmdb=i.getIntegerArrayListExtra("recommendations");
            for(int i=0;i<tmdb.size();i++){
                Tmdbcall = API2.getService().getRecommendations(tmdb.get(i));
                Tmdbcall.enqueue(new Callback<Tmdb>() {
                    @Override
                    public void onResponse(Call<Tmdb> call, Response<Tmdb> response) {
                        List<Result> current=response.body().getResults();
                        if(current!=null&&!current.isEmpty()) {
                            for (int j = 0;j < 3&&j<current.size(); j++) {
                                heavy_duty.addItem(current.get(j), 0);
                            }
                        }
                        else{
                            Toast.makeText(Filler.this, "Sorry but we don't have enough recommendations!"
                                    ,Toast.LENGTH_SHORT).show();
                        }
                        recyclerView.smoothScrollToPosition(0);
                    }

                    @Override
                    public void onFailure(Call<Tmdb> call, Throwable t) {
                    }
                });
            }
        }
        else if(check == 5){
            int id= i.getIntExtra("id", 0);
            Tmdbcall = API2.getService().getRecommendations(id);
            Tmdbcall.enqueue(new Callback<Tmdb>() {
                @Override
                public void onResponse(Call<Tmdb> call, Response<Tmdb> response) {
                    data = response.body();
                    recyclerView.setAdapter(new tmdbadapter(data, true, Filler.this));
                    recyclerView.setNestedScrollingEnabled(false);
                }

                @Override
                public void onFailure(Call<Tmdb> call, Throwable t) {

                }
            });
        }
        else if(check ==6) {
            counter=0;
            Data = new ArrayList<>();
            int id = i.getIntExtra("id", 0);
            final String Country = i.getStringExtra("country");
            name = i.getStringExtra("movieName");
            if(name.split(":").length>1)
                name=name.split(":")[0];
            if (Country.contains("India")) {
                for(int i=0;i<2;i++) {
                    String url2 = "https://www.googleapis.com/youtube/v3/search?key=AIzaSyACsSmjsMz_s0nZ5t2WpM0aeDQB1IvH-Q0"
                            + "&q=" + name.replace(" ", "%20") +"%20movie"+ arr[i]+ "&part=snippet&chart=most%20popular&type=video";
                    RequestQueue queue = Volley.newRequestQueue(Filler.this);

                    final JsonObjectRequest JsonObjectRequest = new JsonObjectRequest

                            (Request.Method.GET, url2, null, new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        counter++;
                                        JSONArray videoItems =/*((JSONObject)((JSONObject)((JSONObject)*/((JSONArray) response.get("items"))/*.get(0))
                                                    .get("id")).get("videoId"))*/;
                                        for (int i = 0; i < videoItems.length(); i++) {
                                            JSONObject id = (JSONObject) ((JSONObject) videoItems.get(i)).get("id");
                                            JSONObject snippet = ((JSONObject) videoItems.get(i)).getJSONObject("snippet");
                                            String videoTitle = snippet.getString("title");
                                            videoTitle = videoTitle.replace("&#39;", "");
                                            videoTitle = videoTitle.replace("&amp;", "&");
                                            videoTitle = videoTitle.replace("&quot;", "*");
                                            /*if (videoTitle.split("\\|").length >= 2)
                                                videoTitle=(videoTitle.split("\\| ")[0] + "#" +
                                                        videoTitle.split("\\|")[1]);*/
                                            int c;
                                            for(c=0;c<Data.size();c++){
                                                if(Data.get(c).second.equals(videoTitle))
                                                    break;
                                            }
                                            if(c!=Data.size())
                                                continue;
                                            String channelTitle = snippet.getString("channelTitle");
                                            for (int j = 0; j < Valids.length; j++) {
                                                if (channelTitle.contains(Valids[j])
                                                        && id.getString("kind").equals("youtube#video") && videoTitle.toLowerCase().contains(name.toLowerCase().replace("!", ""))) {
                                                    Data.add(new Pair<>(id.getString("videoId"), videoTitle));
                                                }
                                            }
                                            if(Data.size()==0&&i==videoItems.length()-1){
                                                Data.add(new Pair<>(id.getString("videoId"), videoTitle));
                                            }
                                            else if(counter==2&&Data.size()==1&&i==videoItems.length()-1)
                                                Data.add(new Pair<>(id.getString("videoId"), videoTitle));
                                        }
                                        recyclerView.setAdapter(new video_adapter(Data, Filler.this, true));
                                        recyclerView.setNestedScrollingEnabled(false);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO: Handle error
                                }
                            });
                    queue.add(JsonObjectRequest);
                }

            } else {
                Tmdbcall = API2.getService().getVideos(id);
                Tmdbcall.enqueue(new Callback<Tmdb>() {
                    @Override
                    public void onResponse(Call<Tmdb> call, Response<Tmdb> response) {
                        if(response.body()!=null) {
                            for (int i = 0; i < response.body().getResults().size(); i++) {
                                Data.add(new Pair<>(response.body().getResults().get(i).getKey(),
                                        response.body().getResults().get(i).getName()));
                            }
                        }
                        recyclerView.setAdapter(new video_adapter(Data, Filler.this, true));
                        recyclerView.setNestedScrollingEnabled(false);
                        if(Data.size()<=2){
                            String url2 = "https://www.googleapis.com/youtube/v3/search?key=AIzaSyACsSmjsMz_s0nZ5t2WpM0aeDQB1IvH-Q0"
                                    + "&q=" + name.replace(" ", "%20") + "%20Movieclips"+ "&part=snippet&chart=most%20popular&type=video&maxResults=40";
                            RequestQueue queue = Volley.newRequestQueue(Filler.this);

                            final JsonObjectRequest JsonObjectRequest = new JsonObjectRequest

                                    (Request.Method.GET, url2, null, new com.android.volley.Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                JSONArray videoItems =/*((JSONObject)((JSONObject)((JSONObject)*/((JSONArray) response.get("items"))/*.get(0))
                                                    .get("id")).get("videoId"))*/;
                                                for (int i = 0; i < videoItems.length(); i++) {
                                                    JSONObject id = (JSONObject) ((JSONObject) videoItems.get(i)).get("id");
                                                    JSONObject snippet = ((JSONObject) videoItems.get(i)).getJSONObject("snippet");
                                                    String videoTitle = snippet.getString("title");
                                                    videoTitle = videoTitle.replace("&#39;", "");
                                                    videoTitle = videoTitle.replace("&amp;", "&");
                                                    videoTitle = videoTitle.replace("&quot;", "*");
                                                    int c;
                                                    for(c=0;c<Data.size();c++){
                                                        if(Data.get(c).second.equals(videoTitle))
                                                            break;
                                                    }
                                                    if(c!=Data.size())
                                                        continue;
                                                    String channelTitle = snippet.getString("channelTitle");
                                                    for (int j = 0; j < Valids.length; j++) {
                                                        if (channelTitle.contains(Valids[j])
                                                                && id.getString("kind").equals("youtube#video") && videoTitle.toLowerCase().contains(name.toLowerCase().replace("!", ""))) {
                                                            Data.add(new Pair<>(id.getString("videoId"), videoTitle));
                                                        }
                                                    }
                                                }
                                                recyclerView.setAdapter(new video_adapter(Data, Filler.this, true));
                                                recyclerView.setNestedScrollingEnabled(false);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new com.android.volley.Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // TODO: Handle error
                                        }
                                    });
                            queue.add(JsonObjectRequest);
                        }
                    }

                    @Override
                    public void onFailure(Call<Tmdb> call, Throwable t) {
                    }
                });
            }
        }
        else if(check==0){
            Intent intent=getIntent();
            Tmdb data=(Tmdb) intent.getSerializableExtra("discover");
            recyclerView.setAdapter(new tmdbadapter(data, true, Filler.this));
            recyclerView.setNestedScrollingEnabled(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            DownloadPop(requestCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final int pos=data.getIntExtra("result", 0);
            recyclerView.smoothScrollToPosition(pos);
            DownloadPop(pos);
//            Animation Bounce = AnimationUtils.loadAnimation(this, R.anim.bouce);
//            view.startAnimation(Bounce);

        }

    }
    public void DownloadPop(final int pos){
        new YouTubeExtractor(this) {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                for (int i = 0, itag; i < ytFiles.size(); i++) {
                    itag=ytFiles.keyAt(i);
                    final String url=ytFiles.get(itag).getUrl();
                    if(url!=null) {
                        new AlertDialog.Builder(Filler.this,  R. style.AlertDialogTheme)
                                .setTitle("Downloadable")
                                .setMessage("Wanna download this video?")
                                .setPositiveButton("Yup", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(ActivityCompat.checkSelfPermission(Filler.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(Filler.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(Filler.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                                            return;
                                        }
                                        LinkedHashMap<String, String>lmap;
                                        if(!Paper.book().contains("downloads")) {
                                            lmap=new LinkedHashMap<>();
                                            Paper.book().write("downloads", lmap);
                                        }
                                        lmap=Paper.book().read("downloads");
                                        if(lmap.containsKey(Data.get(pos).first)){
                                            Toast.makeText(Filler.this, "Already Downloaded!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        int x=0;
                                        if(Paper.book().contains("count"))
                                            x=Paper.book().read("count");
                                        x++;
                                        Paper.book().write("count", x);
                                        Uri uri = Uri.parse(url);
                                        DownloadManager.Request request = new DownloadManager.Request(uri);
                                        request.setTitle(Data.get(pos).second);

                                        request.allowScanningByMediaScanner();
                                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                        request.setDestinationInExternalPublicDir("/cineaste", "download"+Integer.toString(x));

                                        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                                        manager.enqueue(request);
                                        lmap.put(Data.get(pos).first, Data.get(pos).second+";"
                                                + Environment.getExternalStorageDirectory().toString()+"/cineaste/download"+x);
                                        Paper.book().write("downloads", lmap);
                                    }
                                })

                                .setNegativeButton("Nope", null)
                                .setIcon(R.drawable.download)
                                .show();
                        break;
                    }
                }
            }
        }.extract(Data.get(pos).first, true, true);
    }

}
