package com.example.lenovo.retrofit_check;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

/**
 * Created by Lenovo on 1/25/2019.
 */

public class tmdbadapter extends RecyclerView.Adapter<tmdbadapter.myViewHolder>{
    Tmdb data;
    View itemView;
    Context mContext;
    Boolean InternetCheck;
    public tmdbadapter(Tmdb input, Boolean check, Context context){
        mContext=context;
        this.data=input;
        this.InternetCheck=check;
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rv, viewGroup, false);

        final myViewHolder holder = new myViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //to be pooped on....
                String url2 = "http://api.themoviedb.org/3/movie/"+data.getResults().get(holder.getAdapterPosition()).getId()+"?api_key=6de800fc448d56225920d59e2cf378d2";
                RequestQueue queue = Volley.newRequestQueue(itemView.getContext());

                final JsonObjectRequest JsonObjectRequest = new JsonObjectRequest

                        (Request.Method.GET, url2, null, new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    final Call<plot> plotCall=myAPI.getService().getPlot(response.getString("imdb_id"));
                                    plotCall.enqueue(new Callback<plot>() {
                                        @Override
                                        public void onResponse(Call<plot> call, Response<plot> response) {
                                            plot ans=response.body();
                                            if(ans==null){
                                                Toast.makeText(mContext, "Not Available!!!", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            else if(ans.getYear()==null)
                                                return;
                                            Intent i = new Intent(itemView.getContext(), everything.class);
                                            i.putExtra("everything_needed", ans);
                                            itemView.getContext().startActivity(i);
                                        }

                                        @Override
                                        public void onFailure(Call<plot> call, Throwable t) {
                                            Toast.makeText(v.getContext(), "You aren't connected to Internet!", Toast.LENGTH_SHORT).show();
                                        }
                                    });

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
        });
        return holder;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i) {
        myViewHolder.title.setText(data.getResults().get(i).getTitle());
        if(data.getResults().get(i).getReleaseDate()!=null&&data.getResults().get(i).getReleaseDate().length()>=4)
            myViewHolder.year.setText(data.getResults().get(i).getReleaseDate().substring(0, 4));
        myViewHolder.type.setText("Movie");

        if(data.getResults().get(i).getTitle().length()>26)
            myViewHolder.title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 23);

        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.fitCenter();
        options.dontTransform();
        if(data.getResults().get(myViewHolder.getAdapterPosition()).getPosterPath()==null||data.getResults().get(myViewHolder.getAdapterPosition()).getPosterPath().equals("N/A")
        ||data.getResults().get(myViewHolder.getAdapterPosition()).getPosterPath().equalsIgnoreCase("null")){
            myViewHolder.poster.setColorFilter(Color.rgb(248, 245, 245));
            myViewHolder.lottieAnimationView.setVisibility(View.VISIBLE);
        }
        else {
            Glide.with(myViewHolder.poster.getContext())
                    .load("https://image.tmdb.org/t/p/original" + data.getResults().get(i).getPosterPath()).apply(options)
                    .into(myViewHolder.poster);
            myViewHolder.poster.setBackgroundColor(Color.WHITE);
            if(!InternetCheck){
                ColorMatrix colorMatrix = new ColorMatrix();
                colorMatrix.setSaturation(0);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
                myViewHolder.poster.setColorFilter(filter);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.getResults().size();
    }
    public  class myViewHolder extends RecyclerView.ViewHolder{
        public AppCompatTextView title, year, type;
        public ImageView poster;
        public RelativeLayout relativeLayout;
        LottieAnimationView lottieAnimationView;
        public myViewHolder(@NonNull View view) {
            super(view);
            relativeLayout=view.findViewById(R.id.con);
            title = view.findViewById(R.id.textView1);
            year =  view.findViewById(R.id.textView2);
            type =  view.findViewById(R.id.textView3);
            lottieAnimationView=view.findViewById(R.id.lottie);
            poster = view.findViewById(R.id.imageView);
        }
    }
    public void addItem(Result item, int position) {
        this.data.getResults().add(position, item);
        notifyItemInserted(position);
        notifyItemRangeChanged(0,this.data.getResults().size());
    }
}

