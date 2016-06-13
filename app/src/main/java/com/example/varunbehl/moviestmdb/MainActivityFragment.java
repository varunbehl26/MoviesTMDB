package com.example.varunbehl.moviestmdb;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.varunbehl.moviestmdb.db2.MovieDetailContract;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */

public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static String sort = "popular";
    private static final String SORT_POPULAR = "popular";
    private static final String SORT_RATING = "top_rated";
    static String API_KEY = "29c90a4aee629499a2149041cc6a0ffd";
    private RetrofitManager retrofitManager;
    private List<Pictures> movieList = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private ImageAdapter_List imageAdapter_list;
    SharedPreferences sharedPreferences;
    GridView gridView;
    private final String MOVIE_DATA = "movie_data";


    public MainActivityFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofitManager = RetrofitManager.getInstance();
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        imageAdapter_list = new ImageAdapter_List(getActivity(), movieList);
        gridView = (GridView) rootView.findViewById(R.id.gridView1);
        fetchData();
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_sort_rating) {
            if (sort.equals("rating")) {
                Toast.makeText(getActivity(), "Sorted on  Rating", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                sort = "rating";
                fetchData();
                return true;
            }
        } else if (id == R.id.action_sort_popular) {
            if (sort.equals("popular")) {
                Toast.makeText(getActivity(), "Sorted on  Popularity", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                sort = "popular";
                fetchData();
                return true;
            }
        } else if (id == R.id.action_favourite) {
            if (sort.equals("favourite")) {
                Toast.makeText(getActivity(), "Sorted on  Popularity", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                sort = "favourite";
                fetchData();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public List<Pictures> setDataForDetailView(final Cursor cur1) {
        List<Pictures> picturesList = new ArrayList<>();
        while (cur1.moveToNext()) {
            Pictures pictures = new Pictures(
                    cur1.getString(cur1.getColumnIndex(MovieDetailContract.MovieEntry.POSTER_PATH)),
                    Boolean.parseBoolean(cur1.getString(cur1.getColumnIndex(MovieDetailContract.MovieEntry.ADULT))),
                    cur1.getString(cur1.getColumnIndex(MovieDetailContract.MovieEntry.OVERVIEW)),
                    cur1.getString(cur1.getColumnIndex(MovieDetailContract.MovieEntry.RELEASE_DATE)),
                    Integer.parseInt(cur1.getString(cur1.getColumnIndex(MovieDetailContract.MovieEntry.MOVIE_ID))),
                    cur1.getString(cur1.getColumnIndex(MovieDetailContract.MovieEntry.ORIGINAL_TITLE)),
                    cur1.getString(cur1.getColumnIndex(MovieDetailContract.MovieEntry.ORIGINAL_LANGUAGE)),
                    cur1.getString(cur1.getColumnIndex(MovieDetailContract.MovieEntry.BACKDROP_PATH)),
                    Double.parseDouble(cur1.getString(cur1.getColumnIndex(MovieDetailContract.MovieEntry.POPULARITY))),
                    Integer.parseInt(cur1.getString(cur1.getColumnIndex(MovieDetailContract.MovieEntry.VOTE_COUNT))),
                    Boolean.parseBoolean(cur1.getString(cur1.getColumnIndex(MovieDetailContract.MovieEntry.VIDEO))),
                    Double.parseDouble(cur1.getString(cur1.getColumnIndex(MovieDetailContract.MovieEntry.VOTE_AVERAGE))
                    ));
            Log.v("picture set", pictures.toString());
            picturesList.add(pictures);
        }
        return picturesList;
    }


    private void fetchData() {
        if (sort.equals("favourite")) {
            Cursor cur1 = getActivity().getContentResolver().query(MovieDetailContract.MovieEntry.buildMovieUri(),
                    null, null, null, null);
            movieList.clear();
            if ((cur1.moveToFirst()) || cur1.getCount() != 0) {
                movieList = setDataForDetailView(cur1);
                imageAdapter_list = new ImageAdapter_List(getActivity(), movieList);
                imageAdapter_list.notifyDataSetChanged();
                gridView.setAdapter(imageAdapter_list);


                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
                        Pictures pictures = imageAdapter_list.getItem(position);
                        Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra("Pictures", pictures);
                        startActivity(intent);
                    }
                });


                cur1.close();

            }
        } else {
            getDataFromWeb();
        }
    }


    public void getDataFromWeb() {

        Call<Picture_Detail> call;
        if (sort.equals("rating")) {
            call = retrofitManager.getMoviesInfo(SORT_RATING, 1, API_KEY);
        } else {
            call = retrofitManager.getMoviesInfo(SORT_POPULAR, 1, API_KEY);
        }

        call.enqueue(new Callback<Picture_Detail>() {


                         @Override
                         public void onResponse(Call<Picture_Detail> call, Response<Picture_Detail> response) {
                             try {
                                 movieList = response.body().getResults();


                                 imageAdapter_list = new ImageAdapter_List(getActivity(), movieList);
                                 imageAdapter_list.notifyDataSetChanged();
                                 gridView.setAdapter(imageAdapter_list);
                                 gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                     public void onItemClick(AdapterView<?> parent, View v,
                                                             int position, long id) {
                                         Pictures pictures = imageAdapter_list.getItem(position);
                                         //  Cursor cursor = (Cursor) imageAdapter_list.getItem(position);
                                         Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra("Pictures", pictures);
                                         startActivity(intent);
                                         //  setDataForDetailView(cursor);
                                     }
                                 });
                             } catch (Exception e) {
                                 e.printStackTrace();
                                 Log.v("Exception", "NullPointerException");
                             }
                         }

                         @Override
                         public void onFailure(Call<Picture_Detail> call, Throwable t) {
                             Log.e("Error threw: ", t.getMessage());
                         }
                     }

        );
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri movieUri = MovieDetailContract.MovieEntry.buildMovieUri();
        return new CursorLoader(getActivity(), movieUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        imageAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}


