package com.example.varunbehl.moviestmdb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.List;

import db.RetrofitManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static String sort = "popular";
    private static final String SORT_POPULAR = "popular";
    private static final String SORT_RATING = "top_rated";
    static String API_KEY = "29c90a4aee629499a2149041cc6a0ffd";
    private RetrofitManager retrofitManager;
    private List<Pictures> movieList = new ArrayList<>();
    private ImageAdapter imageAdapter;
    GridView gridView;

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
        imageAdapter = new ImageAdapter(getContext(), movieList);
        gridView = (GridView) rootView.findViewById(R.id.gridview);
        getData();
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
                getData();
                return true;
            }
        } else if (id == R.id.action_sort_popular) {
            if (sort.equals("popular")) {
                Toast.makeText(getActivity(), "Sorted on  Popularity", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                sort = "popular";
                getData();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void getData() {

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
                    imageAdapter = new ImageAdapter(getContext(), movieList);
                    imageAdapter.notifyDataSetChanged();
                    gridView.setAdapter(imageAdapter);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v,
                                                int position, long id) {
                            Pictures picture = imageAdapter.getItem(position);
                            Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra("Pictures", picture);
                            startActivity(intent);
                        }
                    });
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Log.v("Exception", "NullPointerException");
                }
            }
            @Override
            public void onFailure(Call<Picture_Detail> call, Throwable t) {
                Log.e("Error threw: ", t.getMessage());
            }
        });
    }

}


