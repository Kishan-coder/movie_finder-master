package com.example.lenovo.retrofit_check;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;

import androidx.viewpager.widget.ViewPager;
import io.paperdb.Paper;


/**
 * A simple {@link Fragment} subclass.
 */
public class ToDoList extends Fragment{

    public ToDoList() {
        // Required empty public constructor
    }
    public static ToDoList newInstance(Bundle args) {
        ToDoList fragment = new ToDoList();
        fragment.setArguments(args);
        return fragment;
    }

    public RecyclerView rv;
    public AutoCompleteTextView searchview;
    private  FragmentActivity c;
    private LinkedHashMap<String, ArrayList<String>> map;
    private static AuctoCompleteAdapter adapter;
    private RelativeLayout relativeLayout;
    private TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_to_do_list, container, false);


        c = getActivity();
        rv = view.findViewById(R.id.rv1);
        relativeLayout=view.findViewById(R.id.rvVis);
        textView=view.findViewById(R.id.empty);

        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        rv.setLayoutManager(layoutManager);
        rv.setDrawingCacheEnabled(true);
        rv.setHasFixedSize(true);

        rv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        searchview=view.findViewById(R.id.searchview);
        searchview.setThreshold(1);
        final Bundle bundle=getArguments();
        if(bundle!=null) {
            LottieAnimationView lottieAnimationView=view.findViewById(R.id.stack);
            lottieAnimationView.setVisibility(View.VISIBLE);
            searchview.setText("");
        }
        else
            searchview.setHint("Enter the Name:");
        Paper.init(c);
        if(!Paper.book().contains("mylist")) {
            RelativeLayout relativeLayout=view.findViewById(R.id.rvVis);
            TextView textView=view.findViewById(R.id.empty);
            textView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.INVISIBLE);
            return view;
        }
        else{
            map=Paper.book().read("mylist");
            ArrayList<String> s=new ArrayList<>(map.keySet());
            map.remove(s.get(map.size()-1));
        }
        searchview.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    searchview.setHint("");
                    searchview.requestFocus();
                    InputMethodManager imm=(InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(searchview, 0);
                }
            }
        });
        searchview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchview.getWindowToken(), 0);

                Bundle args = new Bundle();
                String s=(String) parent.getItemAtPosition(position);
                args.putString("name", s);
                args.putString("actualText", s);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add
                        (R.id.container, ToDoList.newInstance(args));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        searchview.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId==EditorInfo.IME_ACTION_DONE){
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchview.getWindowToken(), 0);

                    searchview.dismissDropDown();

                    ArrayAdapter<String> adapter=(ArrayAdapter<String>) searchview.getAdapter();
                    map=Paper.book().read("mylist");

                    ArrayList<String> s=new ArrayList<>(map.keySet());
                    int o;
                    for(o=0;o<s.size();o++){
                        if(s.get(o).toLowerCase().contains(searchview.getText().toString().toLowerCase()))
                            break;
                    }
                    if(o==s.size()||searchview.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getContext(), "No Match", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    ArrayList<String> CurrentSuggestions= new ArrayList<>();
                    for (int i = 0; i < adapter .getCount(); i++)
                        CurrentSuggestions.add(adapter .getItem(i));
                    Bundle args=new Bundle();
                    args.putStringArrayList("multipleNames", CurrentSuggestions);
                    args.putString("actualText", searchview.getText().toString());
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.add
                            (R.id.container, ToDoList.newInstance(args ));
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                return false;
            }
        });
        //setUpItemTouchHelper();
        Bundle args=getArguments();
        if(args!=null) {
            map=Paper.book().read("mylist");
            String name = args.getString("name", null);
            if(name!=null) {
                final todoitem_adapter radapter = new todoitem_adapter(map.get(name), c);
                rv.setAdapter(radapter);
            }
            else{
                ArrayList<String> multiple=args.getStringArrayList("multipleNames");
                ArrayList<ArrayList<String>> CurrentSuggestions=new ArrayList<>();
                for(int i=0;i<multiple.size();i++)
                    CurrentSuggestions.add(map.get(multiple.get(i)));
                final todoitem_adapter radapter = new todoitem_adapter( c, CurrentSuggestions);
                rv.setAdapter(radapter);
            }
        }
        if (Paper.book().contains("mylist")) {
            if(rv.getAdapter()==null) {
                final todoitem_adapter todoitem_adapter = new todoitem_adapter(c);
                rv.setAdapter(todoitem_adapter);
            }
            if(searchview.getAdapter()==null){
                map=Paper.book().read("mylist");
                ArrayList<String> set = new ArrayList<>(map.keySet());
                Collections.reverse(set);
                adapter = new AuctoCompleteAdapter(c, R.layout.list_view_row, new ArrayList<>(set));
                searchview.setAdapter(adapter);
            }
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
                todoitem_adapter adapter = (todoitem_adapter) rv.getAdapter();
                adapter.remove(swipedPosition);
                map=Paper.book().read("mylist");
                ArrayList<String> set = new ArrayList<>(map.keySet());
                Collections.reverse(set);
                ToDoList.adapter = new AuctoCompleteAdapter(c, R.layout.list_view_row, new ArrayList<>(set));
                searchview.setAdapter(ToDoList.adapter);
            }

        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rv);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if(searchview.getText().toString().equalsIgnoreCase(""))
                searchview.setHint("Enter the Name:");
            map=Paper.book().read("mylist");
            if(map==null||map.size()==0){
                textView.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.INVISIBLE);
            }
            else {
                if(textView.getVisibility()==View.VISIBLE) {
                    textView.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                }
                todoitem_adapter currentAdapter = (todoitem_adapter) rv.getAdapter();
                if(currentAdapter==null) {
                    currentAdapter= new todoitem_adapter(c);
                    rv.setAdapter(currentAdapter);
                }
                if(currentAdapter.getItemCount()!=map.size()&&getFragmentManager().getBackStackEntryCount()==0) {
                    currentAdapter.add();
                    rv.smoothScrollToPosition(0);
                    map=Paper.book().read("mylist");
                    ArrayList<String> set = new ArrayList<>(map.keySet());
                    Collections.reverse(set);
                    adapter = new AuctoCompleteAdapter(c, R.layout.list_view_row, new ArrayList<>(set));
                    searchview.setAdapter(adapter);
                }
            }
        }
    }
}
