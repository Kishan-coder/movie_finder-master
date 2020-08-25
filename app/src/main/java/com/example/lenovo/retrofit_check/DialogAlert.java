package com.example.lenovo.retrofit_check;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import io.paperdb.Paper;

public class DialogAlert extends AppCompatDialogFragment {
    private int posRemove=-1;
    LinkedHashMap<String, ArrayList<String>> Lmap;
    public DialogAlert(int pos){
        posRemove=pos;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity(), R. style.AlertDialogTheme).setIcon(R.drawable.dustbin);
        if(posRemove==-1)
            return null;
        LinkedHashMap<String, String> Dmap;
        String Holder;
        Bundle args=getArguments();
        final String For=args.getString("for");
        if(For.equals("mylist")) {
            Lmap = Paper.book().read("mylist");
            Holder=(String)Lmap.keySet().toArray()[Lmap.size()-posRemove-1];
        }
        else{
            Dmap = Paper.book().read("downloads");
            Holder=Dmap.get(Dmap.keySet().toArray()[Dmap.size()-posRemove-1]).split(";")[0];
        }
        builder.setTitle("Remove "+Holder).setMessage("Sure to remove "+Holder+"?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        return;
                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FragmentManager fragmentManager=getFragmentManager();
                if(For.equals("mylist")){
                    int i_=fragmentManager.getFragments().size()-1;
                    while(!(fragmentManager.getFragments().get(i_) instanceof ToDoList)) {
                        i_--;
                    }
                    ToDoList toDoList=(ToDoList) fragmentManager.getFragments().get(i_);
                    todoitem_adapter adapter=(todoitem_adapter) toDoList.rv.getAdapter();
                    adapter.remove(posRemove);
                    Lmap=Paper.book().read("mylist");
                    ArrayList<String> set = new ArrayList<>(Lmap.keySet());
                    Collections.reverse(set);
                    AuctoCompleteAdapter Sadapter = new AuctoCompleteAdapter(getActivity(), R.layout.list_view_row, new ArrayList<>(set));
                    toDoList.searchview.setAdapter(Sadapter);                }
                else{
                    int i_=fragmentManager.getFragments().size()-1;
                    while(!(fragmentManager.getFragments().get(i_) instanceof downloadFragment)) {
                        i_--;
                    }
                    downloadFragment mDownloadFragment=(downloadFragment) fragmentManager.getFragments().get(i_);
                    ((video_adapter)mDownloadFragment.recyclerView.getAdapter()).remove(posRemove);
                }
            }
        });
        return builder.create();
    }
}
