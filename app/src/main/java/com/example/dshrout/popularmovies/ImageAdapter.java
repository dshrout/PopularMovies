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
    private ArrayList<Movie> mMovies;

    public ImageAdapter(Context c) {
        mContext = c;
        mMovies = new ArrayList<>();
    }

    public boolean add(Movie movie) {
        mMovies.add(movie);
        return true;
    }

    public boolean addAll(Movie[] movies) {
        Collections.addAll(mMovies, movies);
        return true;
    }

    public boolean addAll(ArrayList<Movie> movies) {
        /*for(int i = 0; i < movies.size(); ++i) {
            String url;
            url =  "http://image.tmdb.org/t/p/w342/" + movies.get(i).getPosterPath();
            mMovies.add(url);
        } */
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
            imageView.setImageResource(R.drawable.sample_2);

        return imageView;
    }
}
