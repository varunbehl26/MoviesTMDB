package com.example.varunbehl.moviestmdb;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.varunbehl.moviestmdb.db2.MovieDetailContract;
import com.squareup.picasso.Picasso;


class ImageAdapter extends CursorAdapter {

    public ImageAdapter(Context context, Cursor c) {
        super(context, c);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int idx_short_desc = cursor.getColumnIndex(MovieDetailContract.MovieEntry.POSTER_PATH);
        String poster = cursor.getString(idx_short_desc);


        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_START);


        if (!TextUtils.isEmpty(poster)) {
            Picasso.with(context)
                    .load("http://image.tmdb.org/t/p/w342" + MovieDetailContract.MovieEntry.POSTER_PATH)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imageView);
        }
        return imageView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        int idx_short_desc = cursor.getColumnIndex(MovieDetailContract.MovieEntry.POSTER_PATH);
        String poster = cursor.getString(idx_short_desc);


        ImageView imageView;
        if (view == null) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_START);
        } else {
            imageView = (ImageView) view;
        }

        if (!TextUtils.isEmpty(poster)) {
            Picasso.with(context)
                    .load("http://image.tmdb.org/t/p/w342" + poster)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imageView);
        }


    }
}


