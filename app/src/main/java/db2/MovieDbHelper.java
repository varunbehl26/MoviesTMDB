package db2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by varunbehl on 03/05/16.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + "\"MOVIE_DETAIL\" (" + //
                "\"_ID\" INTEGER PRIMARY KEY," + // 0: ID
                "\"POSTER_PATH\" TEXT NOT NULL ," + // 1: posterPath
                "\"OVERVIEW\" TEXT," + // 2: overview
                "\"RELEASE_DATE\" TEXT," + // 3: releaseDate
                "\"ORIGINAL_TITLE\" TEXT," + // 4: originalTitle
                "\"ORIGINAL_LANGUAGE \" TEXT," + // 5: originalLanguage
                "\"BACKDROP_PATH\" TEXT," + // 6: backdropPath
                "\"ADULT\" INTEGER," + // 7: adult
                "\"VIDEO\" INTEGER," + // 8: video
                "\"POPULARITY\" REAL," + // 9: popularity
                "\"VOTE_AVERAGE\" REAL," + // 10: voteAverage
                "\"VOTE_COUNT\" INTEGER);"; // 11: voteCount

        db.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS MOVIE_DETAIL");
        onCreate(db);
    }
}
