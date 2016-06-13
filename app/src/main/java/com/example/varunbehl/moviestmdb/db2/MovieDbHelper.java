package com.example.varunbehl.moviestmdb.db2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by varunbehl on 03/05/16.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieDetailContract.MovieEntry.TABLE_NAME + "(" + //
                MovieDetailContract.MovieEntry._ID + " INTEGER PRIMARY KEY," + // 0: MOVIE_ID
                MovieDetailContract.MovieEntry.MOVIE_ID + " INTEGER ," + // 0: MOVIE_ID
                MovieDetailContract.MovieEntry.POSTER_PATH + " TEXT ," + // 1: posterPath
                MovieDetailContract.MovieEntry.OVERVIEW + " TEXT," + // 2: overview
                MovieDetailContract.MovieEntry.RELEASE_DATE + " TEXT," + // 3: releaseDate
                MovieDetailContract.MovieEntry.ORIGINAL_TITLE + " TEXT," + // 4: originalTitle
                MovieDetailContract.MovieEntry.ORIGINAL_LANGUAGE + " TEXT," + // 5: originalLanguage
                MovieDetailContract.MovieEntry.BACKDROP_PATH + " TEXT," + // 6: backdropPath
                MovieDetailContract.MovieEntry.ADULT + " INTEGER," + // 7: adult
                MovieDetailContract.MovieEntry.VIDEO + " INTEGER," + // 8: video
                MovieDetailContract.MovieEntry.POPULARITY + " REAL," + // 9: popularity
                MovieDetailContract.MovieEntry.VOTE_AVERAGE + " REAL," + // 10: voteAverage
                MovieDetailContract.MovieEntry.VOTE_COUNT + " INTEGER);"; // 11: voteCount

        db.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieDetailContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
