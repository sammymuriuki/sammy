package com.example.admin.janjaruka.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * Created by Admin on 09/06/2017.
 */


public class SessionManager {
    //Log CAT
    private static String TAG = SessionManager.class.getSimpleName();
    //Shared preferences
    SharedPreferences preferences;
    Editor editor;
    Context context;

    //shared pref mode
    int PRIVATE_MODE = 0;

    //shared prefernces filename
    private static final String PREF_NAME = "Login";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    public SessionManager(Context context){
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }
    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }
    public boolean isLoggedIn(){
        return preferences.getBoolean(KEY_IS_LOGGEDIN, false);
    }

}
