package com.vixir.popularmovies;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.vixir.popularmovies.sharedpref.SettingsActivity;
import com.vixir.popularmovies.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vidhya on 09-10-2016.
 */
public class ListMovieFragment extends Fragment {
    private ListMoviesGridAdapter mListMoviesGridAdapter;
    private ArrayList mMovieListData = new ArrayList();
    private GridView gridView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.list_movies_fragment, container, false);
        gridView = (GridView) v.findViewById(R.id.movies_grid);
        return v;
    }

    @Override
    public void onStart() {
               super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String orderByConnPref = sharedPref.getString(SettingsActivity.KEY_SORT_ORDER, "");
        new FetchMovieListData().execute("http://api.themoviedb.org/3/movie/" + orderByConnPref + "?api_key="+ Util.MY_TMDB_API_KEY);
    }


    private class FetchMovieListData extends AsyncTask<String, Void, ArrayList> {

        @Override
        protected ArrayList doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String listMoviesJson = null;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setInstanceFollowRedirects(false);
                URLEncoder.encode(String.valueOf(url), "UTF-8");
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode(); //can call this instead of con.connect()
                if (responseCode >= 400 && responseCode <= 499) {
                    try {
                        throw new Exception("Bad authentication status: " + responseCode); //provide a more meaningful exception message
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
                InputStream inputStream = urlConnection.getInputStream();

                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");

                }
                if (buffer.length() == 0) {
                    return null;
                }
                listMoviesJson = buffer.toString();
                mMovieListData = getMovieDetailsFromJSON(listMoviesJson);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            finally {
                if(urlConnection!=null){
                    urlConnection.disconnect();
                }
                if (reader!=null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(this.getClass().getName(),"Error closing stream"+e);
                        e.printStackTrace();
                    }
                }
            }
            return mMovieListData;
        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            if(arrayList!=null){
                mListMoviesGridAdapter = new ListMoviesGridAdapter(getContext(), arrayList);
                gridView.setAdapter(mListMoviesGridAdapter);
                mListMoviesGridAdapter.notifyDataSetChanged();
            }
            super.onPostExecute(arrayList);
        }

        private ArrayList getMovieDetailsFromJSON(String listMoviesJson) throws JSONException {
            JSONObject moviesData = new JSONObject(listMoviesJson);
            JSONArray listMoviesArray = moviesData.getJSONArray("results");
            ArrayList list = new ArrayList();
            for (int i = 0; i < listMoviesArray.length(); i++) {
                JSONObject movieData = listMoviesArray.getJSONObject(i);
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("title",movieData.getString("title"));
                map.put("poster",movieData.getString("poster_path"));
                map.put("synopsis",movieData.getString("overview"));
                map.put("voteCount",movieData.getString("vote_count"));
                map.put("popularity",movieData.getString("popularity"));
                map.put("releaseDate",movieData.getString("release_date"));
                map.put("movieId",movieData.getString("id"));
                map.put("rating",movieData.getString("vote_average"));
                map.put("language",movieData.getString("original_language"));
                map.put("backdrop",movieData.getString("backdrop_path"));
                list.add(map);
            }
            return list;
        }
    }
}
