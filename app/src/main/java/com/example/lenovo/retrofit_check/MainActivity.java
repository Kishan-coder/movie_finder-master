package com.example.lenovo.retrofit_check;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import java.util.ArrayList;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Tmdb data;

    private RecyclerView recyclerView;
    private AutoCompleteTextView searchView;
    private TextView textView;
    private AppCompatTextView textview;
    private ArrayList<Pojoex> bookPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);

        Paper.init(getApplicationContext());
        if(Paper.book().contains("searchmovie")) {
            bookPage = Paper.book().read("searchmovie");
        }
        else
            bookPage=new ArrayList<>();
        searchView=findViewById(R.id.searchview);
        textView=findViewById(R.id.textView);
        textview=findViewById(R.id.trending);
        textView.setVisibility(View.GONE);
        searchView.setHint("Enter Movie/ TV Series");

        recyclerView=findViewById(R.id.rv1);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        final Call<Tmdb> Tmdbcall=API2.getService().getPop();
        /*try {
            Response<Tmdb> response=Tmdbcall.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        Tmdbcall.enqueue(new Callback<Tmdb>() {
            @Override
            public void onResponse(Call<Tmdb> call, Response<Tmdb> response) {
                data=response.body();
                if(searchView.getText().toString().isEmpty())
                    recyclerView.setAdapter(new tmdbadapter(data, true, MainActivity.this));
                if(!Paper.book().contains("pop"))
                    Paper.book().write("pop", data);
                else{
                    Tmdb tempdata=Paper.book().read("pop");
                    tempdata.getResults();
                    if(!tempdata.getResults().get(0).getTitle().equals(data.getResults().get(0).getTitle())) {
                        tempdata = new Tmdb(data);
                        Paper.book().write("pop", tempdata);
                    }
                }
            }

            @Override
            public void onFailure(Call<Tmdb> call, Throwable t) {
                if(Paper.book().contains("pop"))
                    recyclerView.setAdapter(new tmdbadapter((Tmdb) Paper.book().read("pop"), false, MainActivity.this));
                ArrayList<String> searchItems= new ArrayList<>();
                if(Paper.book().contains("searchmovie")) {
                    ArrayList<Pojoex> arrayList = Paper.book().read("searchmovie");
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (arrayList.get(i).getSearch() != null)
                            searchItems.add(arrayList.get(i).searchName);
                    }
                    AuctoCompleteAdapter adapter = new AuctoCompleteAdapter(MainActivity.this, R.layout.list_view_row, new ArrayList<>(searchItems));
                    searchView.setAdapter(adapter);
                }
            }
        });
        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                    searchView.setHint("");
            }
        });
        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
                    final Call<Pojoex> pojoexCall=myAPI.getService().getPojoex(searchView.getText().toString());
                    pojoexCall.enqueue(new Callback<Pojoex>() {
                        @Override
                        public void onResponse(Call<Pojoex> call, Response<Pojoex> response) {
                            Pojoex data=response.body();
                            if(data.getSearch()==null) {
                                textview.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);
                                textView.setVisibility(View.VISIBLE);
                                return;
                            }
                            else {
                                textview.setVisibility(View.GONE);
                                recyclerView.getLayoutParams().height=1710;
                                recyclerView.setVisibility(View.VISIBLE);
                                textView.setVisibility(View.GONE);
                                for(int i=0;i<data.getSearch().size()-1;i++){
                                    if(data.getSearch().get(i).getYear().equals(data.getSearch().get(i+1).getYear()))
                                        data.getSearch().remove(i+1);
                                }
                                recyclerView.setAdapter(new myadapter(data, true, MainActivity.this));
                            }
                            if(bookPage.size()==5){
                                for(int i=0;i<5;i++){
                                    if(bookPage.get(i).searchName.equals(data.searchName)){
                                        bookPage.remove(i);
                                        break;
                                    }
                                }
                                if(bookPage.size()==5) {
                                    bookPage.remove(bookPage.size() - 1);
                                }
                                data.searchName=searchView.getText().toString();
                                bookPage.add(0,data);
                                Paper.book().write("searchmovie", bookPage);
                            }
                            if(bookPage.size()<5){
                                data.searchName=searchView.getText().toString();
                                bookPage.add(0,data);
                            }
                        }
                        @Override
                        public void onFailure(Call<Pojoex> call, Throwable t) {
                            if(searchView.getText().toString()=="")
                                return;
                            recyclerView.getLayoutParams().height=1710;
                            recyclerView.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.GONE);
                            textview.setVisibility(View.GONE);
                            if(Paper.book().contains("searchmovie")) {
                                ArrayList<Pojoex> al = Paper.book().read("searchmovie");
                                int i;
                                for (i = 0; i < al.size(); i++) {
                                    if (al.get(i).searchName.equals(searchView.getText().toString())) {
                                        recyclerView.setAdapter(new myadapter(al.get(i), false, MainActivity.this));
                                        break;
                                    }
                                }
                                InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                            }
                        }
                    });
                    return true;
                }
                return false;
            }
        });
    }
}
