package com.example.varunbehl.moviestmdb;
/**
 * Created by varunbehl on 02/04/16.
 */

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;


class ImageAdapter_List extends ArrayAdapter<Pictures> {

    public ImageAdapter_List(Context context, List<Pictures> objects) {
        super(context, 0, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Pictures pictures = getItem(position);
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_START);
        } else {
            imageView = (ImageView) convertView;
        }

        if (!TextUtils.isEmpty(pictures.getPosterPath())) {
            Picasso.with(getContext())
                    .load("http://image.tmdb.org/t/p/w342" + pictures.getPosterPath())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imageView);
        }
        return imageView;
    }
}