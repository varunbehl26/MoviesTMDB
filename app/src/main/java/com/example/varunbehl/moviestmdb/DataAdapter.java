package com.example.varunbehl.moviestmdb;
/**
 * Created by varunbehl on 02/04/16.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private final String IMAGE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w342";
    private List<Pictures> movieArrayList;
    private LayoutInflater inflater;
    private Context mContext;

    DataAdapter(Context context, List<Pictures> objects) {
        this.mContext = context;
        this.movieArrayList = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = inflater.inflate(R.layout.movie_layout, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, final int position) {
        holder.tvMovieTitle.setText(movieArrayList.get(position).getTitle());
        holder.draweeView.setImageURI(getImageUri(movieArrayList.get(position).getPosterPath()));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class)
                        .putExtra(DetailActivityFragment.DETAIL_MOVIE, movieArrayList.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }


    private String getImageUri(String uri) {
        return IMAGE_POSTER_BASE_URL + "/" + uri;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMovieTitle;
        CardView cardView;
        SimpleDraweeView draweeView;

        ViewHolder(View itemView) {
            super(itemView);
            tvMovieTitle = (TextView) itemView.findViewById(R.id.tv_movie_title);
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.img_movie_poster);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }
}