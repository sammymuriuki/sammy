package com.example.admin.janjaruka;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toolbar;

import com.example.admin.janjaruka.fragments.AboutUsFragment;
import com.example.admin.janjaruka.fragments.HomeFragment;
import com.example.admin.janjaruka.helper.CategoriesAsync;
import com.example.admin.janjaruka.helper.INotify;
import com.example.admin.janjaruka.helper.LawsSQLiteHandler;
import com.example.admin.janjaruka.helper.SQLiteHandler;
import com.example.admin.janjaruka.helper.SessionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements INotify{
    private ListView categories_listview;
    private SQLiteHandler sqLiteHandler;
    private SessionManager sessionManager;
    private LawsSQLiteHandler lawsSQLiteHandler;
    private String category_text;
    private int category_icon;
    private Integer category_id=8;
    private CategoriesAsync categoriesAsync;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private View navHeader;
    private ImageView profileImage, navigationHeaderBgImage;
    private NavDrawerTitle[] navDrawerTitles;
    private ListView drawerListview;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private CategoryAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqLiteHandler = new SQLiteHandler(getApplicationContext());
        lawsSQLiteHandler = new LawsSQLiteHandler(MainActivity.this);

        // session manager
        sessionManager = new SessionManager(getApplicationContext());

        if (!sessionManager.isLoggedIn()) {
            logoutUser();
        }



        categories_listview = (ListView) findViewById(R.id.categories_listview);
        notifyDataSetChanged();
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
                new NavDrawerTitle(R.drawable.ic_home_black_48dp, "Home"),
                new NavDrawerTitle(R.drawable.ic_share_black_48dp, "Profile"),
                new NavDrawerTitle(R.drawable.ic_star_black_48dp, "Favorites"),
                new NavDrawerTitle(R.drawable.ic_info_outline_black_48dp, "About"),
                new NavDrawerTitle(R.drawable.ic_share_black_48dp, "Share"),
                new NavDrawerTitle(R.drawable.ic_settings_black_48dp, "Settings")
        };
       // View nav_drawer_header = (View)getLayoutInflater().inflate(R.layout.nav_header_main, null);

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
    public void notifyDataSetChanged() {
        Log.e("List", "Refreshing list.. ");

        //Get A list of Law Categories from the SQLite Database
        List<Law_categories> categories = lawsSQLiteHandler.getCategories();

        //Create law categories array of lawcatetegories object
        Law_categories[] data = new Law_categories[categories.size()];

        //loop therough the list and populate the array
        for(int i=0; i<categories.size(); i++){
            data[i] = categories.get(i);
            Log.e(getClass().getName(), "Just adding "+data[i].category_text);
        }

        adapter = new CategoryAdapter(MainActivity.this, R.layout.law_categories,data);
        categories_listview.setAdapter(adapter);

        Log.e(getClass().getName(), "Adapter has "+adapter.data.length);

    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    /** Swaps fragments in the main content view */
    private void selectItem(int position){
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = new AboutUsFragment();
        Bundle args = new Bundle();
       // args.putInt(AboutUsFragment.);
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

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
    private void logoutUser() {
        sessionManager.setLogin(false);

        sqLiteHandler.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
