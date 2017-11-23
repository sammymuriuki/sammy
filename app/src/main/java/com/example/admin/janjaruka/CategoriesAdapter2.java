package com.example.admin.janjaruka;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.admin.janjaruka.app.AppController;
import com.example.admin.janjaruka.helper.CategoriesImageUrlArray;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * Created by Admin on 22/11/2017.
 */

class CategoryAdapter2 extends ArrayAdapter<Law_categories> {
    public Law_categories[] data;
    Context context;
    int resource;
    ImageLoader imageLoader = AppController.getmInstance(context).getImageLoader();
    private LayoutInflater inflater;
    private CategoriesImageUrlArray categoriesImageUrlArray;
    private  CategoryIconsAsync categoryIconsAsync;
    public CategoryAdapter2(@NonNull Context context, @LayoutRes int resource, Law_categories[] data) {
        super(context, resource, data);
        this.resource = resource;
        this.context = context;
        this.data = data;
        Log.e(getClass().getName(), data.length+"");
        //  Log.e("LawCategories has", String.valueOf(data.length));
        //imageLoader
        categoriesImageUrlArray = new CategoriesImageUrlArray();
        categoriesImageUrlArray.IMAGES = new String[data.length];
        for(int i=0; i< data.length; i++){
            categoriesImageUrlArray.IMAGES[i] = data[i].category_icon;
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        CategoryHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new CategoryHolder();
            holder.category_icon = (NetworkImageView) row.findViewById(R.id.category_icon);
            holder.category = (TextView) row.findViewById(R.id.law_category);

            row.setTag(holder);
        } else {
            holder = (CategoryHolder) row.getTag();
        }
        Law_categories law_categories = data[position];
        holder.category.setText(law_categories.category_text);
        Log.e(getClass().getName(),"Adding item . . . "+law_categories.category_text) ;

        if (imageLoader == null) {
            imageLoader = AppController.getmInstance(context).getImageLoader();
        }
        // categoryIconsAsync= new CategoryIconsAsync(context);
        //  categoryIconsAsync.execute(law_categories.category_icon, holder.category_icon);
       /* holder.category_icon.setDefaultImageResId(R.drawable.ic_question);
        holder.category_icon.setErrorImageResId(R.drawable.ic_error_black_48dp);
        holder.category_icon.setAdjustViewBounds(true);
        holder.category_icon.setImageUrl(law_categories.category_icon,imageLoader);
        holder.category_icon.setImageUrl(law_categories.category_icon,imageLoader);
       // categoriesImageUrlArray = new CategoriesImageUrlArray(); */
        //     Log.e("iMAGE:",categoriesImageUrlArray.IMAGES[position] );
        imageLoader.get(categoriesImageUrlArray.IMAGES[position], ImageLoader.getImageListener(holder.category_icon, R.drawable.ic_question, R.drawable.ic_error_black_48dp), 60, 60);
        //imageLoader.
//
        return row;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    static class CategoryHolder {
        NetworkImageView category_icon;
        TextView category;
        Integer category_id;
    }
    static class CategoryIconsAsync extends AsyncTask<Object, Void, Bitmap> {
        NetworkImageView cateogyIconv;
        private Context context;
        public CategoryIconsAsync(Context context){
            this.context = context;
        }
        @Override
        protected Bitmap doInBackground(Object... parameters) {
            String URL_OF_IMAGE = (String) parameters[0];
            cateogyIconv = (NetworkImageView) parameters[1];
            Bitmap bitmap = null;
            try{
                InputStream inputStream  = new java.net.URL(URL_OF_IMAGE).openStream();
                bitmap= BitmapFactory.decodeStream(inputStream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

       /* @Override
        protected void onPostExecute(Bitmap bitmap) {


        }*/

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            super.onPostExecute(bitmap);
            cateogyIconv.setImageBitmap(bitmap);Log.e("Bit mape:",cateogyIconv.toString());
        }
    }
}
