package com.example.lenovo.retrofit_check;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import io.paperdb.Paper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomePage extends Fragment {

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0F);

    public HomePage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home_page, container, false);

        Paper.init(inflater.getContext());

        AppCompatTextView btn1 =v.findViewById(R.id.button1);
        AppCompatTextView btn2 =v.findViewById(R.id.button2);
        AppCompatTextView btn3 =v.findViewById(R.id.button3);
        AppCompatTextView btn4 =v.findViewById(R.id.button4);
        AppCompatTextView btn5 =v.findViewById(R.id.button5);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                Intent intent=new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                Intent intent=new Intent(getContext(), Filler.class);
                intent.putExtra("case", 1);
                startActivity(intent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                Intent intent=new Intent(getContext(), Filler.class);
                intent.putExtra("case", 2);
                startActivity(intent);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                Intent intent=new Intent(getContext(), Filler.class);
                intent.putExtra("case", 3);
                startActivity(intent);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                if(!Paper.book().contains("People")){
                    Toast.makeText(v.getContext(), "Initializing", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent(getContext(), Chat.class);
                startActivity(intent);
            }
        });
        return  v;
    }
}
