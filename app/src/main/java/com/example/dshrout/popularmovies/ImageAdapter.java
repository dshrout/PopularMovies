package com.example.dshrout.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.dshrout.popularmovies.movies.MovieCard;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;


public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<MovieCard> mMovies;

    public ImageAdapter(Context c) {
        mContext = c;
        mMovies = new ArrayList<>();
    }

    public boolean add(MovieCard movieCard) {
        mMovies.add(movieCard);
        return true;
    }

    public boolean addAll(MovieCard[] movies) {
        Collections.addAll(mMovies, movies);
        return true;
    }

    public boolean addAll(ArrayList<MovieCard> movies) {
        mMovies.addAll(movies);
        return true;
    }

    public void clear() {
        mMovies.clear();
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Object getItem(int position) {
        return mMovies.get(position);
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

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        if(mMovies.get(position).getPosterPath() != "")
            Picasso.with(mContext).load(mMovies.get(position).getPosterPath()).into(imageView);
        else
            imageView.setImageResource(R.drawable.no_image_found);

        return imageView;
    }
}
