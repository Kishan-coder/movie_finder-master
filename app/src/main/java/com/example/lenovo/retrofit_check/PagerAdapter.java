package com.example.lenovo.retrofit_check;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private static HomePage homePage=new HomePage();
    private static ToDoList toDoList=new ToDoList();
    private static downloadFragment downloadFragment=new downloadFragment();
    private static movieofday movieofday_=new movieofday();
    public PagerAdapter(FragmentManager fm){
            super(fm);
    }
    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return homePage;
            case 1:
                return toDoList;
            case 2:
                return downloadFragment;
            case 3:
                return movieofday_;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
