package com.example.dshrout.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by DShrout on 7/21/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> mImageUrls;

    public ImageAdapter(Context c) {
        mContext = c;
        mImageUrls = new ArrayList<>();
    }

    public boolean add(String url) {
        mImageUrls.add(url);
        return true; // TODO: add error handling
    }

    public boolean addAll(String[] urls) {
        for(int i = 0; i < urls.length; ++i) {
            mImageUrls.add(urls[i]);
        }
        return true; // TODO: add error handling
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
