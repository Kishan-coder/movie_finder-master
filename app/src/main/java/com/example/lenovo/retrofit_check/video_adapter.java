package com.example.lenovo.retrofit_check;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import io.paperdb.Paper;

public class video_adapter extends RecyclerView.Adapter<video_adapter.myViewHolder> {

    ArrayList<Pair<String, String>> data;
    Context mcContext;
    boolean ifActivity;
    View itemView;
    LinkedHashMap<String, String> lmap;
    public  video_adapter(ArrayList<Pair<String, String>> data, Context context, boolean check){
        Paper.init(context);
        mcContext=context;
        ifActivity=check;
        if(ifActivity==false) {
            this.data=new ArrayList<>();
            if(!Paper.book().contains("downloads"))
                return;
            lmap = Paper.book().read("downloads");
            ArrayList<String> keyset= new ArrayList<>(lmap.keySet());
            for(int i=keyset.size()-1;i>=0;i--)
                this.data.add(new Pair<>(keyset.get(i),lmap.get(keyset.get(i)).split(";")[0]));
        }
        else
            this.data=data;
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.download_item, viewGroup, false);
        final myViewHolder holder = new myViewHolder(itemView);
        if(ifActivity==false) {
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    DialogAlert dialogAlert = new DialogAlert(holder.getAdapterPosition());
                    Bundle bundle = new Bundle();
                    bundle.putString("for", "downloads");
                    dialogAlert.setArguments(bundle);
                    dialogAlert.show(((Launcher) mcContext).getSupportFragmentManager(), "To Remove");
                    return false;
                }
            });
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //exoPlayer activity
                Intent intent=new Intent((mcContext), ExoPlayer.class);
                ArrayList<String>  ToPass= new ArrayList<>();
                if(ifActivity==false){
                    lmap= Paper.book().read("downloads");
                    ArrayList<String> keyset= new ArrayList<>(lmap.keySet());
                    for(int i=keyset.size()-1;i>=0;i--)
                        ToPass.add(lmap.get(keyset.get(i)).split(";")[1]);
                    intent.putStringArrayListExtra("filePaths", ToPass);
                }
                else {
                    try {
                        if(isConnected()==true){
                            for (int i = 0; i < data.size(); i++)
                                ToPass.add(data.get(i).first);
                            intent.putStringArrayListExtra("keyList", ToPass);
                        }
                        else {
                            Toast.makeText(v.getContext(), "No Internet", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(v.getContext(), "No Internet", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                intent.putExtra("current",  holder.getAdapterPosition());
                ((Activity)mcContext).startActivityForResult(intent, 1);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i) {
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.fitCenter();
            Glide.with(myViewHolder.thumbnail.getContext())
                    .load("https://img.youtube.com/vi/" + data.get(i).first + "/0.jpg").apply(options)
                    .into(myViewHolder.thumbnail);
        myViewHolder.thumbnail.setBackgroundColor(Color.WHITE);
       /* myViewHolder.title.setTextSize(convertFromDp(66));
        if(data.get(i).second.length()>=40)
            myViewHolder.title.setTextSize(convertFromDp(42));*/
        myViewHolder.title.setText(data.get(i).second);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public  class myViewHolder extends RecyclerView.ViewHolder{
        public AppCompatTextView title;
        public ImageView thumbnail;
        public RelativeLayout relativeLayout;
        public myViewHolder(@NonNull View view) {
            super(view);
            relativeLayout=view.findViewById(R.id.video);
            title = view.findViewById(R.id.video_title);
            thumbnail = view.findViewById(R.id.thumbnail);
        }
    }
    public void add(){
        lmap=Paper.book().read("downloads");
        if(lmap.size()==data.size())
            return;
        ArrayList<String> keyset= new ArrayList<>(lmap.keySet());
        for(int i=data.size();i<keyset.size();i++)
            data.add(0,  new Pair<>(keyset.get(i),lmap.get(keyset.get(i)).split(";")[0]));
        notifyDataSetChanged();
    }
    public void remove(int pos){
        Toast.makeText(mcContext, "|DELETED|", Toast.LENGTH_SHORT).show();
        File file=new File(lmap.get(data.get(pos).first).split(";")[1]);
        file.delete();
        lmap.remove(data.get(pos).first);
        data.remove(pos);
        Paper.book().write("downloads", lmap);
        notifyItemRemoved(pos);
    }
    public boolean isConnected() throws InterruptedException, IOException {
        final String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }
}
