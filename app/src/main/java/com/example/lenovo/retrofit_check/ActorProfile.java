package com.example.lenovo.retrofit_check;

import android.graphics.Color;
import android.graphics.Typeface;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActorProfile extends AppCompatActivity {

    TextView textView;
    TextView tv;
    String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_profile);

        value = getIntent().getExtras().getString("name");

        tv=findViewById(R.id.bio);
        textView= findViewById(R.id.title);
        final ImageView imageView=findViewById(R.id.Photo);
        final Call<ActorPhoto> ProfieCall=API2.getService().getActor(value);
        ProfieCall.enqueue(new Callback<ActorPhoto>() {
            @Override
            public void onResponse(Call<ActorPhoto> call, Response<ActorPhoto> response) {
                if(response.body().getResults().isEmpty()){
                    TextView tv=findViewById(R.id.DefaultText);
                    tv.setVisibility(View.VISIBLE);
                    return;
                }
                /*PhotoRes photoRes=response.body().getResults().get(0);*/
                PhotoRes photoRes=null;
                for(int i=0;i<response.body().getResults().size();i++){
                    if(response.body().getResults().get(i).getName().equalsIgnoreCase(value)){
                        photoRes=response.body().getResults().get(i);
                        break;
                    }
                }
                if(photoRes==null) {
                    Toast.makeText(getApplicationContext(),value, Toast.LENGTH_SHORT).show();
                    return;
                }
                textView.setText(photoRes.getName());
                if(photoRes.getProfilePath()==null||photoRes.getProfilePath().equals("N/A")||photoRes.getProfilePath().equalsIgnoreCase("null")) {
                    imageView.setBackgroundColor(Color.rgb(248, 245, 245));
                    imageView.setPadding(60,128,
                            60, 122);
                    Glide.with(ActorProfile.this)
                            .load(R.drawable.error).apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                            .into(imageView);
                }
                else {
                    imageView.setPadding(0, 0, 0, 0);
                    Glide.with(ActorProfile.this)
                            .load("http://image.tmdb.org/t/p/w185"+photoRes.getProfilePath())
                            .apply(RequestOptions.centerCropTransform())
                            .into(imageView);
                }

                final Call<Bio> BioCall=API2.getService().getBio(Integer.toString(photoRes.getId()));
                BioCall.enqueue(new Callback<Bio>() {
                    @Override
                    public void onResponse(Call<Bio> call, Response<Bio> response) {
                        String birthDay="";
                        if(response.body().getB()!=null){
                            birthDay="\nBorn On:\t"
                                    +response.body().getB();
                        }
                        tv.setText("\nBIOGRAPHY\n\nKnown For:\t"+response.body().getK()+birthDay+"\n\n"+
                                response.body().getBiography());
                        if(response.body().getGender()==2)
                            tv.setTextColor(Color.parseColor("#FF4E342E"));
                        else{
                            tv.setTextColor(Color.parseColor("#FFAD1457"));
                        }
                        tv.setTextSize((int)(7.5*getResources().getDisplayMetrics().density));
                    }
                    @Override
                    public void onFailure(Call<Bio> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<ActorPhoto> call, Throwable t) {

            }
        });
    }
}
