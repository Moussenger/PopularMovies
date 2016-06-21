package edu.udacity.mou.project.popularmovies.network;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mou on 21/06/16.
 */
public abstract class AbstractGeneralMoviesNetworkTask<T> extends AsyncTask<String, Void, List<T>> {
    private final String LOG_TAG = MoviesNetworkTask.class.getSimpleName();

    private Context mContext;
    private INetworkListener mListener;

    public AbstractGeneralMoviesNetworkTask (Context context, INetworkListener listener) {
        mContext  = context;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        if(mListener != null) {
            mListener.onStartLoad();
        }
    }

    @Override
    protected List<T> doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        List<T> cache = getDataFromCache();

        if(cache != null) {
            return cache;
        }

        String jsonResponse = null;

        try {

            Uri uri = getUri();
            URL url = new URL(uri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) { return null; }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) { buffer.append(line + "\n"); }

            if (buffer.length() == 0) { return null; }

            jsonResponse = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) { urlConnection.disconnect(); }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return parseResponse(jsonResponse);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    protected List<T> parseResponse (String response) throws JSONException {
        final String RESULTS = "results";

        ArrayList<T> data = new ArrayList<>();

        JSONObject dataJson = new JSONObject(response);
        JSONArray dataResult = dataJson.getJSONArray(RESULTS);

        for(int i=0, length = dataResult.length(); i < length; ++i) {
            JSONObject jsonObject = dataResult.getJSONObject(i);

            T object = parseObject(jsonObject);

            if(object != null) {
                data.add(object);
            }
        }

        return data;
    }

    @Override
    protected void onPostExecute(List<T> data) {
        if(mListener != null) {
            mListener.onDataLoaded(data);
        }
    }

    protected abstract T parseObject (JSONObject object) throws JSONException;

    protected abstract List<T> getDataFromCache ();

    protected abstract Uri getUri ();

    protected Context getContext () {
        return mContext;
    }

    public interface INetworkListener<D> {
        void onStartLoad();
        void onDataLoaded(List<D> data);
    }
}
