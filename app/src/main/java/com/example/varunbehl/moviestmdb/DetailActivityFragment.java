package com.example.varunbehl.moviestmdb;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    DetailsAdapter detailsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


        Pictures picture = getActivity().getIntent().getExtras().getParcelable("Pictures");

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        detailsAdapter = new DetailsAdapter(picture);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(detailsAdapter);

        return rootView;
    }


    public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {
        public DetailsAdapter(Pictures pic) {

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_detail, parent, false);
            ViewHolder vh = new ViewHolder(v);

            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Pictures picture = getActivity().getIntent().getExtras().getParcelable("Pictures");
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w342" + picture.getPosterPath()).into(holder.iconView);
            holder.title.setText(picture.getTitle());
            holder.releaseDate.setText("Released Date:" + picture.getReleaseDate() + "");
            holder.vote.setText("Rating: " + picture.getVoteAverage() + "/10");
            holder.plotSynopsis.setText(picture.getOverview());
        }

        @Override
        public int getItemCount() {
            return 1;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView iconView;
            TextView title, releaseDate, vote, plotSynopsis;

            public ViewHolder(View itemView) {
                super(itemView);
                iconView = (ImageView) itemView.findViewById(R.id.movie_poster);
                title = (TextView) itemView.findViewById(R.id.title);
                releaseDate = (TextView) itemView.findViewById(R.id.release_date);
                vote = (TextView) itemView.findViewById(R.id.vote);
                plotSynopsis = (TextView) itemView.findViewById(R.id.plot_synopsis);
            }
        }
    }
}
