package com.example.admin.janjaruka;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.janjaruka.helper.BylawsAsync;
import com.example.admin.janjaruka.helper.LawsSQLiteHandler;
import com.example.admin.janjaruka.helper.SQLiteHandler;
import com.example.admin.janjaruka.helper.SessionManager;

import java.util.List;

public class BylawsActivity extends Activity {
    Intent bylaws_intent;
    String category_text, penalty, bylaw_text;
    Bylaw_item[] bylaws_items;
    private ListView bylaws_listview;
    TextView category_header_text;
    Integer category_id, bylaw_id;
    LawsSQLiteHandler lawsSQLiteHandler;
    SessionManager sessionManager;
    SQLiteHandler sqLiteHandler;
    BylawsAsync bylawsAsync;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bylaws);

        bylawsAsync = new BylawsAsync(BylawsActivity.this);
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
        //Get a list of Bylaws from the sqlite database that match a cer5tain category_id
        List<Bylaw_item> bylaw_items_list = lawsSQLiteHandler.getBylaws(category_id);
        //Create a Bylaws array of Bylaw_item objects as corrected from the sqlite database

        final Bylaw_item bylaw_items_array[] = new Bylaw_item[bylaw_items_list.size()];

        //loop therough the list and populate the array
        for(int i=0; i<bylaw_items_list.size(); i++){
            bylaw_items_array[i] = bylaw_items_list.get(i);
        }
/*
        bylaw_id = 1;

        switch (category_text) {
            case "General":
                bylaws_items = new Bylaw_item[]{
                        new Bylaw_item(bylaw_id, category_id, bylaw_text, penalty),
                        new Bylaw_item(bylaw_id, category_id, "Causing any risk to users through destruction of a building or road", "Penalty1"),
                        new Bylaw_item(bylaw_id, category_id, "Causing risk of users through inadequate fenced or unfenced land, lack of repair," +
                                "protection, removal or enclosure; leaving things around that may  make one to fall or and discharge a missile in or near a street", "Penalty1"),
                        new Bylaw_item(bylaw_id, category_id, "Destroy the surface of a public street", "Penalty1"),
                        new Bylaw_item(bylaw_id, category_id, "Willfully blocking a free passage or removal/displacement of any council property", "Penalty1"),
                        new Bylaw_item(bylaw_id, category_id, "Cutting down a tree without a permit from the council", "Penalty1")
                };
                break;
            case "Category 6":
                bylaws_items = new Bylaw_item[]{
                        new Bylaw_item(bylaw_id, category_id, bylaw_text, penalty),
                        new Bylaw_item(bylaw_id, category_id, "Causing any risk to users through destruction of a building or road", "Penalty1"),
                        new Bylaw_item(bylaw_id, category_id, "Causing risk of users through inadequate fenced or unfenced land, lack of repair," +
                                "protection, removal or enclosure; leaving things around that may  make one to fall or and discharge a missile in or near a street", "Penalty1"),
                        new Bylaw_item(bylaw_id, category_id, "Destroy the surface of a public street", "Penalty1"),
                        new Bylaw_item(bylaw_id, category_id, "Willfully blocking a free passage or removal/displacement of any council property", "Penalty1"),
                        new Bylaw_item(bylaw_id, category_id, "Cutting down a tree without a permit from the council", "Penalty1")
                };
                break;
            default:
                return;

        }
        */

        BylawsAdapter bylawsAdapter = new BylawsAdapter(BylawsActivity.this, R.layout.bylaws_customview, bylaw_items_array);
        bylaws_listview = (ListView)findViewById(R.id.bylaws_listview);
        bylaws_listview.setAdapter(bylawsAdapter);

    }
    private void logoutUser() {
        sessionManager.setLogin(false);

        sqLiteHandler.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(BylawsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}