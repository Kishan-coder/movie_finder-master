package com.example.lenovo.retrofit_check;

import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Recommend extends AppCompatActivity {

    private ArrayList<Integer> tmdbId;
    private  Pojoex data;
    private EditText search;
    private Button add;
    private Button btm;
    private RecyclerView rv;
    private TextView page_title;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0F);
    Boolean flag=false;
    myadapter Myadapter;
    private int count=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        tmdbId=new ArrayList<>();
        btm=findViewById(R.id.btm);
        search=findViewById(R.id.text);
        add=findViewById(R.id.button);
        page_title=findViewById(R.id.r);
        rv=findViewById(R.id.rv1);

        final ArrayList<String> searches=new ArrayList<>();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                if(searches.contains(search.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Already Added!", Toast.LENGTH_SHORT).show();
                    return;
                }
                searches.add(search.getText().toString());
                final Call<Pojoex> pojoexCall=myAPI.getService().getPojoex(search.getText().toString());
                pojoexCall.enqueue(new Callback<Pojoex>() {
                    @Override
                    public void onResponse(Call<Pojoex> call, Response<Pojoex> response) {
                            data=response.body();
                            if(data.getSearch()==null)
                                Toast.makeText(getApplicationContext(), "Movie NOT FOUND!", Toast.LENGTH_SHORT).show();
                             else if(!flag) {
                                 Search one=null;
                                flag=true;
                                int i;
                                for(i=0;i<data.getSearch().size();i++){
                                    if(data.getSearch().get(i).getTitle().equalsIgnoreCase(search.getText().toString())) {
                                        one=data.getSearch().get(i);
                                        break;
                                    }
                                }
                                if(i==data.getSearch().size()) {
                                     one = data.getSearch().get(0);
                                }
                                data.getSearch().clear();
                                data.getSearch().add(one);
                                Myadapter=new myadapter(data, true, Recommend.this);
                                rv.setLayoutManager(new LinearLayoutManager(Recommend.this));
                                rv.getLayoutParams().height=1610;
                                rv.setAdapter(Myadapter);
                            }
                            else{
                                count++;
                                if(count==3)
                                    btm.setText("Recommed Me!");
                                int i;
                                for(i=0;i<data.getSearch().size();i++){
                                    if(data.getSearch().get(i).getTitle().equalsIgnoreCase(search.getText().toString())) {
                                        Myadapter.addItem(data.getSearch().get(i), 0);
                                        break;
                                    }
                                }
                                if(i==data.getSearch().size()) {
                                    Myadapter.addItem(data.getSearch().get(0), 0);
                                }
                                rv.smoothScrollToPosition(0);
                            }
                            if(data.getSearch()!=null){
                                final Call<ActorPhoto> ForIdCall=API2.getService().getMovieId(searches.get(searches.size()-1));
                                ForIdCall.enqueue(new Callback<ActorPhoto>() {
                                    @Override
                                    public void onResponse(Call<ActorPhoto> call, Response<ActorPhoto> response) {
                                        if(response.body().getResults()!=null&&!response.body().getResults().isEmpty()) {
                                            int i;
                                            for(i=0;i<response.body().getResults().size();i++){
                                                if(response.body().getResults().get(i).getTitle().equalsIgnoreCase(search.getText().toString())) {
                                                    tmdbId.add(response.body().getResults().get(i).getId());
                                                    break;
                                                }
                                            }
                                            if(i==response.body().getResults().size()) {
                                                tmdbId.add(response.body().getResults().get(0).getId());
                                            }
                                        }
                                        else{
                                            Toast.makeText(Recommend.this, "Can't recommend for the latest addition!", Toast.LENGTH_LONG).show();
                                        }
                                        search.setText("");
                                    }
                                    @Override
                                    public void onFailure(Call<ActorPhoto> call, Throwable t) {}
                                });
                            }
                    }

                    @Override
                    public void onFailure(Call<Pojoex> call, Throwable t) {
                        Toast.makeText(Recommend.this, "Movie not available!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count>=3) {
                    v.startAnimation(buttonClick);
                    Intent intent=new Intent(Recommend.this, Filler.class);
                    intent.putExtra("case", 4);
                    intent.putExtra("recommendations", tmdbId);
                    startActivity(intent);
                }
            }
        });
    }
}
