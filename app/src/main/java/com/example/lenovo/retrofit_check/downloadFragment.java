package com.example.lenovo.retrofit_check;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import io.paperdb.Paper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedHashMap;

public class downloadFragment extends Fragment {

    public downloadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) { }
    }
    public RecyclerView recyclerView;
    Context c;
    TextView textView;
    RelativeLayout relativeLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_filler, container, false);

        recyclerView=view.findViewById(R.id.rv1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        relativeLayout=view.findViewById(R.id.rvVis);
        textView=view.findViewById(R.id.empty);
        c = inflater.getContext();
        if(recyclerView.getAdapter()==null){
            recyclerView.setAdapter(new video_adapter(null,c,false));
        }
        Paper.init(c);
        if(!Paper.book().contains("downloads")||((LinkedHashMap<String, String>)Paper.book().read("downloads")).size()==0) {
            textView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.INVISIBLE);
            return view;
        }
        return view;
    }

    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                video_adapter adapter = (video_adapter) recyclerView.getAdapter();
                adapter.remove(swipedPosition);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public  void flip(int pos){
        recyclerView.smoothScrollToPosition(pos);
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(pos);
        if(viewHolder==null)
            return;
        View view = viewHolder.itemView;
        Animation faded = AnimationUtils.loadAnimation(getActivity(), R.anim.faded);
        view.startAnimation(faded);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            LinkedHashMap<String, String> linkedHashMap=Paper.book().read("downloads");
            if(linkedHashMap!=null) {
                if (linkedHashMap.size() == 0) {
                    textView.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.INVISIBLE);
                }
                else{
                    if(relativeLayout.getVisibility()==View.INVISIBLE) {
                        textView.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                    }
                    video_adapter currentAdapter=(video_adapter) recyclerView.getAdapter();
                    currentAdapter.add();
                }
            }
        }
    }
}
