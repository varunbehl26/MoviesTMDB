package com.example.varunbehl.moviestmdb;

import reviews.Reviews;
import Video.Videos;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by varunbehl on 14/04/16.
 */
public interface MoviesInterface {

    @GET("3/movie/{categories}")
    Call<Picture_Detail> getMoviesInfo(@Path("categories") String categories, @Query("page") int page, @Query("api_key") String apiKey);

    @GET("3/movie/{id}/videos")
    Observable<Videos> listVideos(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("3/movie/{id}/reviews")
    Observable<Reviews> listReviews(@Path("id") int id, @Query("api_key") String apiKey);


}