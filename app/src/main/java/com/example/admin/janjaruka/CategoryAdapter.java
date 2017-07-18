package com.example.admin.janjaruka;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Admin on 24/05/2017.
 */

public class CategoryAdapter extends ArrayAdapter<Law_categories> {
    Context context;
    int resource;
    Law_categories[] data;

    public CategoryAdapter(@NonNull Context context, @LayoutRes int resource, Law_categories[] data) {
        super(context, resource, data);
        this.resource = resource;
        this.context = context;
        this.data = data;
        Log.e(getClass().getName(), data.length+"");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        CategoryHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new CategoryHolder();
            holder.category_icon = (ImageView) row.findViewById(R.id.category_icon);
            holder.category = (TextView) row.findViewById(R.id.law_category);

            row.setTag(holder);
        } else {
            holder = (CategoryHolder) row.getTag();
        }
        Law_categories law_categories = data[position];
        holder.category.setText(law_categories.category_text);
        Log.e(getClass().getName(),"Adding item . . . "+law_categories.category_text) ;
        holder.category_icon.setImageResource(law_categories.category_icon);
        return row;
    }

    static class CategoryHolder {
        ImageView category_icon;
        TextView category;
        Integer category_id;
    }
}
