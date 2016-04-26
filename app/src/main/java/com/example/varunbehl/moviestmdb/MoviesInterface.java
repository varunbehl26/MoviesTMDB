package com.example.varunbehl.moviestmdb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by varunbehl on 14/04/16.
 */
public interface MoviesInterface {
    @GET("3/movie/popular")
    Call<Picture_Detail> listPictures(@Query("api_key") String api_key);

    @GET("3/movie/top_rated")
    Call<Picture_Detail> listPic_top(@Query("api_key") String api_key);
}