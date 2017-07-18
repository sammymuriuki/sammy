package com.example.admin.janjaruka.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

//import com.example.admin.janjaruka.Law_categories;
import com.example.admin.janjaruka.MainActivity;
import com.example.admin.janjaruka.R;

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
import static com.example.admin.janjaruka.app.AppConfig.URL_CATEGORIES;

/**
 * Created by Admin on 23/06/2017.
 */

public class CategoriesAsync extends AsyncTask<Void, Void, String> {

    Context context;
    HttpURLConnection httpURLConnection;
    URL url = null;
    ProgressDialog progressDialog;
    MainActivity mainActivity = new MainActivity();
   // public AsyncResponse delegate = null;
    private LawsSQLiteHandler lawsSQLiteHandler;
/*

    public CategoriesAsync(Context context, AsyncResponse delegate){
        this.context = context;
        this.delegate = delegate;
    }
*/

    public CategoriesAsync(Context context){
        this.context = context;

        this.lawsSQLiteHandler = new LawsSQLiteHandler(context);
    }
    @Override
    protected String doInBackground(Void... params) {
        try {
            // Enter URL address where your php file resides
            url = new URL(URL_CATEGORIES);

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
        /*
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait 1...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show(); */
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        //progressDialog.setMessage(response);
        //progressDialog.dismiss();
        if ((response == "No connection") || (response == "Problem loading data")) {
          //  progressDialog.setMessage(response);
          //  progressDialog.show();
            return;
        }
        if (response == null) {
            Toast.makeText(context, "No information", Toast.LENGTH_LONG);
            return;
        }

        try {
            // convert json string to json array
            JSONArray jsonArray = new JSONArray(response);

            String category_id_str;
            Integer category_id;
            String category_text;
            int category_icon = R.drawable.maysir;
            //Create law categories array of lawtegories object
         //   progressDialog.setMessage("wor");
           // Law_categories category_data[] = new Law_categories[jsonArray.length()];
            int array_size = jsonArray.length();
            Integer category_ids[]= new Integer[array_size];
            for(int i=0; i< jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);


                category_id_str = jsonObject.getString("category_id");
                category_id = Integer.valueOf(category_id_str);
                category_text = jsonObject.getString("category_text");
                category_ids[i] = category_id;
                //populate array using json objects
              // category_data[i] = new Law_categories(category_id, category_text, category_icon);
                lawsSQLiteHandler.addCategories(category_id, category_text, category_icon);
                //categoriesSQLiteHandler.populate_category_ids(category_id);

            }
           //progressDialog.setMessage(category_ids.toString());
            lawsSQLiteHandler.removeCategories(category_ids);

           // delegate.processFinish(category_data);


        } catch (JSONException e) {
            progressDialog.setMessage("Check your internet");
            progressDialog.show();
            e.printStackTrace();
        }


    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}