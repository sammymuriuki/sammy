package com.example.admin.janjaruka;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admin.janjaruka.helper.SQLiteHandler;
import com.example.admin.janjaruka.helper.SessionManager;

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
import static com.example.admin.janjaruka.app.AppConfig.URL_REGISTER;
public class SignUpActivity extends Activity {
    private static final String TAG = SignUpActivity.class.getSimpleName();
    private Button signup_button;
    private EditText first_name_edtTxt, last_name_edtTxt, email_edtTxt, password_edtTxt, confirm_password_edtTxt;
    private Spinner gender_spinner;
    private SessionManager sessionManager;

    Intent mainActivityIntent;
    private AsyncSignup asyncSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        first_name_edtTxt = (EditText) findViewById(R.id.firstname_edtTxt);
        last_name_edtTxt = (EditText) findViewById(R.id.lastname_edtTxt);
        email_edtTxt = (EditText) findViewById(R.id.email_edtTxt);
        gender_spinner = (Spinner) findViewById(R.id.gender_spinner);
        password_edtTxt = (EditText) findViewById(R.id.password_edtTxt);
        password_edtTxt = (EditText) findViewById(R.id.password_edtTxt);
        confirm_password_edtTxt = (EditText) findViewById(R.id.confirm_password_edtTxt);

        signup_button = (Button) findViewById(R.id.sign_up_button);

        sessionManager = new SessionManager(getApplicationContext());
        // SQLite database handler

        if (sessionManager.isLoggedIn()) {
            mainActivityIntent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(mainActivityIntent);
            finish();
        }

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first_name, last_name, email_address, gender, password, confirm_password;
                first_name = first_name_edtTxt.getText().toString().trim();
                last_name = last_name_edtTxt.getText().toString().trim();
                email_address = email_edtTxt.getText().toString().trim().toLowerCase();
                gender = gender_spinner.getSelectedItem().toString().trim();
                password = password_edtTxt.getText().toString();
                confirm_password = confirm_password_edtTxt.getText().toString();
                if (!first_name.isEmpty() && !last_name.isEmpty() && !email_address.isEmpty() && !gender.isEmpty() && !password.isEmpty() && !confirm_password.isEmpty()) {
                    if (!password.equals(confirm_password)) {
                        Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (password.length() < 5) {
                        Toast.makeText(getApplicationContext(), "Password is too short", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (email_address.length() <= 10){
                        Toast.makeText(getApplicationContext(), "Enter correct email address", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // request authentication with remote server
                    asyncSignup = new AsyncSignup(SignUpActivity.this);
                    asyncSignup.execute(first_name, last_name, email_address, gender, password);


                } else {
                    Toast.makeText(getApplicationContext(), "Kindly fill all the details", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private class AsyncSignup extends AsyncTask<String, Void, String> {
        private Context context;
        private HttpURLConnection httpURLConnection;
        private URL url;
        private ProgressDialog progressDialog;
        private SQLiteHandler sqLiteHandler;

        public AsyncSignup(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                // Enter URL address where your php file resides
                url = new URL(URL_REGISTER);

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
                        .appendQueryParameter("first_name", params[0])
                        .appendQueryParameter("last_name", params[1])
                        .appendQueryParameter("email_address", params[2])
                        .appendQueryParameter("gender", params[3])
                        .appendQueryParameter("password", params[4]);
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

                    return "Error registering ";
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "No Connection";
            } finally {
                httpURLConnection.disconnect();
            }
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
            if (!response.equals("No Connection")) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
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
                        sqLiteHandler = new SQLiteHandler(context);
                        sqLiteHandler.addUser(user_id, first_name, last_name, email_address, latitude, longitude, date_created, country, favorites, gender);

                        Toast.makeText(context, "Login now!", Toast.LENGTH_LONG).show();
                        // Launch login activity
                        Intent loginIntent = new Intent(context, LoginActivity.class);
                        startActivity(loginIntent);
                        finish();

                    } else {
                        String errorMsg = jObj.getString("error_message");
                        progressDialog.setMessage(errorMsg);
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    progressDialog.setMessage(e.getMessage());
                }


            } else {
                progressDialog.setMessage("Failed. Check your internet connection");
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}

