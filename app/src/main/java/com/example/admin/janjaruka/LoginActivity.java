package com.example.admin.janjaruka;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.janjaruka.helper.LoginAsync;
import com.example.admin.janjaruka.helper.SessionManager;

public class LoginActivity extends Activity {
    private Intent intent_signup, mainActivityIntent;
    private Button sign_up_button, sign_in_button;
    private EditText email_address_edtTxt, password_edtTxt;
    String email_address, password;
    LoginAsync loginAsync;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Session manager
        sessionManager = new SessionManager(getApplicationContext());
        // Check if user is already logged in or not

        if (sessionManager.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainActivityIntent);
            finish();
        }

        sign_up_button = (Button) findViewById(R.id.sign_up_button);
        sign_in_button = (Button) findViewById(R.id.sign_in_button);
        email_address_edtTxt = (EditText) findViewById(R.id.email_address_edtTxt);
        password_edtTxt = (EditText) findViewById(R.id.password_edtTxt);


        intent_signup = new Intent(this, SignUpActivity.class);
        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent_signup);
            }
        });
        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_address = email_address_edtTxt.getText().toString().toLowerCase().trim();
                password = password_edtTxt.getText().toString().trim();
                if (email_address.equals("") || password.equals("") || email_address.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Fill all the fields", Toast.LENGTH_LONG).show();
                    return;
                }
                String type = "login";

                // request authentication with remote server
                loginAsync = new LoginAsync(LoginActivity.this);
                loginAsync.execute(type, email_address, password);

            }
        });
    }

}