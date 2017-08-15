package com.example.admin.janjaruka.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.admin.janjaruka.app.AppConfig.CONNECTION_TIMEOUT;
import static com.example.admin.janjaruka.app.AppConfig.READ_TIMEOUT;
import static com.example.admin.janjaruka.app.AppConfig.URL_BYLAWS;

/**
 * Created by Admin on 23/06/2017.
 */

public class BylawsAsync extends AsyncTask<Void, Void, String> {

    Context context;
    HttpURLConnection httpURLConnection;
    URL url = null;
    ProgressDialog progressDialog;
    INotify iNotify;

    private LawsSQLiteHandler lawsSQLiteHandler;

    public BylawsAsync(Context context, INotify iNotify){
        this.context = context;
        this.lawsSQLiteHandler = new LawsSQLiteHandler(context);
        this.iNotify = iNotify;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            // Enter URL address where your php file resides
            url = new URL(URL_BYLAWS);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(READ_TIMEOUT);
            httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            httpURLConnection.connect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Cannot access the server";
        } catch (IOException e) {
            e.printStackTrace();
        }

        try { // check for connection to the server
            int response_code = httpURLConnection.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {

                // Read data sent from server
                InputStream input = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                // Pass data to onPostExecute method
                return (result.toString());

            } else {
                return "No Connection";
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "Problem loading data";
        } finally {
            httpURLConnection.disconnect();
        }


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        if ((response == "No connection") || (response == "Problem loading data")) {
            Log.e("Bylaws internet", response);
            return;
        }
        if (response == null) {
            Toast.makeText(context, "No information", Toast.LENGTH_LONG);
            return;
        }

        try {
            // convert json string to json array
            JSONArray jsonArray = new JSONArray(response);

            String bylaw_id_str, category_id_str;
            Integer bylaw_id, category_id;
            String bylaw_text, penalty;

            //Create law bylaws array of bylaw object
            int array_size = jsonArray.length();
            Integer bylaw_ids[]= new Integer[array_size];

            for(int i=0; i< jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                bylaw_id_str = jsonObject.getString("bylaw_id");
                bylaw_id = Integer.valueOf(bylaw_id_str);

                category_id_str = jsonObject.getString("category_id");
                category_id = Integer.valueOf(category_id_str);

                bylaw_text = jsonObject.getString("bylaw_text");
                penalty = jsonObject.getString("penalty");

                bylaw_ids[i] = bylaw_id;
                //populate array using json objects

                lawsSQLiteHandler.addBylaws(bylaw_id, category_id, bylaw_text, penalty);

            }

            Log.e("Bylaws Async", "Downloaded data");
            lawsSQLiteHandler.removeBylaws(bylaw_ids);  //Remove Bylaws that are not in the online dfatabase

            iNotify.updateList();

        } catch (JSONException e) {
            Log.e("Bylaws internet", "Check your internet");
            e.printStackTrace();
        }


    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}