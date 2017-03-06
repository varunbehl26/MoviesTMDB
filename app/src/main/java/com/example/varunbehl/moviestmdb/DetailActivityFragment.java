package com.example.varunbehl.moviestmdb;

import android.content.ContentValues;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.varunbehl.moviestmdb.db2.MovieDetailContract;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import Reviews.Reviews;
import Reviews.ReviewsResult;
import Video.Videos;
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
    public EventBus eventBus;
    Videos videos = new Videos();
    Reviews review = new Reviews();
    Pictures picture = new Pictures();
    View rootMovieView, rootView;
    RetrofitManager retrofitManager;
    TextView review_text, trailer_text;
    TextView title, releaseDate, vote, plotSynopsis;
    Button fav_button;
    SimpleDraweeView draweeView;
    CollapsingToolbarLayout collapsingToolbar;
    Drawable image;
    private VideoAdapter dataAdapter;
    private HorizontalGridView gridView;


    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        retrofitManager = RetrofitManager.getInstance();
        rootView = inflater.inflate(R.layout.fragment_detail, container, false);
//        rootMovieView = inflater.inflate(R.layout.movie_detail, container, false);
        collapsingToolbar = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsingToolbar);
//        setSupportActionBar(collapsingToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gridView = (HorizontalGridView) rootView.findViewById(R.id.video_trailer);

//        dataAdapter = new VideoAdapter(getActivity(), null);

        eventBus = EventBus.getDefault();
        eventBus.register(this);
        review_text = (TextView) rootView.findViewById(R.id.review_text);
        trailer_text = (TextView) rootView.findViewById(R.id.trailer_text);
        title = (TextView) rootView.findViewById(R.id.title);
        releaseDate = (TextView) rootView.findViewById(R.id.release_date);
        vote = (TextView) rootView.findViewById(R.id.vote);
        plotSynopsis = (TextView) rootView.findViewById(R.id.plot_synopsis);
        fav_button = (Button) rootView.findViewById(R.id.b11);
        draweeView = (SimpleDraweeView) rootView.findViewById(R.id.movie_poster);
        fav_button.setBackground(getContext().getResources().getDrawable(R.drawable.unfav));
        new LoadDetailPageThread(1).start();

        Bundle arguments = getArguments();

        try {
            if (arguments != null) {
                picture = arguments.getParcelable(DetailActivityFragment.DETAIL_MOVIE);
            } else {
                picture = (Pictures) getActivity().getIntent().getExtras().get(DetailActivityFragment.DETAIL_MOVIE);
            }
            collapsingToolbar.setTitle(picture.getTitle());
            getVideosData();
            getReviewsData();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootView;
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        collapsingToolbar.setTitle(picture.getTitle());
        draweeView.setImageURI("http://image.tmdb.org/t/p/w780" + picture.getBackdropPath());
        releaseDate.setText("Released Date: " + picture.getReleaseDate() + "");
        vote.setText("Rating: " + picture.getVoteAverage() + "/10");
        plotSynopsis.setText(picture.getOverview());
        fav_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fav_button.setBackground(getContext().getResources().getDrawable(R.drawable.fav));
                setDataInDb(picture);
            }
        });
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

                        if (videos.getResults().size() < 1) {
                            trailer_text.setVisibility(View.GONE);
                        } else {
                            trailer_text.setVisibility(View.VISIBLE);
                            trailer_text.setText("Trailers");
                            gridView.setVisibility(View.VISIBLE);
                            dataAdapter = new VideoAdapter(getActivity(), videos.getResults());
                            dataAdapter.notifyDataSetChanged();
                            gridView.setAdapter(dataAdapter);
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
                        if (review.getReviewsResults().size() < 1) {
                            review_text.setVisibility(View.GONE);
                        } else {
                            review_text.setVisibility(View.VISIBLE);
                            review_text.setText("Reviews");
                            LinearLayout review_layout = (LinearLayout) rootView.findViewById(R.id.review_comment);
                            review_layout.setVisibility(View.VISIBLE);
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


    class LoadDetailPageThread extends Thread {
        int requestType;

        public LoadDetailPageThread(int requestType) {
            this.requestType = requestType;
        }


        @Override
        public void run() {
            super.run();
            try {
                String imageUri = "http://image.tmdb.org/t/p/w342" + picture.getBackdropPath();
                InputStream URLcontent = (InputStream) new URL(imageUri).getContent();
                image = Drawable.createFromStream(URLcontent, "your source link");
            } catch (IOException e) {
                e.printStackTrace();
            }
            eventBus.post(new MessageEvent(requestType));
        }

    }
}