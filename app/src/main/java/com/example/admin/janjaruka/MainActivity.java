package com.example.admin.janjaruka;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.admin.janjaruka.app.AppController;
import com.example.admin.janjaruka.helper.CategoriesAsync;
import com.example.admin.janjaruka.helper.INotify;
import com.example.admin.janjaruka.helper.LawsSQLiteHandler;
import com.example.admin.janjaruka.helper.SQLiteHandler;
import com.example.admin.janjaruka.helper.SessionManager;

import java.util.List;

public class MainActivity extends AppCompatActivity implements INotify{
    private ListView categories_listview;
    private SQLiteHandler sqLiteHandler;
    private SessionManager sessionManager;
    private LawsSQLiteHandler lawsSQLiteHandler;
    private CategoriesAsync categoriesAsync;
    private DrawerLayout drawerLayout;
    private NavDrawerTitle[] navDrawerTitles;
    private ListView drawerListview;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private CategoryAdapter adapter;
    //private CategoriesImageUrlArray categoriesImageUrlArray;

    public static void getString(String s) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqLiteHandler = new SQLiteHandler(getApplicationContext());
        lawsSQLiteHandler = new LawsSQLiteHandler(MainActivity.this);

        // session manager
        sessionManager = new SessionManager(getApplicationContext());
/*
        if (!sessionManager.isLoggedIn()) {
            logoutUser();
        }
        */

        AppController.getmInstance(getApplicationContext());

        categories_listview = (ListView) findViewById(R.id.categories_listview);
        updateList();
        //
        categoriesAsync = new CategoriesAsync(MainActivity.this, this);
        categoriesAsync.execute();


        categories_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent bylaws_intent = new Intent(MainActivity.this, BylawsActivity.class);
                Law_categories selected_law_category = adapter.data[position];
                bylaws_intent.putExtra("category_id", selected_law_category.category_id);
                bylaws_intent.putExtra("category_text", selected_law_category.category_text);
                bylaws_intent.putExtra("category_icon", selected_law_category.category_icon);
                startActivity(bylaws_intent);
            }
        });

        navDrawerTitles = new NavDrawerTitle[]{
                new NavDrawerTitle("Categories"),
                new NavDrawerTitle("Profile"),
                new NavDrawerTitle("Favorites"),
                new NavDrawerTitle("Notifications"),
                new NavDrawerTitle("About"),
                new NavDrawerTitle( "Settings")
        };
        NavDrawerListViewAdapter navDrawerListViewAdapter = new NavDrawerListViewAdapter(MainActivity.this, R.layout.nav_drawer_listview, navDrawerTitles);
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
                        drawerLayout.closeDrawer(drawerListview);
                }
            }
        });
    }

    @Override
    public void updateList() {
        Log.e("List", "Refreshing categories list.. ");

        //Get A list of Law Categories from the SQLite Database
        List<Law_categories> categories = lawsSQLiteHandler.getCategories();

        //Create law categories array of lawcatetegories object
        Law_categories[] data = new Law_categories[categories.size()];
        //

        //loop therough the list and populate the array
        for(int i=0; i<categories.size(); i++){
            data[i] = categories.get(i);
            Log.e(getClass().getName(), "Just adding "+data[i].category_text);
            Log.e(getClass().getName(), "Just adding "+data[i].category_icon);

        }

        adapter = new CategoryAdapter(MainActivity.this, R.layout.law_categories,data);
        categories_listview.setAdapter(adapter);

        Log.e(getClass().getName(), "Adapter has "+adapter.data.length);

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
        // Handle other action bar items...

        return super.onOptionsItemSelected(item);
    }
/*
    private void logoutUser() {
        sessionManager.setLogin(false);

        sqLiteHandler.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    } */
}
