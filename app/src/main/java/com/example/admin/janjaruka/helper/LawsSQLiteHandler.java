package com.example.admin.janjaruka.helper;

//import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.admin.janjaruka.Bylaw_item;
import com.example.admin.janjaruka.Law_categories;

import java.util.ArrayList;
import java.util.List;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE;

/**
 * Created by Admin on 29/06/2017.
 */

public class LawsSQLiteHandler extends SQLiteOpenHelper {
    private static final String TAG = SQLiteHandler.class.getSimpleName();
    //database version
    private static final int DATABASE_VERSION = 3;
    //database name
    private static final String DATABASE_NAME = "janjaruka_laws";
    //categories table name
    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_BYLAWS = "bylaws";

    //Fiels in the categories table
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_CATEGORY_TEXT = "category_text";
    private static final String KEY_CATEGORY_ICON = "category_icon";

    //Fields in the bylaws table;
    private static final String KEY_BYLAW_ID = "bylaw_id";
    private static final String KEY_BYLAW_TEXT = "bylaw_text";
    private static final String KEY_PENALTY = "penalty";
    private static final String KEY_BYLAW_CATEGORY_ID = "bylaw_category_id";
    private static final String KEY_FAVORITE = "favorite";

    Context context;
    private Integer category_icon_integer;
    private String  category_icon_str;
    private SQLiteDatabase db;
    private ContentValues values;
    public LawsSQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.values = new ContentValues();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_BYLAWS_TABLE = "CREATE TABLE IF NOT EXISTS "+ TABLE_BYLAWS + "("
                +KEY_BYLAW_ID + " INTEGER PRIMARY KEY, "
                +KEY_BYLAW_CATEGORY_ID +" TEXT, "
                +KEY_BYLAW_TEXT +" TEXT, "
                +KEY_PENALTY +" TEXT, "
                +KEY_FAVORITE +" TEXT"
                + ")";

        String CREATE_CATEGORIES_TABLE = "CREATE TABLE "+ TABLE_CATEGORIES + "("
                +KEY_CATEGORY_ID + " INTEGER PRIMARY KEY, "
                +KEY_CATEGORY_TEXT +" TEXT, "
                +KEY_CATEGORY_ICON +" TEXT "
                + ")";

        db.execSQL(CREATE_BYLAWS_TABLE);
        db.execSQL(CREATE_CATEGORIES_TABLE);

