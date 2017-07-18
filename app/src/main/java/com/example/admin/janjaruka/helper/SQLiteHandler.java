package com.example.admin.janjaruka.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Admin on 09/06/2017.
 */

public class SQLiteHandler extends SQLiteOpenHelper {
    private static final String TAG = SQLiteHandler.class.getSimpleName();
    //database version
    private static final int DATABASE_VERSION = 1;
    //database name
    private static final String DATABASE_NAME = "janjaruka";
    //login table name
    private static final String TABLE_USER = "users";

    //login table column names
    //private static final String KEY_USER_ID = "user_id";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_EMAIL_ADDRESS = "email_address";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_DATE_CREATED = "date_created";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_FAVORITES = "favorites";
    private static final String KEY_GENDER = "gender";
    /*
    public SQLiteHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
*/

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create Tables
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_FIRST_NAME + " TEXT, "
                + KEY_LAST_NAME + " TEXT, "
                + KEY_EMAIL_ADDRESS + " TEXT UNIQUE, "
                + KEY_LATITUDE + " NUMERIC, "
                + KEY_LONGITUDE + " NUMERIC,"
                + KEY_DATE_CREATED + " TEXT, "
                + KEY_COUNTRY + " TEXT, "
                + KEY_FAVORITES + " INTEGER, "
                + KEY_GENDER + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
        Log.d(TAG, "Database tables created");
    }

    //OOn upgradde database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop oldertable if t existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        //create tables again
        onCreate(db);
    }

    /**
     * Storing usre dartails in database
     */
    public void addUser(Integer user_id, String first_name, String last_name, String email_address, Float latitude, Float longitude, String date_created, String country, Integer favorites, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FIRST_NAME, first_name);
        values.put(KEY_LAST_NAME, last_name);
        values.put(KEY_EMAIL_ADDRESS, email_address);
        values.put(KEY_LATITUDE, latitude);
        values.put(KEY_LONGITUDE, longitude);
        values.put(KEY_DATE_CREATED, date_created);
        values.put(KEY_COUNTRY, country);
        values.put(KEY_FAVORITES, favorites);
        values.put(KEY_GENDER, gender);

        //inserting row
        Long id = db.insert(TABLE_USER, null, values);

        db.close(); //close dtatabase connection
        Log.d(TAG, "Account created. " + id);
    }

    /**
     * Getting user datat from database
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("first_name", cursor.getString(1));
            user.put("last_name", cursor.getString(2));
            user.put("email_address", cursor.getString(3));
            user.put("longitude", cursor.getString(4));
            user.put("latitude", cursor.getString(5));
            user.put("date_created", cursor.getString(6));
            user.put("country", cursor.getString(7));
            user.put("favorites", cursor.getString(8));
            user.put("gender", cursor.getString(9));
        }
        cursor.close();
        db.close();
        //return user
        Log.d(TAG, "Fetching user " + user.toString());
        return user;
    }

    /**
     * Recreate database ... delete all ables an create them again
     */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        //Delete all rows
        db.delete(TABLE_USER, null, null);
        db.close();
        Log.d(TAG, "Deleted all user info.");
    }

}
