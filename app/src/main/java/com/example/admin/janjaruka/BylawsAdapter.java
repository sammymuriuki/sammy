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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.janjaruka.helper.BylawsAsync;

/**
 * Created by Admin on 30/05/2017.
 */

public class BylawsAdapter extends ArrayAdapter<Bylaw_item> {
    Context context;
    int resource;
    Bylaw_item[] data;
    BylawsAsync bylawsAsync;

    public BylawsAdapter(@NonNull Context context, @LayoutRes int resource, Bylaw_item[] data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

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
    }

    static class BylawHolder {
        ImageView star_icon, share_icon, court_case_icon, penalty_icon;
        TextView bylawText;
    }
}
