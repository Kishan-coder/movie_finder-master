package com.example.lenovo.retrofit_check;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.concurrent.ThreadLocalRandom;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class movieofday extends Fragment {



    public movieofday() {
        // Required empty public constructor
    }
    int pageNum, itemNo;
    Call<Tmdb> pageCall;
    LinearLayout fl, f;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0F);
    private ImageView imageView;
    TextView textView1;
    private AppCompatTextView btmbtn1;
    private AppCompatTextView btmbtn2;
    private AppCompatTextView btmbtnfinal;
    plot theplot;
    View view;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_everything, container, false);
        imageView=view.findViewById(R.id.imageView);
        btmbtn1=view.findViewById(R.id.btm);
        btmbtn2=view.findViewById(R.id.similars);
        btmbtnfinal=view.findViewById(R.id.utube);
        textView1=view.findViewById(R.id.tv_image);
        fl=view.findViewById(R.id.fl);
        f=view.findViewById(R.id.f);
        Paper.init(inflater.getContext());
        if(!Paper.book().contains("specialToday")){
            getPlot(inflater.getContext());
        }

        theplot=Paper.book().read("specialToday");
        if(theplot==null) {
            ScrollView scrollView=view.findViewById(R.id.scv);
            scrollView.setBackgroundResource(R.drawable.error);
            RelativeLayout relativeLayout=view.findViewById(R.id.rv);
            relativeLayout.setVisibility(View.GONE);
            return view;
        }
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.fitCenter();
        Glide.with(inflater.getContext())
                .load(theplot.getPoster()).apply(options)
                .into(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        String total_text=theplot.getTitle().toUpperCase()+"\n";
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        String currentDate = sdf.format(new Date());
        Integer x= null;
        try {
            x = (sdf.parse(currentDate)).compareTo(sdf.parse(theplot.getReleased()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(x==null);
        else if(x>=0)
            currentDate="Released on: "+theplot.getReleased()+"\n";
        else
            currentDate="Releases on: "+theplot.getReleased()+"\n";
        if(currentDate.charAt(0)=='R')
            total_text+=currentDate;

        total_text+="Country: "+theplot.getCountry()+"\n" + "Genre: "+ theplot.getGenre();
        Spannable spannable = new SpannableString(total_text);
        spannable.setSpan( new CustomTypefaceSpan("", (int)(30*getResources().getDisplayMetrics().density)), 0, theplot.getTitle().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan( new CustomTypefaceSpan("", (int)(23*getResources().getDisplayMetrics().density)), theplot.getTitle().length(), total_text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView1.setText( spannable );

        TextView textView2=view.findViewById(R.id.tv_actor);

        String Info;
        Info="ACTORS :\n"+theplot.getActors()+".";
        Info+="\n\nDIRECTOR(s) :\n"+theplot.getDirector()+ ".\n\nWRITER(s) :\n"+ theplot.getWriter()+".";
        spannable = new SpannableString(Info);
        spannable.setSpan( new CustomTypefaceSpan("", (int)(27*getResources().getDisplayMetrics().density)), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan( new CustomTypefaceSpan("", (int)(23*getResources().getDisplayMetrics().density)), 9, theplot.getActors().length()+9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan( new CustomTypefaceSpan("", (int)(27*getResources().getDisplayMetrics().density)), theplot.getActors().length()+9, theplot.getActors().length()+9+14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan( new CustomTypefaceSpan("", (int)(23*getResources().getDisplayMetrics().density)), theplot.getActors().length()+9+14, theplot.getActors().length()+9+16+theplot.getDirector().length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan( new CustomTypefaceSpan("", (int)(27*getResources().getDisplayMetrics().density)), theplot.getActors().length()+9+16+theplot.getDirector().length()+1, theplot.getActors().length()+9+15+theplot.getDirector().length()+14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan( new CustomTypefaceSpan("", (int)(23*getResources().getDisplayMetrics().density)), theplot.getActors().length()+9+15+theplot.getDirector().length()+14, Info.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView2.setText( spannable );

        textView2.setMovementMethod(LinkMovementMethod.getInstance());
        TextView textView3=view.findViewById(R.id.plot);
        String plot;
        plot="MOVIES' PLOT :\n";
        plot+=theplot.getPlot();
        spannable = new SpannableString(plot);
        spannable.setSpan( new CustomTypefaceSpan("", (int)(27*getResources().getDisplayMetrics().density)), 0, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan( new CustomTypefaceSpan("", (int)(23*getResources().getDisplayMetrics().density)), 15, plot.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView3.setText(spannable);

        String other="OTHER INFORMATION :\n";
        other+="RATINGS:\n";
        Integer I;
        for(I=1;I<theplot.getRatings().size();I++){
            other+=I.toString()+". "+theplot.getRatings().get(I-1).getSource()+": "+theplot.getRatings().get(I-1).getValue()+"\n";
        }
        other+="\n Production House: "+theplot.getProduction();
        other+="\nBOX OFFICE COLLECTION:"+theplot.getBoxOffice();
        other+="\nAwards: "+theplot.getAwards();

        TextView textView4= view.findViewById(R.id.other);
        spannable = new SpannableString(other);
        spannable.setSpan( new CustomTypefaceSpan("", (int)(27*getResources().getDisplayMetrics().density)), 0, 20, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan( new CustomTypefaceSpan("", (int)(23*getResources().getDisplayMetrics().density)), 20, other.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView4.setText(spannable);

        btmbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                if(!Paper.book().contains("mylist")){
                    LinkedHashMap<String, ArrayList<String>> map=new LinkedHashMap<>();
                    Paper.book().write("mylist", map);
                }
                LinkedHashMap<String, ArrayList<String>> map=Paper.book().read("mylist");
                if(!map.containsKey(theplot.getTitle())) {
                    Toast.makeText(inflater.getContext(), theplot.getTitle() + " added to your list! ", Toast.LENGTH_SHORT).show();
                    ArrayList<String> key=new ArrayList<>();
                    key.add(theplot.getTitle()); key.add(theplot.getYear()); key.add(theplot.getType()); key.add(theplot.getPoster());
                    map.put(theplot.getTitle(), key);
                    Paper.book().write("mylist", map);
                }
                else{
                    Toast.makeText(inflater.getContext(), "Movie already present in your list!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btmbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                final Call<ActorPhoto> ForIdCall=API2.getService().getMovieId(theplot.getTitle());
                ForIdCall.enqueue(new Callback<ActorPhoto>() {
                    @Override
                    public void onResponse(Call<ActorPhoto> call, Response<ActorPhoto> response) {
                        Intent intent=new Intent(inflater.getContext(), Filler.class);
                        intent.putExtra("case", 5);
                        for(int i=0;i<response.body().getResults().size();i++) {
                            if(response.body().getResults().get(i).getTitle().equalsIgnoreCase(theplot.getTitle())) {
                                intent.putExtra("id", response.body().getResults().get(i).getId());
                                startActivity(intent);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ActorPhoto> call, Throwable t) {

                    }
                });
            }
        });
        btmbtnfinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                final Call<ActorPhoto> ForIdCall=API2.getService().getMovieId(theplot.getTitle());
                ForIdCall.enqueue(new Callback<ActorPhoto>() {
                    @Override
                    public void onResponse(Call<ActorPhoto> call, Response<ActorPhoto> response) {
                        Intent intent=new Intent(inflater.getContext(), Filler.class);
                        intent.putExtra("case", 6);
                        intent.putExtra("country", theplot.getCountry());
                        intent.putExtra("movieName", theplot.getTitle());
                        for(int i=0;i<response.body().getResults().size();i++) {
                            if(response.body().getResults().get(i).getTitle().equalsIgnoreCase(theplot.getTitle())) {
                                intent.putExtra("id", response.body().getResults().get(i).getId());
                                startActivity(intent);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ActorPhoto> call, Throwable t) {

                    }
                });
            }
        });
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && view!=null) {
            if(!isOnline()){
                fl.setVisibility(View.GONE);
                f.setVisibility(View.GONE);
            }
            else if(isOnline()){
                fl.setVisibility(View.VISIBLE);
                f.setVisibility(View.VISIBLE);
            }
        }
    }
    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public  void getPlot(final Context context){
        Paper.init(context);
        theplot=Paper.book().read("specialToday");
        itemNo = ThreadLocalRandom.current().nextInt(0, 20);
        if(theplot!=null&&theplot.getCountry()!="India"){
            pageNum= ThreadLocalRandom.current().nextInt(1, 50);
            pageCall=API2.getService().getRandIN(Integer.toString(pageNum));
        }else {
            pageNum= ThreadLocalRandom.current().nextInt(1, 50);
            pageCall=API2.getService().getRand(Integer.toString(pageNum));
        }
        pageCall.enqueue(new Callback<Tmdb>() {
            @Override
            public void onResponse(Call<Tmdb> call, Response<Tmdb> response) {
                String title=response.body().getResults().get(itemNo).getTitle();
                final  Call<plot> plotCall=myAPI.getService().getplot(title);
                plotCall.enqueue(new Callback<plot>() {
                    @Override
                    public void onResponse(Call<plot> call, Response<plot> response) {
                        if(response.body().getTitle()==null){
                            getPlot(context);
                            return;
                        }
                        Paper.book().write("specialToday", response.body());
                    }

                    @Override
                    public void onFailure(Call<plot> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<Tmdb> call, Throwable t) {

            }
        });
    }
}
