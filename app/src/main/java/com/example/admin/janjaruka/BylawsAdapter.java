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
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.janjaruka.helper.BylawsAsync;

import java.util.ArrayList;

/**
 * Created by Admin on 30/05/2017.
 */

public class BylawsAdapter extends BaseExpandableListAdapter {
    Context context;
    int resource;
    LayoutInflater inflater;
    private ArrayList<Bylaw_item> bylaw_items;
    public BylawsAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<Bylaw_item> bylaw_items) {
        this.context = context;
       this.resource = resource;
        //this.data = data;
        this.bylaw_items = bylaw_items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
/*
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        BylawHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new BylawHolder();

            holder.star_icon = (ImageView) row.findViewById(R.id.star_icon);
            holder.court_case_icon = (ImageView) row.findViewById(R.id.court_case_icon);
            holder.penalty_icon = (ImageView) row.findViewById(R.id.penalty_icon);
            holder.share_icon = (ImageView) row.findViewById(R.id.share_icon);
            holder.bylawText = (TextView) row.findViewById(R.id.bylaw_textview);

            row.setTag(holder);
        } else {
            holder = (BylawHolder) row.getTag();
        }
        Bylaw_item bylaw_item = data[position];
        holder.bylawText.setText(bylaw_item.bylaw_text);
        holder.star_icon.setTag(bylaw_item.bylaw_text);
        holder.court_case_icon.setTag(bylaw_item.bylaw_text);
        holder.penalty_icon.setTag(bylaw_item.bylaw_text);

        holder.star_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Star", Toast.LENGTH_SHORT).show();
            }
        });
        holder.court_case_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Case", Toast.LENGTH_SHORT).show();
            }
        });
        holder.penalty_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Penalty", Toast.LENGTH_SHORT).show();
            }
        });
        holder.share_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show();
            }
        });
        return row;
    } */

    @Override
    public int getGroupCount() {
        return bylaw_items.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return bylaw_items.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return bylaw_items.get(groupPosition).penalty;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(resource, null);
        }
        Bylaw_item bylaw_item = (Bylaw_item) getGroup(groupPosition);
        TextView bylaw_textview = (TextView)convertView.findViewById(R.id.bylaw_textview);

        String bylaw_text = bylaw_item.bylaw_text;
        bylaw_textview.setText(bylaw_text);


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.penalty_view, null);
        }
        String child = (String) getChild(groupPosition, childPosition);
        TextView childTextView = (TextView) convertView.findViewById(R.id.penalty_textview);

        childTextView.setText(child);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
