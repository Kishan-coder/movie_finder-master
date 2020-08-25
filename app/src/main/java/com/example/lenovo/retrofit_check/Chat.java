package com.example.lenovo.retrofit_check;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Chat extends AppCompatActivity {

    String with_people="", with_genres="",
            date1="", date2="";
    private EditText user_message;
    private Button send;
    private RecyclerView recyclerView;
    HashMap<String, Integer> map;
    private Tmdb data;
    String noMonth=null;
    //NO VOLLEY CALL.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Paper.init(this);
        noMonth="jan january feb mar march april apr may june jun jul july aug august sept october oct november dec december";
        if(!Paper.book().contains("genres")){
            map=new HashMap<>();
            map.put("action", 28);map.put("adventure", 12);map.put("horror", 27);map.put("romantic", 10749); map.put("thriller", 53);
            map.put("fight", 28); map.put("fighting", 28);map.put("war", 10752);
            map.put("comedy", 35); map.put("drama", 18);map.put("animated", 16);map.put("fantasy",14);map.put("documentary",99);
            map.put("fiction", 878);map.put("science fiction", 878);map.put("funny", 35); map.put("crime", 80); map.put("gore", 27);
            map.put("nerve wrecking", 27); map.put("musical", 10402);map.put("music", 10402); map.put("light", 10751);map.put("family", 10751);
            Paper.book().write("genres", map);
        }
        else
            map=Paper.book().read("genres");
        user_message=findViewById(R.id.textInput);
        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0F);
        send=findViewById(R.id.btnSend);
        recyclerView=findViewById(R.id.rv1);
        recyclerView.setLayoutManager(new LinearLayoutManager(Chat.this));
        final chatAdapter myadapter=new chatAdapter(Chat.this);
        recyclerView.setAdapter(myadapter);
        myadapter.addItem("Hi There. I'm Cineaste, I am built to answer all your movie queries. Just ask me to test..", false, false, null);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                myadapter.addItem(user_message.getText().toString(), true, false, null);
                with_people = "";
                with_genres = "";
                date1 = "";
                date2 = "";
                String people = Paper.book().read("people"), current;
                String[] user_space=user_message.getText().toString().split("\\s");
                int _i, _j, k;
                for(int i=0;i<user_space.length;i++){
                    current=user_space[i].toLowerCase();
                    if(map.containsKey(current.toLowerCase())){
                        with_genres+=map.get(current.toLowerCase());
                    }
                    else if((!noMonth.contains(current))&&people.contains(" "+current+":")||people.contains(" "+current+" ")){
                        for(int j=i+1;j<user_space.length;j++){
                            if(people.contains(" "+user_space[j].toLowerCase()+":")||
                                    people.contains(" "+user_space[j].toLowerCase()+" "))
                                current += " " + user_space[j].toLowerCase();
                            else{
                                if(map.containsKey(user_space[j]))
                                    with_genres+=map.get(user_space[j].toLowerCase());
                                i=j+1;
                                break;
                            }
                        }
                        _i=people.indexOf(current+":");
                        _j = people.indexOf(":", _i + 1);
                        k = people.indexOf(",", _j + 1);
                        with_people += people.substring(_j + 1, k) + ",";
                    }
                }

                Parser parser = new Parser();
                List groups = parser.parse(user_message.getText().toString());
                if (!groups.isEmpty() && !((DateGroup)(groups.get(0))).getDates().isEmpty()) {
                    date1=new SimpleDateFormat("yyyy-MM-dd").format(((DateGroup)(groups.get(0))).getDates().get(0));
                    if(((DateGroup)(groups.get(0))).getDates().size()!=1)
                        date2=new SimpleDateFormat("yyyy-MM-dd").format(((DateGroup)(groups.get(0))).getDates().get(1));
                }
                if(date2.equals("")&&date1.equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))){
                    if(user_message.getText().toString().toLowerCase().contains("year")) {
                        date1 = date1.substring(0, 4) + "-01-01";
                        date2 = date1.substring(0, 4) + "-12-30";
                    }
                    else {
                        date1 = date1.substring(0, 7) + "-01";
                        date2 = date1.substring(0, 7) + "-30";
                    }
                }
                else if(date2.equals("")&&!date1.equals("")){
                    if(date1.substring(4).equals(new SimpleDateFormat("MM-dd").format(new Date()))){
                        date1 = date1.substring(0, 4) + "-01-01";
                        date2 = date1.substring(0, 4) + "-12-30";
                    }
                    else{
                        date1 = date1.substring(0, 7) + "-01";
                        date2 = date1.substring(0, 7) + "-30";
                    }
                }
                if(date1.compareTo(date2)>0) {
                    String temp = date1;
                    date1=date2;
                    date2=temp;
                }
                if (!(with_people.equals("") && with_genres.equals("") && date1.equals("") && date2.equals(""))) {
                    Call<Tmdb> Tmdbcall = null;
                    if (user_message.getText().toString().contains("starrer") ||
                            user_message.getText().toString().contains("starring"))
                        Tmdbcall = API2.getService().starrer(with_people, date1, date2, with_genres);
                    else
                        Tmdbcall = API2.getService().discover(with_people, date1, date2, with_genres);
                    user_message.setText("");
                    Tmdbcall.enqueue(new Callback<Tmdb>() {
                        @Override
                        public void onResponse(Call<Tmdb> call, retrofit2.Response<Tmdb> response) {
                            data = response.body();
//                            if (with_people.equals("") && entities.size() == 0) {
//                                String url2 = "http://api.themoviedb.org/3/discover/movie?api_key=6de800fc448d56225920d59e2cf378d2&" +
//                                        "with_original_language=hi&with_people=" + with_people + "&with_genres=" + with_genres + "&primary_release_date.gte=" + date1
//                                        + "&primary_release_date.lte=" + date2;
//                                RequestQueue queue = Volley.newRequestQueue(Chat.this);
//
//                                final JsonObjectRequest JsonObjectRequest = new JsonObjectRequest
//
//                                        (Request.Method.GET, url2, null, new com.android.volley.Response.Listener<JSONObject>() {
//                                            @Override
//                                            public void onResponse(JSONObject response) {
//                                                try {
//                                                    //to-Do
//                                                    String bubbleText = "";
//                                                    JSONArray jsonArray = response.getJSONArray("results");
//                                                    for (int i = 0; i < jsonArray.length(); i++) {
//                                                        data.getResults().add(new Result(jsonArray.getJSONObject(i).getString("title"),
//                                                                jsonArray.getJSONObject(i).getString("poster_path"),
//                                                                jsonArray.getJSONObject(i).getString("release_date"),
//                                                                jsonArray.getJSONObject(i).getString("id")));
//                                                    }
//                                                    Collections.shuffle(data.getResults());
//                                                    for (int i = 0; i < 3; i++) {
//                                                        bubbleText += (i + 1) + ". " + data.getResults().get(i).getTitle() + "\n";
//                                                    }
//                                                    myadapter.addItem(bubbleText.substring(0, bubbleText.length() - 1), false, true, data);
//
//                                                } catch (JSONException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//                                        }, new com.android.volley.Response.ErrorListener() {
//                                            @Override
//                                            public void onErrorResponse(VolleyError error) {
//                                                // TODO: Handle error
//                                            }
//                                        });
//                                queue.add(JsonObjectRequest);
//                            } else {
                                String bubbleText = "Top movie(s) matching your query are:\n";
                                for (int i = 0; i < 3; i++) {
                                    if (i == data.getResults().size())
                                        break;
                                    bubbleText += (i + 1) + ". " + data.getResults().get(i).getTitle() + "\n";
                                }
                                if (bubbleText.equals("Top movie(s) we found are:\n"))
                                    myadapter.addItem("They were together in no movies.", false, false, null);
                                myadapter.addItem(bubbleText.substring(0, bubbleText.length() - 1), false, true, data);
                        }

                        @Override
                        public void onFailure(Call<Tmdb> call, Throwable t) {
                        }
                    });
                } else {
                    String bubbleText = "Sorry but Nothing was found :(" + "\nBeware of the spellling mistakes if any!";
                    myadapter.addItem(bubbleText, false, false, data);
                }
                /*

                if (!(with_people.equals("") && with_genres.equals("") && date1.equals("") && date2.equals(""))) {
                    Call<Tmdb> Tmdbcall = null;
                    if (user_message.getText().toString().contains("starrer") ||
                            user_message.getText().toString().contains("starring"))
                        Tmdbcall = API2.getService().starrer(with_people, date1, date2, with_genres);
                    else
                        Tmdbcall = API2.getService().discover(with_people, date1, date2, with_genres);
                    user_message.setText("");
                    Tmdbcall.enqueue(new Callback<Tmdb>() {
                        @Override
                        public void onResponse(Call<Tmdb> call, retrofit2.Response<Tmdb> response) {
                            data = response.body();
                            if (with_people.equals("") && entities.size() == 0) {
                                String url2 = "http://api.themoviedb.org/3/discover/movie?api_key=6de800fc448d56225920d59e2cf378d2&" +
                                        "with_original_language=hi&with_people=" + with_people + "&with_genres=" + with_genres + "&primary_release_date.gte=" + date1
                                        + "&primary_release_date.lte=" + date2;
                                RequestQueue queue = Volley.newRequestQueue(Chat.this);

                                final JsonObjectRequest JsonObjectRequest = new JsonObjectRequest

                                        (Request.Method.GET, url2, null, new com.android.volley.Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    //to-Do
                                                    String bubbleText = "";
                                                    JSONArray jsonArray = response.getJSONArray("results");
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        data.getResults().add(new Result(jsonArray.getJSONObject(i).getString("title"),
                                                                jsonArray.getJSONObject(i).getString("poster_path"),
                                                                jsonArray.getJSONObject(i).getString("release_date"),
                                                                jsonArray.getJSONObject(i).getString("id")));
                                                    }
                                                    Collections.shuffle(data.getResults());
                                                    for (int i = 0; i < 3; i++) {
                                                        bubbleText += (i + 1) + ". " + data.getResults().get(i).getTitle() + "\n";
                                                    }
                                                    myadapter.addItem(bubbleText.substring(0, bubbleText.length() - 1), false, true, data);

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
                            } else {
                                String bubbleText = "Top movie(s) we found are:\n";
                                for (int i = 0; i < 3; i++) {
                                    if (i == data.getResults().size())
                                        break;
                                    bubbleText += (i + 1) + ". " + data.getResults().get(i).getTitle() + "\n";
                                }
                                if (bubbleText.equals("Top movie(s) we found are:\n"))
                                    myadapter.addItem("They were together in no movies.", false, false, null);
                                myadapter.addItem(bubbleText.substring(0, bubbleText.length() - 1), false, true, data);
                            }
                        }

                        @Override
                        public void onFailure(Call<Tmdb> call, Throwable t) {
                        }
                    });
                } else {
                    String bubbleText = "Sorry but Nothing was found :(" + "\nBeware of the spellling mistakes if any!";
                    myadapter.addItem(bubbleText, false, false, data);
                }*/
            }
//                                    if(response.getResult().getOutput().getIntents().get(0).toString().equalsIgnoreCase("upcoming")){
//
//                                    }


               /* if(check) {
                    myadapter.addItem(user_message.getText().toString(), check);
                    check=false;
                }
                else {
                    myadapter.addItem(user_message.getText().toString(), check);
                    check=true;
                }*/

                    });
                    }
                    }
