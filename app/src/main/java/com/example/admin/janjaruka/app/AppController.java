package com.example.admin.janjaruka.app;

import android.app.Application;
import com.android.volley.RequestQueue;
import com.android.volley.Request;
import com.android.volley.toolbox.Volley;
/**
 * Created by Admin on 09/06/2017.
 */

public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue requestQueue;
    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
    public static synchronized AppController getmInstance(){
        return mInstance;
    }
    public RequestQueue getRequestQueue(){
        if(requestQueue == null ){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }
    public <T> void addToRequestQueue(Request<T> request){
        request.setTag(TAG);
        getRequestQueue().add(request);
    }
    public void cancelPendingRequest(Object tag){
        if(requestQueue != null){
            requestQueue.cancelAll(tag);
        }
    }
}
