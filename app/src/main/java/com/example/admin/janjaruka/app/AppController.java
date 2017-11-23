package com.example.admin.janjaruka.app;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.example.admin.janjaruka.helper.BitmapCache;

import java.io.File;

import static com.example.admin.janjaruka.app.AppConfig.DEFAULT_CACHE_DIR;
import static com.example.admin.janjaruka.app.AppConfig.DEFAULT_DISK_USAGE_BYTES;

/**
 * Created by Admin on 09/06/2017.
 */

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;


    public static synchronized AppController getmInstance(Context applicationContext){
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
            imageLoader =  new ImageLoader(this.requestQueue,new BitmapCache());
        }
        return this.imageLoader;
    }
    public RequestQueue getRequestQueue(){
        if(requestQueue == null ){
            requestQueue = newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    private RequestQueue newRequestQueue(Context applicationContext) {
        RequestQueue queue;
        File rootCache = applicationContext.getExternalCacheDir();
        if (rootCache == null) {

            Log.e("Failed","Can't find External Cache Dir");
            rootCache = applicationContext.getCacheDir();
        }

        File cacheDir = new File(rootCache, DEFAULT_CACHE_DIR);
        cacheDir.mkdirs();

        HttpStack stack = new HurlStack();
        Network network = new BasicNetwork(stack);
        DiskBasedCache diskBasedCache = new DiskBasedCache(cacheDir, DEFAULT_DISK_USAGE_BYTES);
        queue = new RequestQueue(diskBasedCache, network);
        queue.start();

        return queue;
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
/*    public static RequestQueue newRequestQueue()(this){
        // define cache folder
        RequestQueue queue;
        File rootCache = this.getExternalCacheDir();
        if (rootCache == null) {
           *//* Log.e("Can't find External Cache Dir, "
                    + "switching to application specific cache directory");*//*
            rootCache = this.getCacheDir();
        }

        File cacheDir = new File(rootCache, DEFAULT_CACHE_DIR);
        cacheDir.mkdirs();

        HttpStack stack = new HurlStack();
        Network network = new BasicNetwork(stack);
        DiskBasedCache diskBasedCache = new DiskBasedCache(cacheDir, DEFAULT_DISK_USAGE_BYTES);
        queue = new RequestQueue(diskBasedCache, network);
        queue.start();

        return queue;
    }*/
}
