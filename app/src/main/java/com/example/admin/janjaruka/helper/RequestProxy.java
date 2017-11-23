package com.example.admin.janjaruka.helper;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Admin on 22/11/2017.
 */
public class RequestProxy {

    private RequestQueue requestQueue;

    // package access constructor
    RequestProxy(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }
    public void login() {
        // login request
    }

    public void weather() {
        // weather request
    }
}
