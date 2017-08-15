package com.example.admin.janjaruka;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Admin on 06/07/2017.
 */

public class NavDrawerListViewAdapter extends ArrayAdapter<NavDrawerTitle> {
    Context context;
    int resource;
    NavDrawerTitle[] data;

    public NavDrawerListViewAdapter(@NonNull Context context, @LayoutRes int resource, NavDrawerTitle[] data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        NavDrawerListViewAdapter.NavDrawerHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new NavDrawerListViewAdapter.NavDrawerHolder();
            holder.title_text= (TextView) row.findViewById(R.id.nav_drawer_menu_title);

            row.setTag(holder);
        } else {
            holder = (NavDrawerListViewAdapter.NavDrawerHolder) row.getTag();
        }
        NavDrawerTitle navDrawerTitle = data[position];
        holder.title_text.setText(navDrawerTitle.title_text);
        return row;
    }

    static class NavDrawerHolder {

        TextView title_text;
    }
}
