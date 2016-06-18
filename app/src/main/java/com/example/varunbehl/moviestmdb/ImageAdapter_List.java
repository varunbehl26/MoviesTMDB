package com.example.varunbehl.moviestmdb;
/**
 * Created by varunbehl on 02/04/16.
 */

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


class ImageAdapter_List extends ArrayAdapter<Pictures> {

    public ImageAdapter_List(Context context, List<Pictures> objects) {
        super(context, 0, objects);
        this.mContext = context;
        this.movieArrayList = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    List<Pictures> movieArrayList;
    LayoutInflater inflater;
    private final String IMAGE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w342";
    private Context mContext;


    @Override
    public int getCount() {
        return movieArrayList.size();
    }

    @Override
    public Pictures getItem(int position) {
        return movieArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Pictures movie = movieArrayList.get(position);

        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.movie_layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.tvMovieTitle.setText(movie.getTitle());
        Picasso.with(mContext).load(getImageUri(movie.getPosterPath())).into(holder.imgPoster);

        return convertView;
    }


    static class ViewHolder {
        TextView tvMovieTitle;
        ImageView imgPoster;
        CardView cardView;

        public ViewHolder(View itemView) {
            tvMovieTitle = (TextView) itemView.findViewById(R.id.tv_movie_title);
            imgPoster = (ImageView) itemView.findViewById(R.id.img_movie_poster);
            cardView = (CardView) itemView.findViewById(R.id.card_view);

        }
    }


    public String getImageUri(String uri) {
        return IMAGE_POSTER_BASE_URL + "/" + uri;
    }
}