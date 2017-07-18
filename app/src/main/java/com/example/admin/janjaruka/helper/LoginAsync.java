package com.example.admin.janjaruka.helper;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.util.Log;

import com.example.admin.janjaruka.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static com.example.admin.janjaruka.app.AppConfig.CONNECTION_TIMEOUT;
import static com.example.admin.janjaruka.app.AppConfig.READ_TIMEOUT;
import static com.example.admin.janjaruka.app.AppConfig.URL_LOGIN;


/**
 * Created by Admin on 14/06/2017.
 */
public class LoginAsync extends AsyncTask<String, Void, String> {
    ProgressDialog progressDialog;
    Context context;
    HttpURLConnection httpURLConnection;
    URL url = null;
    private SessionManager sessionManager;
    private SQLiteHandler sqLiteHandler;

    public LoginAsync(Context context) {
        this.context = context;
        this.sessionManager = new SessionManager(context);
        this.sqLiteHandler = new SQLiteHandler(context);
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];


    //Login
        if (type.equals("login")) {
            try {
                // Enter URL address where your php file resides
                url = new URL(URL_LOGIN);

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Cannot access the server";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(READ_TIMEOUT);
                httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                httpURLConnection.setRequestMethod("POST");



                // setDoInput and setDoOutput method depict handling of both send and receive
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("email_address", params[1])
                        .appendQueryParameter("password", params[2]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                httpURLConnection.connect();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {

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
                return "No Connection";
            } finally {
                httpURLConnection.disconnect();
            }


        }

        return null;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait ...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();


    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        Log.e(getClass().getName(), response);
        if (!response.equals("No Connection")) {
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");

                // Check for error node in json
                if (!error) {
                    // user successfully logged in
                    // Create login session
                    sessionManager.setLogin(true);

                    // Now store the user in SQLite
                    JSONObject user = jObj.getJSONObject("user");
                    String user_id_str = user.getString("user_id");
                    Integer user_id = Integer.valueOf(user_id_str);
                    String first_name = user.getString("first_name");
                    String last_name = user.getString("last_name");
                    String email_address = user.getString("email_address");
                    String latitude_str = user.getString("latitude");
                    Float latitude = Float.valueOf(latitude_str);
                    String longitude_str = user.getString("longitude");
                    float longitude = Float.valueOf(longitude_str);
                    String gender = user.getString("gender");
                    String date_created = user.getString("date_created");
                    String country = user.getString("country");
                    Integer favorites = user.getInt("favorites");

                    // Inserting row in users table -- SQLITE
                    sqLiteHandler.addUser(user_id, first_name, last_name, email_address, latitude, longitude, date_created, country, favorites, gender);

                    progressDialog.setMessage("Successful");
                    // Launch main activity
                    Intent mainActivity_intent = new Intent(context, MainActivity.class);
                    context.startActivity(mainActivity_intent);

                    ((Activity) context).finish();
                } else {
                    // Error in login. Get the error message
                    String errorMsg = jObj.getString("error_message");
                    progressDialog.setMessage(errorMsg);

                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                progressDialog.setMessage(e.getMessage());
            }
        } else {
            progressDialog.setMessage("Failed! Check your internet connection");
        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}