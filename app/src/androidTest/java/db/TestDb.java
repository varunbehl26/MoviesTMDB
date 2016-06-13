package db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.varunbehl.moviestmdb.db2.MovieDbHelper;
import com.example.varunbehl.moviestmdb.db2.MovieDetailContract;

import java.util.HashSet;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase("movie.db");
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    @Override
    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MovieDetailContract.MovieEntry.TABLE_NAME);

        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);

        SQLiteDatabase db = new MovieDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());


        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + MovieDetailContract.MovieEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        c = db.rawQuery("PRAGMA table_info(" + MovieDetailContract.MovieEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        HashSet<String> movieColumnHashSet = new HashSet<String>();
        movieColumnHashSet.add(MovieDetailContract.MovieEntry._ID);
        movieColumnHashSet.add(MovieDetailContract.MovieEntry.POSTER_PATH);
        movieColumnHashSet.add(MovieDetailContract.MovieEntry.OVERVIEW);
        movieColumnHashSet.add(MovieDetailContract.MovieEntry.RELEASE_DATE);
        movieColumnHashSet.add(MovieDetailContract.MovieEntry.ORIGINAL_TITLE);
        movieColumnHashSet.add(MovieDetailContract.MovieEntry.ORIGINAL_LANGUAGE);
        movieColumnHashSet.add(MovieDetailContract.MovieEntry.BACKDROP_PATH);
        movieColumnHashSet.add(MovieDetailContract.MovieEntry.ADULT);
        movieColumnHashSet.add(MovieDetailContract.MovieEntry.VIDEO);
        movieColumnHashSet.add(MovieDetailContract.MovieEntry.POPULARITY);
        movieColumnHashSet.add(MovieDetailContract.MovieEntry.VOTE_AVERAGE);
        movieColumnHashSet.add(MovieDetailContract.MovieEntry.VOTE_COUNT);


        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            movieColumnHashSet.remove(columnName);
        } while (c.moveToNext());


        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                movieColumnHashSet.isEmpty());
        db.close();
    }


    public void testInsertDatabase() {

        SQLiteDatabase sqLiteDatabase;
        MovieDbHelper movieDbHelper = new MovieDbHelper(getContext());

        sqLiteDatabase = movieDbHelper.getWritableDatabase();


        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieDetailContract.MovieEntry._ID, 1111);
        contentValues.put(MovieDetailContract.MovieEntry.ORIGINAL_TITLE, "Bagmati");
        contentValues.put(MovieDetailContract.MovieEntry.RELEASE_DATE, "2015-10-20");
        contentValues.put(MovieDetailContract.MovieEntry.POPULARITY, 7.8);
        contentValues.put(MovieDetailContract.MovieEntry.OVERVIEW, " this is the horror movie");

        long recordId = sqLiteDatabase.insert(MovieDetailContract.MovieEntry.TABLE_NAME, null, contentValues);

        assertTrue(recordId != -1);

        Cursor cursor = sqLiteDatabase.query(MovieDetailContract.MovieEntry.TABLE_NAME, null, null, null, null, null, null);

        assertTrue(cursor.getCount() != -1);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
