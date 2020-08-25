package com.example.lenovo.retrofit_check;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

import io.paperdb.Paper;

import static android.widget.Toast.LENGTH_SHORT;

public class Launcher extends FragmentActivity {
    ViewPager viewPager;
    String name, value;
    DialogAlert dialogAlert=null;
    PagerAdapter pagerAdapter;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        Calendar calendar = Calendar.getInstance();

        Paper.init(Launcher.this);

        if(!Paper.book().contains("People")){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("message");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String ans=dataSnapshot.getValue(String.class);
                    Paper.book().write("people", ans);
                    HashMap<String, Integer> hmap=new HashMap<>();
                    for(int i=0;i<ans.length();i++){
                        if(ans.charAt(i)==':'){
                            i++;
                            value="";
                            while(i<ans.length()&&ans.charAt(i)>='0'&&ans.charAt(i)<='9') {
                                value += ans.charAt(i);
                                i++;
                            }
                            i++;
                            hmap.put(name, Integer.parseInt(value));
                            name="";
                        }
                        else if((ans.charAt(i)>='a'&&ans.charAt(i)<='z')||ans.charAt(i)==' ')
                            name+=ans.charAt(i);
                    }
                    Paper.book().write("People", hmap);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        if(!Paper.book().contains("alarmcheck")){
            Paper.book().write("alarmcheck", dateFormat.format(calendar.getTime()));
        }
        if(Paper.book().contains("alarmcheck")) {
            String stored_min=Paper.book().read("alarmcheck").toString();
            String current_min=dateFormat.format(calendar.getTime());
            if(Math.abs(Integer.parseInt(stored_min)-Integer.parseInt(current_min))>=1)
                new movieofday().getPlot(Launcher.this);
            Paper.book().write("alarmcheck", dateFormat.format(calendar.getTime()));
        }
        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Home"));
        tabLayout.addTab(tabLayout.newTab().setText("My To-Do List"));
        tabLayout.addTab(tabLayout.newTab().setText("My Downloads"));
        tabLayout.addTab(tabLayout.newTab().setText("Movie of the Day"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            TextView tv=(TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab,null);
//            tv.setTypeface(typeface);
//            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
//            tabLayout.getTabAt(i).setCustomView(tv);
//        }

        viewPager = findViewById(R.id.vp);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                final InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        if(dialogAlert!=null&&dialogAlert.getDialog()!=null&&dialogAlert.getDialog().isShowing()){
            dialogAlert.getDialog().dismiss();
            return;
        }
        if(viewPager.getCurrentItem()!=1){
            super.onBackPressed();
        }
        LinkedHashMap<String, ArrayList<String>> map=Paper.book().read("mylist");
        ArrayList<String> set = new ArrayList<>(map.keySet());
        Collections.reverse(set);
        FragmentManager fragmentManager=getSupportFragmentManager();
        if(fragmentManager.getBackStackEntryCount()==1){
            ToDoList ViewPagerFrag=(ToDoList) pagerAdapter.getItem(1);
            ViewPagerFrag.searchview.setText("");
            ViewPagerFrag.searchview.setHint("Enter the Name:");
            todoitem_adapter t=(todoitem_adapter)ViewPagerFrag.rv.getAdapter();
            t.mismatchParent((AuctoCompleteAdapter) ViewPagerFrag.searchview.getAdapter());
            ViewPagerFrag.searchview.setAdapter(new AuctoCompleteAdapter(this, R.layout.list_view_row, new ArrayList<>(set)));
            fragmentManager.popBackStackImmediate();
            return;
        }
        fragmentManager.popBackStackImmediate();
        if(fragmentManager.getBackStackEntryCount()>0){
            int i=fragmentManager.getFragments().size()-1;
            while(!(fragmentManager.getFragments().get(i) instanceof ToDoList))
                    i--;
            ToDoList currentTop=(ToDoList) fragmentManager.findFragmentById(fragmentManager.getFragments().
                    get(i).getId());
            currentTop.searchview.setText(currentTop.getArguments().getString("actualText"));
            currentTop.searchview.dismissDropDown();
            todoitem_adapter t=(todoitem_adapter)currentTop.rv.getAdapter();
            t.mismatch((AuctoCompleteAdapter) currentTop.searchview.getAdapter());
            currentTop.searchview.setAdapter(new AuctoCompleteAdapter(this, R.layout.list_view_row, new ArrayList<>(set)));
        }
        else{
            super.onBackPressed();
        }
    }
    public void onLongClickr(int pos){
        dialogAlert=new DialogAlert(pos);
        Bundle bundle=new Bundle();
        bundle.putString("for", "mylist");
        dialogAlert.setArguments(bundle);
        dialogAlert.show(getSupportFragmentManager(), "To Remove");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            downloadFragment f = (downloadFragment) viewPager
                    .getAdapter()
                    .instantiateItem(viewPager, viewPager.getCurrentItem());
            f.flip(data.getIntExtra("result", 0));
        }
    }
}