        Log.d(TAG, "Database tables created");
    }

    //Insert category details into the database
    public void addCategories(Integer category_id, String category_text, String category_icon){
        db = this.getWritableDatabase();

        category_icon_str=category_icon;

        String INSERT_OR_REPLACE_CATEGORIES = "INSERT OR REPLACE INTO "+TABLE_CATEGORIES+" ("
                +KEY_CATEGORY_ID+", "+KEY_CATEGORY_TEXT+", "+KEY_CATEGORY_ICON+" ) VALUES ('"
                +category_id.toString()+"', '"+category_text+"', '"+category_icon_str+"' ) ";
        db.execSQL(INSERT_OR_REPLACE_CATEGORIES);
        db.close(); //close database connection

    }
    public void addBylaws(Integer bylaw_id,Integer category_id, String category_text, String penalty){
        db = this.getWritableDatabase();
        values.put(KEY_BYLAW_ID, bylaw_id.toString());
        values.put(KEY_BYLAW_CATEGORY_ID, category_id.toString());
        values.put(KEY_BYLAW_TEXT, category_text);
        values.put(KEY_PENALTY,penalty);
        values.put(KEY_FAVORITE, false);

        db.insertWithOnConflict(TABLE_BYLAWS, null, values, CONFLICT_REPLACE);

        db.close();
    }
    public void favorite(Integer bylaw_id, Boolean favorite){
        db = this.getWritableDatabase();
        String statusTest = "";
        ///Change the value of favorite
        String update_bylaw = "UPDATE "+TABLE_BYLAWS+" SET "+KEY_FAVORITE+" = '"+favorite+"' WHERE "+KEY_BYLAW_ID+" = '"+bylaw_id+"'";
         db.execSQL(update_bylaw);
        Log.e("Favorite", "Favarite updated to "+favorite);
        if (favorite){
            statusTest = "Unfavorited";
        }else {
            statusTest ="Favorited";
        }

        Toast.makeText(context, statusTest, Toast.LENGTH_SHORT).show();
    }
    //when a category is not in the database online remove it from the sqlite database
    public void removeCategories(Integer[] category_ids){
        db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM "+TABLE_CATEGORIES+"";   //selet all details in the sqlite databse

        Cursor cursor = db.rawQuery(selectQuery, null);
        List <Integer> allCategories = new ArrayList<Integer>();
        if(cursor.moveToFirst()){
            do{
                Integer category_id_in_table = Integer.valueOf(cursor.getString(0));
                allCategories.add(category_id_in_table);                //add all the category ids that are in the table to an arraylist
            }while (cursor.moveToNext());
        }

        for (int i=0; i< category_ids.length; i++){
            allCategories.remove(category_ids[i]);   //remove all categories in the table matching with the ones that are online and leave non-matching ones
        }

        for (int i = 0; i<allCategories.size(); i++){
            String REMOVE_CATEGORIES = "DELETE FROM "+TABLE_CATEGORIES+" WHERE "+KEY_CATEGORY_ID+" = '"+allCategories.get(i)+"'"; //remove the non-matching categories
            db.execSQL(REMOVE_CATEGORIES);
        }

    }
    //when a bylaw is not in the database online remove it from the sqlite database
    public void removeBylaws(Integer[] bylaw_ids){
        db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM "+TABLE_BYLAWS+"";   //selet all details in the sqlite databse

        Cursor cursor = db.rawQuery(selectQuery, null);
        List <Integer> allBylaws = new ArrayList<Integer>();
        if(cursor.moveToFirst()){
            do{
                Integer bylaw_id_in_table = Integer.valueOf(cursor.getString(0));
                //add all the bylaw_ids that are in the table to an arraylist
                allBylaws.add(bylaw_id_in_table);
            }while (cursor.moveToNext());
        }

        for (int i=0; i< bylaw_ids.length; i++){
            //remove all bylaws in the table matching with the ones that are online and leave non-matching ones
            allBylaws.remove(bylaw_ids[i]);
        }

        for (int i = 0; i<allBylaws.size(); i++){
            //remove the non-matching bylaws
            String REMOVE_BYLAWS = "DELETE FROM "+TABLE_BYLAWS+" WHERE "+KEY_BYLAW_ID+" = '"+allBylaws.get(i)+"'";
            db.execSQL(REMOVE_BYLAWS);
        }

    }

    public List<Law_categories> getCategories(){

        List<Law_categories> categories_list = new ArrayList<Law_categories>();
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping throught all; rows and adding to list

        if(cursor.moveToFirst()){
            do{
                Law_categories law_categories = new Law_categories(Integer.valueOf(cursor.getString(0)),cursor.getString(1),cursor.getString(2));
                //add category to list  */
                categories_list.add(law_categories);

            }while (cursor.moveToNext());

        }
        //return categories list
        return categories_list;
    }

    public List<Bylaw_item> getBylaws(Integer category_id){
        List<Bylaw_item> bylaws_list = new ArrayList<Bylaw_item>();
        String selectQuery = "SELECT * FROM " +TABLE_BYLAWS+" WHERE "+KEY_BYLAW_CATEGORY_ID+" = '"+category_id.toString()+"'";
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping throught all; rows and adding to list
        if(cursor.moveToFirst()){
            do{
                Bylaw_item bylaw_item = new Bylaw_item();
                bylaw_item.bylaw_id = Integer.valueOf(cursor.getString(0));
                bylaw_item.category_id= Integer.valueOf(cursor.getString(1));
                bylaw_item.bylaw_text = cursor.getString(2);
                bylaw_item.penalty = cursor.getString(3);
                bylaw_item.favorite = Boolean.valueOf(cursor.getString(4));
                //add bylaw_object to list
                bylaws_list.add(bylaw_item);
            }while (cursor.moveToNext());

        }
        //return bylaws list
        return bylaws_list;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop oldertable if t existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BYLAWS);
        //create tables again
        onCreate(db);
    }
    /**
     * Recreate database ... delete all tables an create them again
     */
    public void deleteAllCategories() {
        db = this.getWritableDatabase();
        //Delete all rows
        db.delete(TABLE_CATEGORIES, null, null);
        db.close();
        Log.d(TAG, "Deleted all info.");
    }
    public void deleteAllBylaws() {
        db = this.getWritableDatabase();
        //Delete all rows
        db.delete(TABLE_BYLAWS, null, null);
        db.close();
        Log.d(TAG, "Deleted all info.");
    }
}
