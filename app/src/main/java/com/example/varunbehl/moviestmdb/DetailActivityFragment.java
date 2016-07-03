package com.example.varunbehl.moviestmdb;

import android.support.v4.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.varunbehl.moviestmdb.db2.MovieDetailContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Video.VideoResult;
import Video.Videos;
import Reviews.*;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public static final String TAG = DetailActivityFragment.class.getSimpleName();
    static final String DETAIL_MOVIE = "DETAIL_MOVIE";

    static String API_KEY = "29c90a4aee629499a2149041cc6a0ffd";
    Videos videos = new Videos();
    Reviews review = new Reviews();
    Pictures picture = new Pictures();
    DetailsAdapter detailsAdapter;
    View rootMovieView, rootView;
    RetrofitManager retrofitManager;
    TextView review_text, trailer_text;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        retrofitManager = RetrofitManager.getInstance();
        rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        rootMovieView = inflater.inflate(R.layout.movie_detail, container, false);

        review_text = (TextView) rootView.findViewById(R.id.review_text);
        trailer_text = (TextView) rootView.findViewById(R.id.trailer_text);

        Bundle arguments = getArguments();

        if (arguments != null) {
            picture = arguments.getParcelable(DetailActivityFragment.DETAIL_MOVIE);
        } else {
            picture = (Pictures) getActivity().getIntent().getExtras().get(DetailActivityFragment.DETAIL_MOVIE);
        }


        getVideosData();
        getReviewsData();

        detailsAdapter = new DetailsAdapter(picture);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(detailsAdapter);

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    public void getVideosData() {

        Observable<Videos> videosObservable = retrofitManager.getTrailer(picture.getId(), API_KEY);

        videosObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Videos>() {
                    @Override
                    public void onCompleted() {
                        trailer_text.setText("Trailers");

                        LinearLayout review_layout = (LinearLayout) rootView.findViewById(R.id.video_trailer);
                        for (final VideoResult videoResult : videos.getResults()) {
                            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_movie_trailer,
                                    null);
                            Button b1 = (Button) view.findViewById(R.id.b1);
                            b1.setText(videoResult.getName());
                            b1.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoResult.getKey())));
                                }
                            });
                            review_layout.addView(view);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // handle error
                    }

                    @Override
                    public void onNext(Videos vid) {
                        videos = vid;

                        Log.v("videosList", videos.toString() + "");
                    }
                });
    }


    public void getReviewsData() {

        Observable<Reviews> reviewsObservable = retrofitManager.getComments(picture.getId(), API_KEY);

        reviewsObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Reviews>() {
                    @Override
                    public void onCompleted() {
                        review_text.setText("Reviews");

                        LinearLayout review_layout = (LinearLayout) rootView.findViewById(R.id.review_comment);


                        for (ReviewsResult comment : review.getReviewsResults()) {

                            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_movie_comments,
                                    null);

                            TextView tvCommentBy = (TextView) view.findViewById(R.id.tv_comment_by);

                            TextView tvCommentContent = (TextView) view.findViewById(R.id.tv_comment_content);


                            tvCommentContent.setText(comment.getContent());
                            tvCommentBy.setText(comment.getAuthor());

                            review_layout.addView(view);
                        }
                    }


                    @Override
                    public void onError(Throwable e) {
                        review_text.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onNext(Reviews reviews) {
                        review = reviews;
                    }
                });
    }


    public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {
        public DetailsAdapter(Pictures pictures) {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_detail, parent, false);
            ViewHolder vh = new ViewHolder(v);

            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w342" + picture.getPosterPath()).into(holder.iconView);
            holder.title.setText(picture.getTitle());
            holder.releaseDate.setText("Released Date:" + picture.getReleaseDate() + "");
            holder.vote.setText("Rating: " + picture.getVoteAverage() + "/10");
            holder.plotSynopsis.setText(picture.getOverview());

            holder.fav_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Pictures> movieList = new ArrayList<Pictures>();
                    Uri movieDataUri = MovieDetailContract.MovieEntry.buildMovieUri();

                    Cursor cur = getActivity().getContentResolver().query(movieDataUri,
                            null, null, null, null);
                    setDataInDb(picture);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 1;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView iconView;
            TextView title, releaseDate, vote, plotSynopsis;
            Button fav_button;

            public ViewHolder(View itemView) {
                super(itemView);
                iconView = (ImageView) itemView.findViewById(R.id.movie_poster);
                title = (TextView) itemView.findViewById(R.id.title);
                releaseDate = (TextView) itemView.findViewById(R.id.release_date);
                vote = (TextView) itemView.findViewById(R.id.vote);
                plotSynopsis = (TextView) itemView.findViewById(R.id.plot_synopsis);
                fav_button = (Button) itemView.findViewById(R.id.b11);

            }
        }

        private void setDataInDb(Pictures pictures) {
            ContentValues movieValues = new ContentValues();
            movieValues.put(MovieDetailContract.MovieEntry.MOVIE_ID, pictures.getId());
            movieValues.put(MovieDetailContract.MovieEntry.POSTER_PATH, pictures.getPosterPath());
            movieValues.put(MovieDetailContract.MovieEntry.OVERVIEW, pictures.getOverview());
            movieValues.put(MovieDetailContract.MovieEntry.RELEASE_DATE, pictures.getReleaseDate());
            movieValues.put(MovieDetailContract.MovieEntry.ORIGINAL_TITLE, pictures.getOriginalTitle());
            movieValues.put(MovieDetailContract.MovieEntry.ORIGINAL_LANGUAGE, pictures.getOriginalLanguage());
            movieValues.put(MovieDetailContract.MovieEntry.BACKDROP_PATH, pictures.getBackdropPath());
            movieValues.put(MovieDetailContract.MovieEntry.ADULT, pictures.getAdult());
            movieValues.put(MovieDetailContract.MovieEntry.VIDEO, pictures.getVideo());
            movieValues.put(MovieDetailContract.MovieEntry.POPULARITY, pictures.getPopularity());
            movieValues.put(MovieDetailContract.MovieEntry.VOTE_AVERAGE, pictures.getVoteAverage());
            movieValues.put(MovieDetailContract.MovieEntry.VOTE_COUNT, pictures.getVoteCount());

            getContext().getContentResolver().insert(MovieDetailContract.MovieEntry.CONTENT_URI, movieValues);
        }

    }
}