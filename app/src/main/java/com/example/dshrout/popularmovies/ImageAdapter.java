package com.example.dshrout.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.dshrout.popularmovies.movies.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> mImageUrls;

    public ImageAdapter(Context c) {
        mContext = c;
        mImageUrls = new ArrayList<>();
    }

    public boolean add(String url) {
        mImageUrls.add(url);
        return true;
    }

    public boolean addAll(String[] urls) {
        Collections.addAll(mImageUrls, urls);
        return true;
    }

    public boolean addAll(ArrayList<Movie> movies) {
        for(int i = 0; i < movies.size(); ++i) {
            String url;
            url =  "http://image.tmdb.org/t/p/w342/" + movies.get(i).getPosterPath();
            mImageUrls.add(url);
        }
        return true;
    }

    public void clear() {
        mImageUrls.clear();
    }

    @Override
    public int getCount() {
        return mImageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView == null) {
            imageView = new ImageView(mContext);
        } else {
            imageView = (ImageView)convertView;
        }

        Picasso.with(mContext).load(mImageUrls.get(position)).into(imageView);
        return imageView;
    }
}
