package com.example.varunbehl.moviestmdb;
/**
 * Created by varunbehl on 02/04/16.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import Video.VideoResult;

class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private final String IMAGE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w342";
    private List<VideoResult> videosList;
    private LayoutInflater inflater;
    private Context mContext;

    VideoAdapter(Context context, List<VideoResult> objects) {
        this.mContext = context;
        this.videosList = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = inflater.inflate(R.layout.movie_layout, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(VideoAdapter.ViewHolder holder, final int position) {
        holder.tvMovieTitle.setText(videosList.get(position).getName());
        holder.draweeView.setImageURI("http://img.youtube.com/vi/" + videosList.get(position).getKey() + "/0.jpg");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videosList.get(position).getKey())));
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return videosList.size();
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