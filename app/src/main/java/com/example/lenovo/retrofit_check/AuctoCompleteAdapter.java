package com.example.lenovo.retrofit_check;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class AuctoCompleteAdapter extends ArrayAdapter<String> {
    Context context;
    int LayourRedTd;
    ArrayList<String> data;
    ArrayList<String> current;
    public AuctoCompleteAdapter(@NonNull Context context, int resource, ArrayList<String> data) {
        super(context, resource);
        this.data=data;
        this.context=context;
        LayourRedTd=resource;
        current=new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_view_row, parent,
                false);
        String data_item=getItem(position);
        AppCompatTextView textViewItem= rowView.findViewById(R.id.textViewItem);
        textViewItem.setText(data_item);
        textViewItem.setTextColor(Color.BLACK);
        return rowView;
    }

    @Override
    public void insert(@Nullable String object, int index) {
        data.add(index, object);
    }

    @Override
    public void remove(@Nullable String object) {
        data.remove(object);
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return current.get(position);
    }

    @Override
    public int getCount() {
        return current.size();
    }

    private Filter mFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length()>0) {
                ArrayList<String> suggestions = new ArrayList<>();
                for (String data_item : data) {
                    // Note: change the "contains" to "startsWith" if you only want starting matches
                    if (data_item.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(data_item);
                    }
                }
                results.values = suggestions;
                results.count = suggestions.size();
            }
            else{
                results.values=(ArrayList<String>) data;
                results.count=data.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results != null && results.count > 0) {
                // we have filtered results
                current=(ArrayList<String>) results.values;
                notifyDataSetChanged();
            } /*else {
                // no filter, add entire original list back in
                Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
                addAll(data);
            }*/
        }
    };

    public Filter getFilter() {
        return mFilter;
    }
}
