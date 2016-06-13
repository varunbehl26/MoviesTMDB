package com.example.varunbehl.moviestmdb;

import Video.Videos;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import reviews.Reviews;
import rx.Observable;

/**
 * Singleton class for making the network call for specific url,getting response.
 */
public class RetrofitManager {

    public static Retrofit retrofit = null;
    public static MoviesInterface iMovieService = null;
    public static RetrofitManager retrofitManager = null;
    static String API_BASE_URL = "http://api.themoviedb.org/";

    private RetrofitManager() {

        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        iMovieService = retrofit.create(MoviesInterface.class);
    }

    public static RetrofitManager getInstance() {
        if (retrofitManager == null) {
            retrofitManager = new RetrofitManager();
        }
        return retrofitManager;
    }


    public Call<Picture_Detail> getMoviesInfo(String categories, int page, String apiKey) {
        Call<Picture_Detail> moviesInfoCall = iMovieService.getMoviesInfo(categories, page, apiKey);
        return moviesInfoCall;
    }

    public Observable<Reviews> getComments(int movieId, String apiKey) {
        Observable<Reviews> movieCommentsCall = iMovieService.listReviews(movieId, apiKey);
        return movieCommentsCall;
    }

    public Observable<Videos> getTrailer(int movieId, String apiKey) {
        Observable<Videos> movieCommentsCall = iMovieService.listVideos(movieId, apiKey);
        return movieCommentsCall;
    }
}
