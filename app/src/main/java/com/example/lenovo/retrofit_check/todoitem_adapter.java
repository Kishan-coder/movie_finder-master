package com.example.lenovo.retrofit_check;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import java.util.LinkedHashMap;

import io.paperdb.Paper;

public class todoitem_adapter extends RecyclerView.Adapter<todoitem_adapter.myViewHolder> {
    ArrayList<ArrayList<String>>data;
    ArrayList<String> d;
    Context mContext;
    LinkedHashMap<String, ArrayList<String>> hmap;
    public  todoitem_adapter( Context context, ArrayList<ArrayList<String>> multiple){
        Paper.init(context);
        mContext=context;
        hmap= Paper.book().read("mylist");
        data=multiple;
    }
    public  todoitem_adapter(Context context){
        Paper.init(context);
        mContext=context;
        hmap= Paper.book().read("mylist");
        data=new ArrayList<>();
        ArrayList<String> keyset= new ArrayList<>(hmap.keySet());
        //search.addAll(hmap.keySet());
        for(int i=keyset.size()-1;i>=0;i--) {
            data.add(hmap.get(keyset.get(i)));
        }
    }
    public  todoitem_adapter(ArrayList<String> mPlot, Context context){
        Paper.init(context);
        hmap= Paper.book().read("mylist");
        mContext=context;
        data=new ArrayList<>();
        data.add(mPlot);
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rv, viewGroup, false);
        final myViewHolder holder = new myViewHolder(itemView);
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ( ((Launcher) mContext)).onLongClickr(holder.getAdapterPosition());
                return false;
            }
        });
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return  position;
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, final int i) {
        d=data.get(i);
        myViewHolder.title.setText(d.get(0));
        myViewHolder.year.setText(d.get(1));
        myViewHolder.type.setText(d.get(2));

        if(d.get(3)==null||d.get(3).equals("N/A")||d.get(3).equalsIgnoreCase("null")) {
            myViewHolder.poster.setBackgroundColor(Color.rgb(248, 245, 245));
            myViewHolder.poster.setPadding(60,128,
                    60, 122);
            Glide.with(myViewHolder.poster.getContext())
                    .load(R.drawable.error).apply(new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                    .into(myViewHolder.poster);
        }
        else {
            myViewHolder.poster.setPadding(0, 0, 0, 0);
            Glide.with(myViewHolder.poster.getContext())
                    .load(d.get(3)).apply(new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                    .into(myViewHolder.poster);
            myViewHolder.poster.setBackgroundColor(Color.WHITE);
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }
    public  class myViewHolder extends RecyclerView.ViewHolder{
        public AppCompatTextView title, year, type;
        public LottieAnimationView lottieAnimationView;
        public ImageView poster;
        public RelativeLayout relativeLayout;
        public myViewHolder(@NonNull View view) {
            super(view);
            relativeLayout=view.findViewById(R.id.con);
            title = view.findViewById(R.id.textView1);
            year =  view.findViewById(R.id.textView2);
            type =  view.findViewById(R.id.textView3);
            poster = view.findViewById(R.id.imageView);
            lottieAnimationView=view.findViewById(R.id.lottie);
        }
    }
    public void remove(int pos){
        d=data.get(pos);
        Toast.makeText(mContext, d.get(0)+" removed from your list!", Toast.LENGTH_SHORT).show();
        hmap=Paper.book().read("mylist");
        hmap.remove(d.get(0));
        data.remove(pos);
        Paper.book().write("mylist", hmap);
        notifyItemRemoved(pos);
    }
    public void add(){
        hmap=Paper.book().read("mylist");
        ArrayList<String> keyset= new ArrayList<>(hmap.keySet());
        int temp=data.size();
        for(int i=data.size();i<keyset.size();i++)
            data.add(0, hmap.get(keyset.get(i)));

        notifyItemRangeInserted(0, keyset.size()-temp);
    }
    public  void mismatch(AuctoCompleteAdapter search){
        hmap=Paper.book().read("mylist");
        for(int i=0;i<data.size();i++)
            if(!hmap.containsKey(data.get(i).get(0))){
                search.remove(data.get(i).get(0));
                data.remove(i);
            }
            notifyDataSetChanged();
            search.notifyDataSetChanged();
    }

    public void mismatchParent(AuctoCompleteAdapter search){
        //repeast for a reason: preventing=>notifyDatasetChanged 2 calls
        hmap=Paper.book().read("mylist");
        for(int i=0;i<data.size();i++)
            if(!hmap.containsKey(data.get(i).get(0))){
                search.remove(data.get(i).get(0));
                data.remove(i);
            }
        ArrayList<String> keyset= new ArrayList<>(hmap.keySet());
        for(int i=data.size();i<keyset.size();i++) {
            search.insert(keyset.get(i), 0);
            data.add(0, hmap.get(keyset.get(i)));
        }
        notifyDataSetChanged();
        search.notifyDataSetChanged();
    }
}
