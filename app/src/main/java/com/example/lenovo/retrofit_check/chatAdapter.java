package com.example.lenovo.retrofit_check;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.MyViewHolder> {
    ArrayList<Boolean> extas=new ArrayList<>();
    RecyclerView recyclerView;
    ArrayList<String> messages=new ArrayList<>();
    ArrayList<Boolean> checks=new ArrayList<>();
    Context context;
    Tmdb data;
    public chatAdapter(@NonNull Context context) {
        this.context=context;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView messageView;
        private AppCompatTextView extras;
        private LinearLayout linearLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            messageView = itemView.findViewById(R.id.tv_message);
            extras=itemView.findViewById(R.id.extras);
            linearLayout=itemView.findViewById(R.id.ll);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false);
        MyViewHolder myViewHolder= new MyViewHolder(itemView);
        if(viewType%10==1){
            myViewHolder.linearLayout.setBackgroundResource(R.drawable.bhagwaan1);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myViewHolder.linearLayout.getLayoutParams();
            final float scale = context.getResources().getDisplayMetrics().density;
            params.setMargins((int)(80*scale+0.5f), 0, 0, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            myViewHolder.linearLayout.setLayoutParams(params);
        }
        if((viewType/10)%10==1) {
            myViewHolder.extras.setVisibility(View.VISIBLE);
            myViewHolder.extras.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0F);
                    v.setAnimation(buttonClick);
                    Intent intent = new Intent(context, Filler.class);
                    intent.putExtra("discover", data);
                    context.startActivity(intent);
                }
            });
        }
        return myViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        int check=0;
        Boolean flag= checks.get(position);
        if(extas.get(position)==true)
            check=10;
        return flag ? check+1 : check+0;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.messageView.setText(messages.get(position));
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView=recyclerView;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addItem(String item, Boolean check, Boolean a, Tmdb data) {
        extas.add(a);
        this.data=data;
        this.checks.add(check);
        this.messages.add(item);
        notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(this.messages.size()-1);
    }

}
