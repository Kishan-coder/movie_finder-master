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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class myadapter extends RecyclerView.Adapter<myadapter.myViewHolder>{
    Pojoex data;
    View itemView;
    Boolean checkInternt;
    public myadapter(Pojoex input, Boolean check, Context context){
        this.data=input;
        this.checkInternt=check;
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

         itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rv, viewGroup, false);
        final myViewHolder holder = new myViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Call<plot> plotCall=myAPI.getService().getPlot(data.getSearch().get(holder.getAdapterPosition()).getImdbID());
                plotCall.enqueue(new Callback<plot>() {
                    @Override
                    public void onResponse(Call<plot> call, Response<plot> response) {
                        plot ans=response.body();
                        Intent i = new Intent(itemView.getContext(), everything.class);
                        i.putExtra("everything_needed", ans);
                        itemView.getContext().startActivity(i);
                    }

                    @Override
                    public void onFailure(Call<plot> call, Throwable t) {
                        Toast.makeText(itemView.getContext(), "You aren't connected to Internet", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder myViewHolder, final int i) {
        myViewHolder.title.setText(data.getSearch().get(i).getTitle().toUpperCase());
        myViewHolder.year.setText(data.getSearch().get(i).getYear());
        myViewHolder.type.setText(data.getSearch().get(i).getType().toUpperCase());

        if(data.getSearch().get(i).getTitle().length()>26)
            myViewHolder.title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 23);

        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.fitCenter();

        if(data.getSearch().get(myViewHolder.getAdapterPosition()).getPoster()==null||data.getSearch().get(myViewHolder.getAdapterPosition()).getPoster().equals("N/A")
                ||data.getSearch().get(myViewHolder.getAdapterPosition()).getPoster().equalsIgnoreCase("null")){
            myViewHolder.poster.setColorFilter(Color.rgb(248, 245, 245));
            myViewHolder.lottieAnimationView.setVisibility(View.VISIBLE);
        }
        else {
            myViewHolder.lottieAnimationView.setVisibility(View.GONE);
            Glide.with(myViewHolder.poster.getContext())
                    .load(data.getSearch().get(i).getPoster()).apply(options)
                    .into(myViewHolder.poster);
            myViewHolder.poster.setBackgroundColor(Color.WHITE);
            if(!checkInternt){
                ColorMatrix colorMatrix = new ColorMatrix();
                colorMatrix.setSaturation(0);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
                myViewHolder.poster.setColorFilter(filter);
            }
        }
    }

    @Override
    public int getItemCount() {
         return data.getSearch().size();
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
            lottieAnimationView=view.findViewById(R.id.lottie);
            type =  view.findViewById(R.id.textView3);
            poster = view.findViewById(R.id.imageView);
        }
    }
    public void addItem(Search item, int position) {
        this.data.getSearch().add(position, item);
        notifyItemInserted(position);
        notifyItemRangeChanged(0,this.data.getSearch().size());
    }
}
