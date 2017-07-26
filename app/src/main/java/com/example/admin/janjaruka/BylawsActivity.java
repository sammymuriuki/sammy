package com.example.admin.janjaruka;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.janjaruka.helper.BylawsAsync;
import com.example.admin.janjaruka.helper.INotify;
import com.example.admin.janjaruka.helper.LawsSQLiteHandler;
import com.example.admin.janjaruka.helper.SQLiteHandler;
import com.example.admin.janjaruka.helper.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class BylawsActivity extends Activity implements INotify{
    Intent bylaws_intent;
    String category_text, penalty, bylaw_text;
    Bylaw_item[] bylaws_items;
    private ExpandableListView bylaws_listview;
    TextView category_header_text;
    Integer category_id, bylaw_id;
    LawsSQLiteHandler lawsSQLiteHandler;
    SessionManager sessionManager;
    SQLiteHandler sqLiteHandler;
    BylawsAsync bylawsAsync;
    BylawsAdapter bylawsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bylaws);


        bylawsAsync = new BylawsAsync(BylawsActivity.this, this);
        bylawsAsync.execute();


        bylaws_intent = getIntent();
        category_text = bylaws_intent.getStringExtra("category_text");
        category_id = bylaws_intent.getIntExtra("category_id", 1);
        Toast.makeText(getApplicationContext(), category_text, Toast.LENGTH_SHORT).show();
        category_header_text =(TextView)findViewById(R.id.category_header_text);
        category_header_text.setText(category_text);
        sqLiteHandler = new SQLiteHandler(getApplicationContext());
        //SQLiteHandler
        lawsSQLiteHandler = new LawsSQLiteHandler(BylawsActivity.this);
        // session manager
        sessionManager = new SessionManager(getApplicationContext());
        if (!sessionManager.isLoggedIn()) {
            logoutUser();
        }

        bylaws_listview = (ExpandableListView) findViewById(R.id.bylaws_listview);
        updateList();


    }
    private void logoutUser() {
        sessionManager.setLogin(false);

        sqLiteHandler.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(BylawsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void updateList() {
        Log.e("Bylaws", "Refreshing list ... ");

        //Get a list of Bylaws from the sqlite database that match a cer5tain category_id
        List<Bylaw_item> bylaw_items_list = lawsSQLiteHandler.getBylaws(category_id);

        //Create a Bylaws array of Bylaw_item objects as corrected from the sqlite database
        final ArrayList<Bylaw_item> bylaw_items_arraylist = new ArrayList<Bylaw_item>();

      //  Log.e("Bylaws", "The list has ... "+bylaw_items_list.size()+" items");

        //loop through the list and populate the array
        for(int i=0; i<bylaw_items_list.size(); i++){
            bylaw_items_arraylist.add(bylaw_items_list.get(i));
            Log.e(getClass().getName(), "Just adding "+bylaw_items_arraylist.get(i));
        }

        bylawsAdapter = new BylawsAdapter(BylawsActivity.this, R.layout.bylaws_customview, bylaw_items_arraylist);
        bylaws_listview.setAdapter(bylawsAdapter);

    }
}

