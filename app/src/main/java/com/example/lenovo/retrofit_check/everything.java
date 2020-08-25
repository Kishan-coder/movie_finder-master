package com.example.lenovo.retrofit_check;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class everything extends AppCompatActivity{

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0F);
    private ImageView imageView;
    TextView textView1;
    Intent intent;
    private AppCompatTextView btmbtn1;
    private AppCompatTextView btmbtn2;
    private AppCompatTextView btmbtnfinal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_everything);
        intent =new Intent(everything.this, ActorProfile.class);

        Paper.init(getApplicationContext());
        imageView = findViewById(R.id.imageView);
        textView1 = findViewById(R.id.tv_image);
        btmbtn1=findViewById(R.id.btm);
        btmbtn2=findViewById(R.id.similars);
        btmbtnfinal=findViewById(R.id.utube);
        Intent i = getIntent();
        final plot theplot = (plot) i.getSerializableExtra("everything_needed");
        final Boolean check=i.getBooleanExtra("listCheck", false);
        if(check){
            LinearLayout fl=findViewById(R.id.fl);
            LinearLayout f=findViewById(R.id.f);
            fl.setVisibility(View.GONE);
            f.setVisibility(View.GONE);
        }
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.fitCenter();
        if(theplot.getPoster()==null||theplot.getPoster().equals("N/A")||theplot.getPoster().equalsIgnoreCase("null")) {
            imageView.setBackgroundColor(Color.rgb(248, 245, 245));
            imageView.setPadding(60,128,
                    60, 122);
            Glide.with(this)
                    .load(R.drawable.error).apply(new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                    .into(imageView);
        }
        else {
            imageView.setPadding(0, 0, 0, 0);
            Glide.with(this)
                    .load(theplot.getPoster()).apply(options)
                    .into(imageView);
            imageView.setBackgroundColor(Color.WHITE);
        }
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

        TextView textView2=findViewById(R.id.tv_actor);

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
        Spannable spans = (Spannable) textView2.getText();
        String possibleWord="";
        int start=0;
        for(int i1=0;i1<Info.length();i1++) {
            Character c=Info.charAt(i1);
            if(c=='\n') {
                possibleWord="";
                continue;
            }
            else if(c==','||c=='.') {
                if(c=='.'&&i1+1<Info.length()&&(Info.charAt(i1+1)==','||Info.charAt(i1+1)=='.'))
                    continue;
                possibleWord=possibleWord.trim();
                ClickableSpan clickSpan = getClickableSpan(possibleWord);
                spans.setSpan(clickSpan, start, i1,
                        Spannable.SPAN_COMPOSING);
                possibleWord="";
                start=i1+2;

            }
            else {
                if(c=='(')
                    while(c!=')') {
                        i1++;
                        c = Info.charAt(i1);
                    }
                else
                    possibleWord += c;
            }
        }

        TextView textView3=findViewById(R.id.plot);
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
        if(I==1)
            other+="N/A";
        other+="\n Production House: "+theplot.getProduction();
        other+="\nBOX OFFICE COLLECTION:"+theplot.getBoxOffice();
        other+="\nAwards: "+theplot.getAwards();

        TextView textView4= findViewById(R.id.other);
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
                    Toast.makeText(everything.this, theplot.getTitle() + " added to your list! ", Toast.LENGTH_SHORT).show();
                    ArrayList<String> key=new ArrayList<>();
                    key.add(theplot.getTitle()); key.add(theplot.getYear()); key.add(theplot.getType()); key.add(theplot.getPoster());
                    map.put(theplot.getTitle(), key);
                    Paper.book().write("mylist", map);
                }
                else{
                    Toast.makeText(everything.this, "Movie already present in your list!", Toast.LENGTH_SHORT).show();
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
                        Intent intent=new Intent(everything.this, Filler.class);
                        intent.putExtra("case", 5);
                        int i;
                        for( i=0;i<response.body().getResults().size();i++) {
                            if(response.body().getResults().get(i).getTitle().equalsIgnoreCase(theplot.getTitle())) {
                                intent.putExtra("id", response.body().getResults().get(i).getId());
                                startActivity(intent);
                                break;
                            }
                        }
                        if(i==response.body().getResults().size()) {
                            if(i==0) {
                                Toast.makeText(getApplicationContext(), "Data Not Available!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            intent.putExtra("id", response.body().getResults().get(0).getId());
                            startActivity(intent);
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
                        Intent intent=new Intent(everything.this, Filler.class);
                        intent.putExtra("case", 6);
                        intent.putExtra("country", theplot.getCountry());
                        intent.putExtra("movieName", theplot.getTitle());
                        for(int i=0;i<response.body().getResults().size();i++) {
                            if(response.body().getResults().get(i).getTitle().equalsIgnoreCase(theplot.getTitle())) {
                                intent.putExtra("id", response.body().getResults().get(i).getId());
                                break;
                            }
                        }
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<ActorPhoto> call, Throwable t) {

                    }
                });
            }
        });
    }



    private ClickableSpan getClickableSpan(final String word) {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if(isOnline()) {
                    intent.putExtra("name", word);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(everything.this, "No Internet!!!", Toast.LENGTH_SHORT).show();
                }
            }
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
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
}
