package com.example.admin.janjaruka;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.janjaruka.helper.ChatMessage;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class CommentActivity extends AppCompatActivity {
    private static int SIGNIN_REQUEST_CODE =1;
    private DrawerLayout drawerLayout;
    private NavDrawerTitle[] navDrawerTitles;
    private ListView drawerListview;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String bylaw_text;
    private Intent bylaws_itent;
    private TextView comment_bylaw_textview;
    private Button comment_button;
    private FirebaseListAdapter<ChatMessage> adapter;
    private LinearLayout comment_activity ;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGNIN_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Snackbar.make(comment_activity, "Successfully signed in.", Snackbar.LENGTH_SHORT).show();
                displayChatMessage();
            }else {
                Snackbar.make(comment_activity, "Sign in failed.", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_top_menu, menu);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        bylaws_itent = getIntent();
        bylaw_text =bylaws_itent.getStringExtra("bylaw_text");

        comment_bylaw_textview = (TextView) findViewById(R.id.comment_bylaw_textview);
        comment_bylaw_textview.setText(bylaw_text);

        comment_activity = (LinearLayout) findViewById(R.id.comment_activity);
        comment_button = (Button) findViewById(R.id.comment_button);

        comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText comment_input = (EditText) findViewById(R.id.comment_editText);
                FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(comment_input.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                comment_input.setText("");
            }
        });
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGNIN_REQUEST_CODE);
        }else {
            Snackbar.make(comment_activity, "Welcome"+FirebaseAuth.getInstance().getCurrentUser().getEmail(), Snackbar.LENGTH_SHORT).show();
            displayChatMessage();
        }

        navDrawerTitles = new NavDrawerTitle[]{
                new NavDrawerTitle("Categories"),
                new NavDrawerTitle("Profile"),
                new NavDrawerTitle("Favorites"),
                new NavDrawerTitle("Notifications"),
                new NavDrawerTitle("About"),
                new NavDrawerTitle( "Settings")
        };
        NavDrawerListViewAdapter navDrawerListViewAdapter = new NavDrawerListViewAdapter(CommentActivity.this, R.layout.nav_drawer_listview, navDrawerTitles);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerListview = (ListView) findViewById(R.id.navList);
        //drawerListview.addHeaderView(nav_drawer_header);
        drawerListview.setAdapter(navDrawerListViewAdapter);

        mTitle = mDrawerTitle = getTitle();
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close){
            /**  Called when a drawer has settled in a completely close state. **/
            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
            }
            /** Called when a drawer has settled in a completely open state  **/
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
            }
        };
        //Set Drawer toggle as the drawe listener
        drawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position){
                    case 0:
                        intent = new Intent(CommentActivity.this, MainActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(drawerListview);
                        break;
                }
            }
        });
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        //if the nav drawe is open, hide action items related to the content view
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerListview);
        // menu.findItem(R.id.)
        return super.onPrepareOptionsMenu(menu);

    }
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if(item.getItemId() == R.id.menu_signout){
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(comment_activity, "You have been logged out", Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void displayChatMessage() {
        ListView listOfMessage = (ListView) findViewById(R.id.list_of_messages);
        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.message_list, FirebaseDatabase.getInstance().getReference() ) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                 TextView messageText, messageUser, messageTime;
                messageText = (TextView)v.findViewById(R.id.message_text);
                messageUser = (TextView) v.findViewById(R.id.message_user);
                messageTime = (TextView) v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
            }
        };
        listOfMessage.setAdapter(adapter);
    }
}
