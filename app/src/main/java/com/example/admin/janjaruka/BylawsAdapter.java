package com.example.admin.janjaruka;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.janjaruka.helper.LawsSQLiteHandler;

import java.util.ArrayList;

/**
 * Created by Admin on 30/05/2017.
 */

public class BylawsAdapter extends BaseExpandableListAdapter {
    LayoutInflater inflater;
    private Context context;
    private int resource;
    private ArrayList<Bylaw_item> bylaw_items;
    private Intent sendIntent, commentIntent;
    private String penalty_text, bylaw_text;
    private  Bylaw_item bylaw_item;
    private ImageView comment_icon, share_icon, favourite_icon, case_icon;

    private LawsSQLiteHandler lawsSQLiteHandler;

    public BylawsAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<Bylaw_item> bylaw_items) {
        this.context = context;
        this.resource = resource;
        this.bylaw_items = bylaw_items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sendIntent = new Intent();
        this.lawsSQLiteHandler = new LawsSQLiteHandler(context);
    }

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
        bylaw_item = (Bylaw_item) getGroup(groupPosition);
        TextView bylaw_textview = (TextView)convertView.findViewById(R.id.bylaw_textview);

        final String bylaw_text = bylaw_item.bylaw_text;
        bylaw_textview.setText(bylaw_text);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.bylaw_childview, null);
        }
        bylaw_item = (Bylaw_item) getGroup(groupPosition);

       // penalty_text = (String) getChild(groupPosition, childPosition);

        //bylaw_text =  bylaw_item.bylaw_text;

        TextView penalty_textView = (TextView) convertView.findViewById(R.id.penalty_textview);

        share_icon = (ImageView)convertView.findViewById(R.id.share_icon);
        share_icon.setTag(bylaw_item.penalty);

        comment_icon = (ImageView)convertView.findViewById(R.id.comment_icon);
        comment_icon.setTag(bylaw_item.bylaw_text);

        favourite_icon = (ImageView)convertView.findViewById(R.id.star_icon);
        favourite_icon.setTag(bylaw_item.favorite);
       // Log.e("Favorite icon", String.valueOf(favourite_icon.getTag()));

        if (bylaw_item.favorite){
            favourite_icon.setImageResource(R.drawable.ic_grade_white_48dp);
        }else {
            favourite_icon.setImageResource(R.drawable.ic_star_black_48dp);
        }
        case_icon = (ImageView)convertView.findViewById(R.id.court_case_icon);

        share_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "@Janjaruka , "+share_icon.getTag());
                sendIntent.setType("text/plain");
                //startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                context.startActivity(sendIntent);
            }
        });
        comment_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentIntent = new Intent(context, CommentActivity.class);
                commentIntent.putExtra("bylaw_text", (String) comment_icon.getTag());
                context.startActivity(commentIntent);
            }
        });
        favourite_icon.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if ((Boolean) v.getTag()){
                    //if true set favorite false and change icon color
                    lawsSQLiteHandler.favorite(bylaw_item.bylaw_id,true);
                    favourite_icon.setImageResource(R.drawable.ic_star_black_48dp);
                    bylaw_item.favorite = false;
                }else {
                    //if false set favorite true and change the icon color
                    lawsSQLiteHandler.favorite(bylaw_item.bylaw_id,false);
                    favourite_icon.setImageResource(R.drawable.ic_grade_white_48dp);
                    bylaw_item.favorite = true;
                }
            }
        });

        penalty_textView.setText(penalty_text);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
