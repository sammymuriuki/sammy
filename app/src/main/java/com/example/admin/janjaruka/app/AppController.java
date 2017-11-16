package com.example.admin.janjaruka.app;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.admin.janjaruka.helper.BitmapCache;

/**
 * Created by Admin on 09/06/2017.
 */

public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    public static synchronized AppController getmInstance(){
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public ImageLoader getImageLoader(){
        getRequestQueue();
        if (imageLoader==null){
            imageLoader = new ImageLoader(this.requestQueue, new BitmapCache());
        }
        return this.imageLoader;
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

    public <T> void addToRequestQueue(Request<T> request, String tag)
    {
        request.setTag((TextUtils.isEmpty(tag) ? TAG : tag));
        getRequestQueue().add(request);
    }

}
